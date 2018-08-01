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

package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.Nodes;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点信息表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface NodesMapper {

    /**
     * 新增
     *
     * @param nodes
     */
    Integer insert(Nodes nodes);

    /**
     * 修改
     *
     * @param nodes
     */
    Integer update(@Param("id") Long id, @Param("nodes") Nodes nodes);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 作废
     *
     * @param id
     * @return
     */
    Integer cancel(Long id);

    /**
     * 任务推送状态
     *
     * @param id
     * @param taskPushState
     * @return
     */
    Integer taskPushState(@Param("id") Long id, @Param("taskPushState") String taskPushState);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    Nodes selectById(Long id);

    /**
     * 根據主鍵id查找數據
     *
     * @param nodeId
     * @return
     */
    Nodes selectByNodeId(String nodeId);

    /**
     * 分頁
     *
     * @return
     */
    List<Nodes> page(@Param("page") Page<Nodes> page, @Param("ipAddress") String ipAddress,
                     @Param("state") Integer state, @Param("machineName") String machineName);

    /**
     * 分頁All
     *
     * @param ipAddress
     * @param state
     * @param machineName
     * @return
     */
    Integer pageAll(@Param("ipAddress") String ipAddress, @Param("state") Integer state,
            @Param("machineName") String machineName);

    /**
     * 新增
     *
     * @param nodeId
     * @param machineName
     * @param ipAddress
     * @param pidNumber
     * @param heartBeatTime
     * @param state
     * @return
     */
    Integer insertState(@Param("nodeId") String nodeId, @Param("machineName") String machineName,
            @Param("ipAddress") String ipAddress, @Param("pidNumber") String pidNumber,
            @Param("taskPushState") String taskPushState, @Param("heartBeatTime") String heartBeatTime,
            @Param("state") Integer state);

    /**
     * 节点状态
     *
     * @param nodeId
     * @param nodeId
     * @param state
     * @return
     */
    Integer updateState(@Param("nodeId") String nodeId, @Param("machineName") String machineName,
            @Param("ipAddress") String ipAddress, @Param("pidNumber") String pidNumber,
            @Param("taskPushState") String taskPushState, @Param("heartBeatTime") String heartBeatTime,
            @Param("state") Integer state);

    /**
     * 修改心跳时间并且变更节点状态
     *
     * @param nodeId
     * @param nodeId
     * @param heartBeatTime
     * @return
     */
    Integer updateHeartBeatTime(@Param("nodeId") String nodeId, @Param("machineName") String machineName,
            @Param("ipAddress") String ipAddress, @Param("pidNumber") String pidNumber,
            @Param("taskPushState") String taskPushState, @Param("heartBeatTime") String heartBeatTime);

    /**
     * 验证nodeId是否重复
     *
     * @param nodeId
     * @return
     */
    Integer testNodeId(@Param("nodeId") String nodeId);

    /**
     * 查询所有节点
     * 
     * @return
     */
    List<Nodes> selectList();
}