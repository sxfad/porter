package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.MrNodesSchedule;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点任务监控表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrNodesScheduleMapper {

    /**
     * 新增
     *
     * @param mrNodesSchedule
     */
    Integer insert(MrNodesSchedule mrNodesSchedule);

    /**
     * 修改
     *
     * @param mrNodesSchedule
     */
    Integer update(@Param("id") Long id, @Param("mrNodesSchedule") MrNodesSchedule mrNodesSchedule);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    MrNodesSchedule selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<MrNodesSchedule> page(@Param("page") Page<MrNodesSchedule> page,
                               @Param("state") Integer state,
                               @Param("ipAddress") String ipAddress,
                               @Param("computerName") String computerName);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state, @Param("ipAddress") String ipAddress, @Param("computerName") String computerName);

}