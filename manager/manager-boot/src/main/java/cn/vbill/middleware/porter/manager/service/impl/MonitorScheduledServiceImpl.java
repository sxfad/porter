package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.mapper.MonitorScheduledMapper;
import cn.vbill.middleware.porter.manager.core.util.DateMathUtils;
import cn.vbill.middleware.porter.manager.service.MonitorScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 付紫钲
 * @date: 2018/4/25
 */
@Service
public class MonitorScheduledServiceImpl implements MonitorScheduledService {

    @Autowired
    private MonitorScheduledMapper monitorScheduledMapper;

    /**
     * entryData.getKey():log_date
     * entry.getKey():mr_log_monitor
     * entry.getValue()mr_log_monitor_20180425
     */
    @Override
    public void transferDataTask() {
        //获取当前时间 并计算出前天的时间
        Date date = DateMathUtils.dateAddDays(new Date(), -2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String newDate = sdf.format(date);
        //节点任务实时监控表
        String mrNodesMonitorName = "mr_nodes_monitor_" + newDate;
        //日志信息表
        String mrLogMonitorName = "mr_log_monitor_" + newDate;
        //任务泳道实时监控表
        String mrJobTasksMonitorName = "mr_job_tasks_monitor_" + newDate;

        Map<String, Map<String, String>> dataMap = new HashMap<>();
        //key:旧表表名 value:新表表名
        Map<String, String> monitorDateMap = new HashMap<>();
        monitorDateMap.put("mr_nodes_monitor", mrNodesMonitorName);
        monitorDateMap.put("mr_job_tasks_monitor", mrJobTasksMonitorName);
        Map<String, String> logDateMap = new HashMap<>();
        logDateMap.put("mr_log_monitor", mrLogMonitorName);
        //不同的日期字段
        dataMap.put("log_date", logDateMap);
        dataMap.put("monitor_date", monitorDateMap);
        for (Map.Entry<String, Map<String, String>> entryData : dataMap.entrySet()) {
            for (Map.Entry<String, String> entry : dataMap.get(entryData.getKey()).entrySet()) {
                //判断时间、旧表表名、新表表名、前天日期
                monitorScheduledMapper.transferData(entryData.getKey(), entry.getKey(), entry.getValue(), newDate);
            }
        }
    }

    @Override
    public void dropTableTask() {
        //根据当前时间计算30天前的时间
        Date date = DateMathUtils.dateAddDays(new Date(), -30);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String newDate = sdf.format(date);
        //节点任务实时监控表
        String mrNodesMonitorName = "mr_nodes_monitor_" + newDate;
        //日志信息表
        String mrLogMonitorName = "mr_log_monitor_" + newDate;
        //任务泳道实时监控表
        String mrJobTasksMonitorName = "mr_job_tasks_monitor_" + newDate;

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("mrNodesMonitorName", mrNodesMonitorName);
        nameMap.put("mrLogMonitorName", mrLogMonitorName);
        nameMap.put("mrJobTasksMonitorName", mrJobTasksMonitorName);
        for (Map.Entry<String, String> entry : nameMap.entrySet()) {
            monitorScheduledMapper.dropTable(entry.getValue());
        }
    }
}
