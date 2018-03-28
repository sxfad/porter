/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.common.dic.NodeStatusType;
import com.suixingpay.datas.manager.core.entity.Nodes;
import com.suixingpay.datas.manager.core.mapper.NodesMapper;
import com.suixingpay.datas.manager.service.NodesService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 节点信息表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class NodesServiceImpl implements NodesService {

    @Autowired
    private NodesMapper nodesMapper;

    @Override
    public Integer insert(Nodes nodes) {
        //验证nodeId是否重复
        Integer total = nodesMapper.testNodeId(nodes.getNodeId());
        Integer number = 0;
        if (total > 0) {
            number = -1;
            return number;
        } else {
            nodes.setTaskPushState(NodeStatusType.SUSPEND);
            nodes.setState(-1);
            number = nodesMapper.insert(nodes);
            return number;
        }
    }

    @Override
    public Integer update(Long id, Nodes nodes) {
        return nodesMapper.update(id, nodes);
    }

    @Override
    public Integer cancel(Long id) {
        return nodesMapper.cancel(id);
    }

    @Override
    public Integer taskPushState(Long id, NodeStatusType taskPushState) {
        Integer i = nodesMapper.taskPushState(id, taskPushState.getCode());
        return i;
    }

    @Override
    public Integer delete(Long id) {
        return nodesMapper.delete(id);
    }

    @Override
    public Nodes selectById(Long id) {
        return nodesMapper.selectById(id);
    }

    @Override
    public Page<Nodes> page(Page<Nodes> page, String ipAddress, Integer state, String machineName) {
        Integer total = nodesMapper.pageAll(ipAddress, state, machineName);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(nodesMapper.page(page, ipAddress, state, machineName));
        }
        return page;
    }

    @Override
    public Integer insertState(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime, Integer state) {
        return nodesMapper.insertState(nodeId, machineName, ipAddress, pidNumber,
                taskPushState == null ? NodeStatusType.SUSPEND.getCode() : taskPushState.getCode(), heartBeatTime, state);
    }

    @Override
    public Integer updateState(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime, Integer state) {
        return nodesMapper.updateState(nodeId, machineName, ipAddress, pidNumber,
                taskPushState == null ? NodeStatusType.SUSPEND.getCode() : taskPushState.getCode(), heartBeatTime, state);
    }

    @Override
    public Integer updateHeartBeatTime(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime) {
        return nodesMapper.updateHeartBeatTime(nodeId, machineName, ipAddress, pidNumber,
                taskPushState == null ? NodeStatusType.SUSPEND.getCode() : taskPushState.getCode(), heartBeatTime);
    }

    @Override
    public boolean testNodeId(String nodeId) {
        boolean flag = true;
        Integer total = nodesMapper.testNodeId(nodeId);
        if (total > 0) {
            flag = false;
        }
        return flag;
    }
}
