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

package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.dto.RoleDataControl;
import cn.vbill.middleware.porter.manager.service.NodesOwnerService;
import cn.vbill.middleware.porter.manager.service.NodesService;
import cn.vbill.middleware.porter.common.statistics.DNode;
import cn.vbill.middleware.porter.common.node.dic.NodeStatusType;
import cn.vbill.middleware.porter.manager.core.entity.Nodes;
import cn.vbill.middleware.porter.manager.core.mapper.NodesMapper;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private NodesOwnerService nodesOwnerService;

    @Override
    @Transactional
    public Integer insert(Nodes nodes) {
        // 验证nodeId是否重复
        Integer total = nodesMapper.testNodeId(nodes.getNodeId());
        Integer number = 0;
        if (total > 0) {
            number = -1;
            return number;
        } else {
            nodes.setTaskPushState(NodeStatusType.SUSPEND);
            nodes.setState(-1);
            number = nodesMapper.insert(nodes);
            nodesOwnerService.insertByNodes(nodes.getId());
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
    public Nodes selectByNodeId(String nodeId) {
        return nodesMapper.selectByNodeId(nodeId);
    }

    @Override
    public Page<Nodes> page(Page<Nodes> page, String nodeId, String ipAddress, Integer state, String machineName) {
        RoleDataControl roleDataControl = RoleCheckContext.getUserIdHolder();
        Integer total = nodesMapper.pageAll(nodeId, ipAddress, state, machineName, roleDataControl);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(nodesMapper.page(page, nodeId, ipAddress, state, machineName, roleDataControl));
        }
        return page;
    }

    @Override
    public Integer insertState(DNode node, String heartBeatTime, Integer state) {
        return this.insertState(node.getNodeId(), node.getHostName(), node.getAddress(), node.getProcessId(),
                node.getStatus(), heartBeatTime, state);
    }

    @Override
    public Integer updateState(DNode node, String heartBeatTime, Integer state) {
        return this.updateState(node.getNodeId(), node.getHostName(), node.getAddress(), node.getProcessId(),
                node.getStatus(), heartBeatTime, state);
    }

    @Override
    public Integer updateHeartBeatTime(DNode node, String heartBeatTime) {
        return updateHeartBeatTime(node.getNodeId(), node.getHostName(), node.getAddress(),
                node.getProcessId(), node.getStatus(), heartBeatTime);
    }

    @Override
    public Integer insertState(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime, Integer state) {
        return nodesMapper.insertState(nodeId, machineName, ipAddress, pidNumber,
                taskPushState == null ? NodeStatusType.SUSPEND.getCode() : taskPushState.getCode(), heartBeatTime,
                state);
    }

    @Override
    public Integer updateState(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime, Integer state) {
        return nodesMapper.updateState(nodeId, machineName, ipAddress, pidNumber,
                taskPushState == null ? NodeStatusType.SUSPEND.getCode() : taskPushState.getCode(), heartBeatTime,
                state);
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

    @Override
    public List<Nodes> selectList() {
        return nodesMapper.selectList();
    }
}
