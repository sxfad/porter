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

package cn.vbill.middleware.porter.manager.service;

import java.util.List;

import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import cn.vbill.middleware.porter.manager.core.entity.Nodes;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 节点信息表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface NodesService {

    /**
     * 新增
     *
     * @date 2018/8/10 下午2:07
     * @param: [nodes]
     * @return: java.lang.Integer
     */
    Integer insert(Nodes nodes);

    /**
     * 更新
     *
     * @date 2018/8/10 下午2:11
     * @param: [id, nodes]
     * @return: java.lang.Integer
     */
    Integer update(Long id, Nodes nodes);

    /**
     * 删除
     *
     * @date 2018/8/10 下午2:11
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * canel
     *
     * @date 2018/8/10 下午2:11
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer cancel(Long id);

    /**
     * taskPushState
     *
     * @date 2018/8/10 下午2:11
     * @param: [id, taskPushState]
     * @return: java.lang.Integer
     */
    Integer taskPushState(Long id, NodeStatusType taskPushState);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 下午2:11
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.Nodes
     */
    Nodes selectById(Long id);

    /**
     * 根据nodeid查询
     *
     * @date 2018/8/10 下午2:12
     * @param: [nodeId]
     * @return: cn.vbill.middleware.porter.manager.core.entity.Nodes
     */
    Nodes selectByNodeId(String nodeId);

    /**
     * 分页
     *
     * @date 2018/8/10 下午2:12
     * @param: [page, ipAddress, state, machineName]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.Nodes>
     */
    Page<Nodes> page(Page<Nodes> page, String ipAddress, Integer state, String machineName);

    /**
     * insertState
     *
     * @date 2018/8/10 下午2:12
     * @param: [node, heartBeatTime, state]
     * @return: java.lang.Integer
     */
    Integer insertState(DNode node, String heartBeatTime, Integer state);

    /**
     * updateState
     *
     * @date 2018/8/10 下午2:12
     * @param: [node, heartBeatTime, state]
     * @return: java.lang.Integer
     */
    Integer updateState(DNode node, String heartBeatTime, Integer state);

    /**
     * 更新心跳时间
     *
     * @date 2018/8/10 下午2:13
     * @param: [node, heartBeatTime]
     * @return: java.lang.Integer
     */
    Integer updateHeartBeatTime(DNode node, String heartBeatTime);

    /**
     * insertState
     *
     * @date 2018/8/10 下午2:13
     * @param: [nodeId, machineName, ipAddress, pidNumber, taskPushState, heartBeatTime, state]
     * @return: java.lang.Integer
     */
    Integer insertState(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime, Integer state);

    /**
     * updateState
     *
     * @date 2018/8/10 下午2:13
     * @param: [nodeId, machineName, ipAddress, pidNumber, taskPushState, heartBeatTime, state]
     * @return: java.lang.Integer
     */
    Integer updateState(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime, Integer state);

    /**
     * 更新心跳时间
     *
     * @date 2018/8/10 下午2:13
     * @param: [nodeId, machineName, ipAddress, pidNumber, taskPushState, heartBeatTime]
     * @return: java.lang.Integer
     */
    Integer updateHeartBeatTime(String nodeId, String machineName, String ipAddress, String pidNumber,
            NodeStatusType taskPushState, String heartBeatTime);

    /**
     * testNodeId
     *
     * @date 2018/8/10 下午2:14
     * @param: [nodeId]
     * @return: boolean
     */
    boolean testNodeId(String nodeId);

    /**
     * selectList
     *
     * @date 2018/8/10 下午2:14
     * @param: []
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.Nodes>
     */
    List<Nodes> selectList();
}
