package com.suixingpay.datas.manager.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.suixingpay.datas.manager.core.entity.Nodes;
import com.suixingpay.datas.manager.web.page.Page;

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
     * 分頁
     *
     * @return
     */
    List<Nodes> page(@Param("page") Page<Nodes> page,
                     @Param("ipAddress") String ipAddress,
                     @Param("state") Integer state,
                     @Param("machineName") String machineName);

    /**
     * 分頁All
     *
     * @param ipAddress
     * @param state
     * @param machineName
     * @return
     */
    Integer pageAll(@Param("ipAddress") String ipAddress,
                    @Param("state") Integer state,
                    @Param("machineName") String machineName);

    /**
     * 节点状态
     *
     * @param nodeId
     * @param state
     * @return
     */
    Integer updateState(@Param("nodeId") String nodeId, @Param("state") Integer state);

    /**
     * 修改心跳时间并且变更节点状态
     *
     * @param nodeId
     * @param heartBeatTime
     * @return
     */
    Integer updateHeartBeatTime(@Param("nodeId") String nodeId, @Param("heartBeatTime") String heartBeatTime);

    /**
     * 验证nodeId是否重复
     *
     * @param nodeId
     * @return
     */
    Integer testNodeId(@Param("nodeId") String nodeId);
}