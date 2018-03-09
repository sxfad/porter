package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.MrNodesMonitor;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点任务实时监控表 Mapper接口
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrNodesMonitorMapper {

    /**
     * 新增
     * @param mrNodesMonitor
     */
    Integer insert(MrNodesMonitor mrNodesMonitor);

    /**
     * 修改
     * @param mrNodesMonitor
     */
    Integer update(@Param("id") Long id, @Param("mrNodesMonitor") MrNodesMonitor mrNodesMonitor);

    /**
     * 刪除
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根據主鍵id查找數據
     * @param id
     * @return
     */
    MrNodesMonitor selectById(Long id);

    /**
     * 分頁
     * @return
     */
    List<MrNodesMonitor> page(@Param("page") Page<MrNodesMonitor> page, @Param("state") Integer state);

    /**
     * 分頁All
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

}