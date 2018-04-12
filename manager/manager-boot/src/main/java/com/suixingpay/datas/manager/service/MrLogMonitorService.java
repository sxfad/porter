package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.common.statistics.NodeLog;
import com.suixingpay.datas.manager.core.entity.MrLogMonitor;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 日志监控信息表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrLogMonitorService {

    Integer insert(MrLogMonitor mrLogMonitor);

    Integer update(Long id, MrLogMonitor mrLogMonitor);

    Integer delete(Long id);

    MrLogMonitor selectById(Long id);

    Page<MrLogMonitor> page(Page<MrLogMonitor> page, String ipAddress, Integer state, String beginTime, String endTime);

    /** 解析任务日志. */
    void dealNodeLog(NodeLog log);
}
