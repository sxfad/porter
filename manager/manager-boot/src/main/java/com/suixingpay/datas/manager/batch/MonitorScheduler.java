package com.suixingpay.datas.manager.batch;

import com.suixingpay.datas.manager.core.util.DateMathUtils;
import com.suixingpay.datas.manager.service.MrJobTasksMonitorService;
import com.suixingpay.datas.manager.service.MrLogMonitorService;
import com.suixingpay.datas.manager.service.MrNodesMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author fuzizheng[fu_zz@suixingpay.com]
 */
@Component
public class MonitorScheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MrJobTasksMonitorService mrJobTasksMonitorService;

    @Autowired
    private MrLogMonitorService mrLogMonitorService;

    @Autowired
    private MrNodesMonitorService mrNodesMonitorService;

    //每天凌晨2点启动定时器，删除前天以前的数据
    @Scheduled(cron = "0 0 2 * * ? ")
    @Transactional
    public void demo() {
        logger.info("启动定时器，转移删除前天以前的数据");
        //获取当前时间 并计算出前天的时间
        Date date = DateMathUtils.dateAddDays(new Date(), -2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String newDate = sdf.format(date);
        String nowDate = sdf.format(DateMathUtils.getDate());
        //根据前天时间 转移数据到新表 准备：新表表名 -- 表_yyyyMMdd
        //节点任务实时监控表
        String mrNodesMonitorName = "mr_nodes_monitor_" + nowDate;
        mrNodesMonitorService.createTable(mrNodesMonitorName, newDate);
        //日志信息表
        String mrLogMonitorName = "mr_log_monitor_" + nowDate;
        mrLogMonitorService.createTable(mrLogMonitorName, newDate);
        //任务泳道实时监控表
        String mrJobTasksMonitorName = "mr_job_tasks_monitor_" + nowDate;
        mrJobTasksMonitorService.createTable(mrJobTasksMonitorName, newDate);

        //在旧表删除转移的数据
        mrJobTasksMonitorService.deleteByDate(newDate);
        mrLogMonitorService.deleteByDate(newDate);
        mrNodesMonitorService.deleteByDate(newDate);

    }

    //每天凌晨3点启动定时器，删除存在30天以前的表
    @Scheduled(cron = "0 0 3 * * ? ")
    public void secondDemo() {
        logger.info("启动定时器，删除存在30天的表");
        Date date = DateMathUtils.dateAddDays(new Date(), -30);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String newDate = sdf.format(date);
        //获取30天前的时间 删除表名称为 "表_yyyyMMdd"
        //节点任务实时监控表
        String mrNodesMonitorName = "mr_nodes_monitor_" + newDate;
        mrNodesMonitorService.dropTable(mrNodesMonitorName);
        //日志信息表
        String mrLogMonitorName = "mr_log_monitor_" + newDate;
        mrLogMonitorService.dropTable(mrLogMonitorName);
        //任务泳道实时监控表
        String mrJobTasksMonitorName = "mr_job_tasks_monitor_" + newDate;
        mrJobTasksMonitorService.dropTable(mrJobTasksMonitorName);

    }
}