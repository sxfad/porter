package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.common.statistics.TaskPerformance;
import com.suixingpay.datas.manager.core.entity.MrJobTasksMonitor;
import com.suixingpay.datas.manager.core.icon.MrJobMonitor;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 任务泳道实时监控表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrJobTasksMonitorService {

    Integer insert(MrJobTasksMonitor mrJobTasksMonitor);

    Integer update(Long id, MrJobTasksMonitor mrJobTasksMonitor);

    Integer delete(Long id);

    MrJobTasksMonitor selectById(Long id);

    Page<MrJobTasksMonitor> page(Page<MrJobTasksMonitor> page);

    /** 解析任务每秒统计信息. */
    void dealTaskPerformance(TaskPerformance performance);

    /** 获取实时监控数据 . */
    MrJobMonitor obMrJobMonitor(String jobId, String swimlaneId, String schemaTable, Long intervalTime, Long intervalCount);

    void createTable(String mrJobTasksMonitorName, String newDate);

    void deleteByDate(String newDate);

    void dropTable(String mrJobTasksMonitorName);

}
