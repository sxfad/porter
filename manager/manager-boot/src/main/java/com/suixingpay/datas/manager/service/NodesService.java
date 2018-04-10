package com.suixingpay.datas.manager.service;

import java.util.List;

import com.suixingpay.datas.common.cluster.data.DNode;
import com.suixingpay.datas.common.dic.NodeStatusType;
import com.suixingpay.datas.manager.core.entity.Nodes;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 节点信息表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface NodesService {

    Integer insert(Nodes nodes);

    Integer update(Long id, Nodes nodes);

    Integer delete(Long id);

    Integer cancel(Long id);

    Integer taskPushState(Long id, NodeStatusType taskPushState);

    Nodes selectById(Long id);

    Nodes selectByNodeId(String nodeId);

    Page<Nodes> page(Page<Nodes> page, String ipAddress, Integer state, String machineName);

    Integer insertState(DNode node, String heartBeatTime, Integer state);

    Integer updateState(DNode node, String heartBeatTime, Integer state);

    Integer updateHeartBeatTime(DNode node, String heartBeatTime);

    Integer insertState(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime, Integer state);

    Integer updateState(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime, Integer state);

    Integer updateHeartBeatTime(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime);

    boolean testNodeId(String nodeId);

    List<Nodes> selectList();
}
