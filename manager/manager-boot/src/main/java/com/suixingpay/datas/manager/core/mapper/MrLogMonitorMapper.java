package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.MrLogMonitor;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日志监控信息表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrLogMonitorMapper {

    /**
     * 新增
     *
     * @param mrLogMonitor
     */
    Integer insert(MrLogMonitor mrLogMonitor);

    /**
     * 修改
     *
     * @param mrLogMonitor
     */
    Integer update(@Param("id") Long id, @Param("mrLogMonitor") MrLogMonitor mrLogMonitor);

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
    MrLogMonitor selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<MrLogMonitor> page(@Param("page") Page<MrLogMonitor> page, @Param("ipAddress") String ipAddress,
            @Param("state") Integer state, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("ipAddress") String ipAddress, @Param("state") Integer state,
            @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 复制表结构和数据到新表
     * @param mrLogMonitorName
     * @param newDate
     */
    void createTable(@Param("mrLogMonitorName") String mrLogMonitorName, @Param("newDate") String newDate);

    /**
     * 删除前天数据
     *
     * @param newDate
     */
    void deleteByDate(@Param("newDate") String newDate);

    /**
     * 删除存在30天的数据迁移表
     *
     * @param mrLogMonitorName
     */
    void dropTable(@Param("mrLogMonitorName") String mrLogMonitorName);
}