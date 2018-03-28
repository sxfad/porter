/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.cluster.zookeeper;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.data.Stat;

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.command.NodeOrderPushCommand;
import com.suixingpay.datas.common.cluster.command.broadcast.NodeOrderPush;
import com.suixingpay.datas.common.cluster.data.DNode;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import com.suixingpay.datas.manager.core.util.ApplicationContextUtil;
import com.suixingpay.datas.manager.core.util.DateFormatUtils;
import com.suixingpay.datas.manager.service.NodesService;
import com.suixingpay.datas.manager.service.impl.NodesServiceImpl;

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
    //private static final Pattern NODE_ORDER_PATTERN = Pattern.compile(ZK_PATH + "/.*/order/.*");
    private static final Pattern NODE_STAT_PATTERN = Pattern.compile(ZK_PATH + "/.*/stat");
    private static final Pattern NODE_LOCK_PATTERN = Pattern.compile(ZK_PATH + "/.*/lock");

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        LOGGER.debug("NodeListener:{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        // 当前时间
        String heartBeatTime = DateFormatUtils.formatDate(DateFormatUtils.PATTERN_DEFAULT, new Date());
        NodesService nodesService = ApplicationContextUtil.getBean(NodesServiceImpl.class);
        // 节点上下线
        if (NODE_LOCK_PATTERN.matcher(zkEvent.getPath()).matches()) {
            String nodeInfoPath = zkEvent.getPath().replace("/lock", "/stat");
            DNode node = getDNode(nodeInfoPath);

            if (zkEvent.isOnline()) { // 节点上线
                // 服务启动，在线通知
                int i = nodesService.updateState(node.getNodeId(),node.getHostName(),node.getAddress(),node.getProcessId(), heartBeatTime, 1);
                LOGGER.info("节点[{}]上线", node.getNodeId());
                if (i == 0) {
                    nodesService.insertState(node.getNodeId(),node.getHostName(),node.getAddress(),node.getProcessId(), heartBeatTime, 1);
                    LOGGER.warn("节点[{}]尚未完善管理后台节点信息，请及时配置！", node.getNodeId());
                }
            }
            if (zkEvent.isOffline()) { // 节点下线
                // do something 服务停止，离线通知
                int i = nodesService.updateState(node.getNodeId(),node.getHostName(),node.getAddress(),node.getProcessId(), heartBeatTime, -1);
                LOGGER.info("节点[{}]下线", node.getNodeId());
                if (i == 0) {
                    LOGGER.warn("节点[{}]尚未完善管理后台节点信息，请及时配置！", node.getNodeId());
                    nodesService.insertState(node.getNodeId(),node.getHostName(),node.getAddress(),node.getProcessId(), heartBeatTime, -1);
                }
            }
        }

        // 节点状态更新
        if (NODE_STAT_PATTERN.matcher(zkEvent.getPath()).matches()) {
            DNode node = getDNode(zkEvent.getPath());
            System.err.println("DNode...."+node.getNodeId()+"..."+JSON.toJSONString(node));
            // do something 心跳时间记录 并且表示节点在线
            int i = nodesService.updateHeartBeatTime(node.getNodeId(), node.getHostName(),node.getAddress(),node.getProcessId(),heartBeatTime);
            LOGGER.info("节点[{}]状态上报", node.getNodeId());
            if (i == 0) {
                LOGGER.warn("节点[{}]尚未完善管理后台节点信息，请及时配置！", node.getNodeId());
                nodesService.insertState(node.getNodeId(),node.getHostName(),node.getAddress(),node.getProcessId(), heartBeatTime, 1);
            }
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

    private DNode getDNode(String nodePath) {
        Pair<String, Stat> dataPair = client.getData(nodePath);
        return DNode.fromString(dataPair.getLeft(), DNode.class);
    }

    @Override
    public void push(NodeOrderPushCommand command) throws Exception {
        String nodePath = ZK_PATH + "/" + command.getConfig().getNodeId();
        String baseOrderPath = "/" + nodePath + "/order";
        String orderPath = "/" + baseOrderPath + UUID.randomUUID().toString();

        client.createWhenNotExists(nodePath, false, true, null);
        client.createWhenNotExists(baseOrderPath, false, true, null);
        client.changeData(orderPath, false, false, command.render());
    }
}