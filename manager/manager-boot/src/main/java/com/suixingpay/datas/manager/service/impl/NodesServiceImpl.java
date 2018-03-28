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
        nodes.setTaskPushState(NodeStatusType.SUSPEND);
        nodes.setState(-1);
        return nodesMapper.insert(nodes);
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
        //Nodes nodes = nodesMapper.selectById(id);
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
    public Integer updateState(String nodeId, Integer state) {
        return nodesMapper.updateState(nodeId, state);
    }

    @Override
    public Integer updateHeartBeatTime(String nodeId, String heartBeatTime) {
        return nodesMapper.updateHeartBeatTime(nodeId, heartBeatTime);
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
