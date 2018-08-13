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
import cn.vbill.middleware.porter.common.cluster.command.NodeOrderPushCommand;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.NodeOrderPush;
import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.core.util.DateFormatUtils;
import cn.vbill.middleware.porter.manager.service.MrNodesScheduleService;
import cn.vbill.middleware.porter.manager.service.NodesService;
import cn.vbill.middleware.porter.manager.service.impl.MrNodesScheduleServiceImpl;
import cn.vbill.middleware.porter.manager.service.impl.NodesServiceImpl;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 节点监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterNodeListener extends ZookeeperClusterListener implements NodeOrderPush {
    private static final String ZK_PATH = BASE_CATALOG + "/node";
    // private static final Pattern NODE_ORDER_PATTERN = Pattern.compile(ZK_PATH +
    // "/.*/order/.*");
    private static final Pattern NODE_STAT_PATTERN = Pattern.compile(ZK_PATH + "/.*/stat");
    private static final Pattern NODE_LOCK_PATTERN = Pattern.compile(ZK_PATH + "/.*/lock");

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKClusterNodeListener.class);

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        LOGGER.debug("NodeListener:{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        try {
            // 当前时间
            //String heartBeatTime = DateFormatUtils.formatDate(DateFormatUtils.PATTERN_DEFAULT, new Date());
            NodesService nodesService = ApplicationContextUtil.getBean(NodesServiceImpl.class);
            // 节点上下线
            if (NODE_LOCK_PATTERN.matcher(zkEvent.getPath()).matches()) {
                String nodeInfoPath = zkEvent.getPath().replace("/lock", "/stat");
                DNode node = getDNode(nodeInfoPath);
                String heartBeatTime = DateFormatUtils.formatDate(DateFormatUtils.PATTERN_DEFAULT, node.getHeartbeat());
                if (zkEvent.isOnline()) { // 节点上线
                    LOGGER.info("节点[{}]上线", node.getNodeId());
                    // 服务启动，在线通知
                    int i = nodesService.updateState(node, heartBeatTime, 1);
                    if (i == 0) {
                        nodesService.insertState(node, heartBeatTime, 1);
                        LOGGER.warn("节点[{}]尚未完善管理后台节点信息，请及时配置！", node.getNodeId());
                    }
                }
                if (zkEvent.isOffline()) { // 节点下线
                    // do something 服务停止，离线通知
                    int i = nodesService.updateState(node, heartBeatTime, -1);
                    LOGGER.info("节点[{}]下线", node.getNodeId());
                    if (i == 0) {
                        LOGGER.warn("节点[{}]尚未完善管理后台节点信息，请及时配置！", node.getNodeId());
                        nodesService.insertState(node, heartBeatTime, -1);
                    }
                }
            }

            // 节点状态更新
            if (NODE_STAT_PATTERN.matcher(zkEvent.getPath()).matches()) {
                DNode node = getDNode(zkEvent.getPath());
                String heartBeatTime = DateFormatUtils.formatDate(DateFormatUtils.PATTERN_DEFAULT, node.getHeartbeat());
                LOGGER.info("节点[{}]状态上报", node.getNodeId());
                LOGGER.info("2-DNode...." + node.getNodeId() + "..." + JSON.toJSONString(node));
                // do something 心跳时间记录 并且表示节点在线
                int i = nodesService.updateHeartBeatTime(node, heartBeatTime);
                if (i == 0) {
                    LOGGER.warn("节点[{}]尚未完善管理后台节点信息，请及时配置！", node.getNodeId());
                    nodesService.insertState(node, heartBeatTime, 1);
                }
                MrNodesScheduleService mrNodesScheduleService = ApplicationContextUtil
                        .getBean(MrNodesScheduleServiceImpl.class);
                mrNodesScheduleService.dealDNode(node);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            LOGGER.error("2-DNode-Error....出错,请追寻...", e);
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ZookeeperClusterListenerFilter() {
            @Override
            protected String getPath() {
                return listenPath();
            }

            @Override
            protected boolean doFilter(ZookeeperClusterEvent event) {
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
        Pair<String, Stat> dataPair = client.getData(nodePath);
        return DNode.fromString(dataPair.getLeft(), DNode.class);
    }

    @Override
    public void push(NodeOrderPushCommand command) throws Exception {
        String nodePath = ZK_PATH + "/" + command.getConfig().getNodeId();
        String baseOrderPath = nodePath + "/order";
        String orderPath = baseOrderPath + "/" + UUID.randomUUID().toString();
        client.createWhenNotExists(nodePath, false, true, null);
        client.createWhenNotExists(baseOrderPath, false, true, null);
        client.changeData(orderPath, true, false, command.render());
    }
}