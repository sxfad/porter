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
