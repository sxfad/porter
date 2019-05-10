/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.manager.cluster.zookeeper;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.event.command.NodeOrderPushCommand;
import cn.vbill.middleware.porter.common.cluster.event.executor.TaskPushEventExecutor;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.statistics.DNode;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.core.util.DateFormatUtils;
import cn.vbill.middleware.porter.manager.service.MrNodesScheduleService;
import cn.vbill.middleware.porter.manager.service.NodesService;
import cn.vbill.middleware.porter.manager.service.impl.MrNodesScheduleServiceImpl;
import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 节点监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterNodeListener extends ZookeeperClusterListener {
    private static final String ZK_PATH = BASE_CATALOG + "/node";
    private static final Pattern NODE_STAT_PATTERN = Pattern.compile(ZK_PATH + "/.*/stat");
    private static final Pattern NODE_LOCK_PATTERN = Pattern.compile(ZK_PATH + "/.*/lock");

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent zkEvent) {
        logger.debug("NodeListener:{},{},{}", zkEvent.getId(), zkEvent.getData(), zkEvent.getEventType());
        try {
            // 当前时间
            NodesService nodesService = ApplicationContextUtil.getBean(NodesService.class);
            // 节点上下线
            if (NODE_LOCK_PATTERN.matcher(zkEvent.getId()).matches()) {
                String nodeInfoPath = zkEvent.getId().replace("/lock", "/stat");
                DNode node = getDNode(nodeInfoPath);
                String heartBeatTime = DateFormatUtils.formatDate(DateFormatUtils.PATTERN_DEFAULT, node.getHeartbeat());
                //最后心跳时间距离现在的差值，单位分钟
                long lastHeartbeatDiffOnMinutes = (new Date().getTime() - node.getHeartbeat().getTime())/1000/60;
                if (zkEvent.isOnline() && lastHeartbeatDiffOnMinutes <= 5) { // 节点上线
                    logger.info("节点[{}]上线", node.getNodeId());
                    // 服务启动，在线通知
                    int i = nodesService.updateState(node, heartBeatTime, 1);
                    if (i == 0) {
                        nodesService.insertState(node, heartBeatTime, 1);
                        logger.warn("节点[{}]尚未完善管理后台节点信息，请及时配置！", node.getNodeId());
                    }
                }
                if (zkEvent.isOffline() || (lastHeartbeatDiffOnMinutes > 5 && zkEvent.isOnline())) { // 节点下线
                    // do something 服务停止，离线通知
                    int i = nodesService.updateState(node, heartBeatTime, -1);
                    logger.info("节点[{}]下线", node.getNodeId());
                    if (i == 0) {
                        logger.warn("节点[{}]尚未完善管理后台节点信息，请及时配置！", node.getNodeId());
                        nodesService.insertState(node, heartBeatTime, -1);
                    }
                }
            }

            // 节点状态更新
            if (NODE_STAT_PATTERN.matcher(zkEvent.getId()).matches()) {
                DNode node = getDNode(zkEvent.getId());
                //一分钟内的心跳数据
                if ((new Date().getTime() - node.getHeartbeat().getTime())/1000/60 <= 5) {
                    String heartBeatTime = DateFormatUtils.formatDate(DateFormatUtils.PATTERN_DEFAULT, node.getHeartbeat());

                    logger.info("节点[{}]状态上报", node.getNodeId());
                    logger.info("2-DNode...." + node.getNodeId() + "..." + JSON.toJSONString(node));
                    // do something 心跳时间记录 并且表示节点在线
                    int i = nodesService.updateHeartBeatTime(node, heartBeatTime);
                    if (i == 0) {
                        logger.warn("节点[{}]尚未完善管理后台节点信息，请及时配置！", node.getNodeId());
                        nodesService.insertState(node, heartBeatTime, 1);
                    }
                    MrNodesScheduleService mrNodesScheduleService = ApplicationContextUtil
                            .getBean(MrNodesScheduleServiceImpl.class);
                    mrNodesScheduleService.dealDNode(node);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("2-DNode-Error....出错,请追寻...", e);
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ClusterListenerFilter() {
            @Override
            public String getPath() {
                return listenPath();
            }

            @Override
            public boolean doFilter(ClusterTreeNodeEvent event) {
                return true;
            }
        };
    }

    /**
     * getDNode
     *
     * @date 2018/8/9 下午4:17
     * @param: [nodePath]
     * @return: cn.vbill.middleware.porter.common.cluster.data.DNode
     */
    private DNode getDNode(String nodePath) {
        ClusterClient.TreeNode dataPair = client.getData(nodePath);
        return DNode.fromString(dataPair.getData(), DNode.class);
    }

    @Override
    public List<ClusterListenerEventExecutor> watchedEvents() {
        List<ClusterListenerEventExecutor> executors = new ArrayList<>();
        //任务上传事件
        executors.add(new TaskPushEventExecutor(this.getClass(), true, true, listenPath()));
        //任务注册
        executors.add(new ClusterListenerEventExecutor(this.getClass(), ClusterListenerEventType.NodeOrderPush).bind((clusterCommand, client) -> {
            NodeOrderPushCommand command = (NodeOrderPushCommand) clusterCommand;
            String nodePath = ZK_PATH + "/" + command.getConfig().getNodeId();
            String baseOrderPath = nodePath + "/order";
            String orderPath = baseOrderPath + "/" + UUID.randomUUID().toString();
            client.create(nodePath, null, false, true);
            client.create(baseOrderPath, null, false, true);
            client.changeData(orderPath, true, false, command.render());
        }, client));
        return executors;
    }
}