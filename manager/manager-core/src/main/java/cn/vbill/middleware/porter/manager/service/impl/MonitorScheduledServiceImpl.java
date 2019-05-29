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

package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.mapper.MonitorScheduledMapper;
import cn.vbill.middleware.porter.manager.core.util.DateFormatUtils;
import cn.vbill.middleware.porter.manager.core.util.DateMathUtils;
import cn.vbill.middleware.porter.manager.service.MonitorScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(MonitorScheduledServiceImpl.class);

    @Autowired
    private MonitorScheduledMapper monitorScheduledMapper;

    /**
     * entryData.getKey():log_date entry.getKey():mr_log_monitor
     * entry.getValue()mr_log_monitor_20180425
     */
    @Override
    public void transferDataTask() {
        Long a = System.currentTimeMillis();
        try {
            // 获取当前时间 并计算出前天的时间
            Date date = DateMathUtils.dateAddDays(new Date(), -2);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String newDate = sdf.format(date);
            // 节点任务实时监控表
            String mrNodesMonitorName = "mr_nodes_monitor_" + newDate;
            // 日志信息表
            String mrLogMonitorName = "mr_log_monitor_" + newDate;
            // 任务泳道实时监控表
            String mrJobTasksMonitorName = "mr_job_tasks_monitor_" + newDate;

            Map<String, Map<String, String>> dataMap = new HashMap<>();
            // key:旧表表名 value:新表表名
            Map<String, String> monitorDateMap = new HashMap<>();
            monitorDateMap.put("mr_nodes_monitor", mrNodesMonitorName);
            monitorDateMap.put("mr_job_tasks_monitor", mrJobTasksMonitorName);
            Map<String, String> logDateMap = new HashMap<>();
            logDateMap.put("mr_log_monitor", mrLogMonitorName);
            // 不同的日期字段
            dataMap.put("log_date", logDateMap);
            dataMap.put("monitor_date", monitorDateMap);
            for (Map.Entry<String, Map<String, String>> entryData : dataMap.entrySet()) {
                for (Map.Entry<String, String> entry : dataMap.get(entryData.getKey()).entrySet()) {
                    // 判断时间、旧表表名、新表表名、前天日期
                    monitorScheduledMapper.transferData(entryData.getKey(), entry.getKey(), entry.getValue(), newDate);
                }
            }
        } finally {
            logger.info("转移删除前天以前的数据 总耗时：[{}]", (System.currentTimeMillis() - a));
        }
    }

    @Override
    public void dropTableTask() {
        Long a = System.currentTimeMillis();
        try {
            // 根据当前时间计算30天前的时间
            Date date = DateMathUtils.dateAddDays(new Date(), -30);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String newDate = sdf.format(date);
            // 节点任务实时监控表
            String mrNodesMonitorName = "mr_nodes_monitor_" + newDate;
            // 日志信息表
            String mrLogMonitorName = "mr_log_monitor_" + newDate;
            // 任务泳道实时监控表
            String mrJobTasksMonitorName = "mr_job_tasks_monitor_" + newDate;

            Map<String, String> nameMap = new HashMap<>();
            nameMap.put("mrNodesMonitorName", mrNodesMonitorName);
            nameMap.put("mrLogMonitorName", mrLogMonitorName);
            nameMap.put("mrJobTasksMonitorName", mrJobTasksMonitorName);
            for (Map.Entry<String, String> entry : nameMap.entrySet()) {
                monitorScheduledMapper.dropTable(entry.getValue());
            }
        } finally {
            logger.info("删除存在30天的表 总耗时：[{}]", (System.currentTimeMillis() - a));
        }
    }

    @Override
    public void createTableTask() {
        Long a = System.currentTimeMillis();
        try {
            // Map<String, Map<String, String>> key根据数据表保存，value中的map，key为根据昨天、明天、后天拼表名
            Map<String, Map<String, String>> map = new HashMap<>();
            // 组装数据
            map = conver("mr_nodes_monitor_", map);
            map = conver("mr_log_monitor_", map);
            map = conver("mr_job_tasks_monitor_", map);

            Map<String, String> dataMap = null;
            String oldTable = null;
            String tomorrowTable = null;
            String nowTable = null;
            for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
                dataMap = entry.getValue();
                oldTable = dataMap.get("old");
                tomorrowTable = dataMap.get("tomorrow");
                nowTable = dataMap.get("new");
                monitorScheduledMapper.createTable(nowTable, oldTable);
                String tomorrow = monitorScheduledMapper.checkTomorrowTable(tomorrowTable);
                if (tomorrow == null) {
                    logger.error("未查到[{}]表，开始重新新建...", tomorrowTable);
                    monitorScheduledMapper.createTomorrowTable(tomorrowTable, oldTable);
                }
            }
        } finally {
            logger.info("新建数据库表 总耗时：[{}]", (System.currentTimeMillis() - a));
        }
    }

    @Override
    public void initMonitorTable() {
        Date today = new Date();
        Date tomorrowDate = DateMathUtils.dateAddDays(today, +1);
        //判断今天和明天表有没有建立 如果没有就建立
        Map<String, Map<String, String>> dataMap = makeTableName(today, tomorrowDate);
        for (Map.Entry<String, String> entry : dataMap.get("now").entrySet()) {
            String nowTable = monitorScheduledMapper.checkTomorrowTable(entry.getValue());
            if (nowTable == null) {
                logger.info("未查到今日：[{}]表，开始初始化...", entry.getValue());
                monitorScheduledMapper.createTable(entry.getValue(), entry.getKey());
            }
        }
        for (Map.Entry<String, String> entry : dataMap.get("tomorrow").entrySet()) {
            String tomorrowTable = monitorScheduledMapper.checkTomorrowTable(entry.getValue());
            if (tomorrowTable == null) {
                logger.info("未查到明日：[{}]表，开始初始化...", entry.getValue());
                monitorScheduledMapper.createTable(entry.getValue(), entry.getKey());
            }
        }
    }

    /**
     * 拼出今天和明天的表名
     *
     * @param today
     * @param tomorrowDate
     * @return map
     */
    private Map<String, Map<String, String>> makeTableName(Date today, Date tomorrowDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowTime = sdf.format(today);
        String tomorrowTime = sdf.format(tomorrowDate);

        Map<String, Map<String, String>> dateMap =new HashMap<>();

        Map<String, String> nowTableName = new HashMap<>();
        nowTableName.put("mr_job_tasks_monitor", "mr_job_tasks_monitor_" + nowTime);
        nowTableName.put("mr_nodes_monitor", "mr_nodes_monitor_" + nowTime);
        nowTableName.put("mr_log_monitor", "mr_log_monitor_" + nowTime);
        dateMap.put("now", nowTableName);

        Map<String, String> tomorrowTableName = new HashMap<>();
        tomorrowTableName.put("mr_job_tasks_monitor", "mr_job_tasks_monitor_" + tomorrowTime);
        tomorrowTableName.put("mr_nodes_monitor", "mr_nodes_monitor_" + tomorrowTime);
        tomorrowTableName.put("mr_log_monitor", "mr_log_monitor_" + tomorrowTime);
        dateMap.put("tomorrow", tomorrowTableName);

        return dateMap;
    }

    /**
     * 拼出表名，放在map中
     *
     * @param table
     * @param map
     */
    private Map<String, Map<String, String>> conver(String table, Map<String, Map<String, String>> map) {
        Date date = new Date();
        // 明天、后天的日期
        Date tomorrowDate = DateMathUtils.dateAddDays(date, +1);
        Date newDate = DateMathUtils.dateAddDays(date, +2);

        // 去掉最后一个下划线，形成表名
        String oldTable = table.substring(0, table.length() - 1);
        String tomorrowTable = table + DateFormatUtils.formatDate("yyyyMMdd", tomorrowDate);
        String newTable = table + DateFormatUtils.formatDate("yyyyMMdd", newDate);
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("old", oldTable);
        dataMap.put("tomorrow", tomorrowTable);
        dataMap.put("new", newTable);
        map.put(table, dataMap);
        return map;
    }
}
