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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.vbill.middleware.porter.manager.core.entity.MrNodesMonitor;
import cn.vbill.middleware.porter.manager.core.icon.MrNodeMonitor;
import cn.vbill.middleware.porter.manager.core.mapper.MrNodesMonitorMapper;
import cn.vbill.middleware.porter.manager.service.MrNodesMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vbill.middleware.porter.common.task.statistics.DTaskPerformance;
import cn.vbill.middleware.porter.manager.core.util.DateFormatUtils;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 节点任务实时监控表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class MrNodesMonitorServiceImpl implements MrNodesMonitorService {

    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(128);

    @Autowired
    private MrNodesMonitorMapper mrNodesMonitorMapper;

    @Override
    public Integer insert(MrNodesMonitor mrNodesMonitor) {
        return mrNodesMonitorMapper.insert(mrNodesMonitor, null);
    }

    @Override
    public Integer update(Long id, MrNodesMonitor mrNodesMonitor) {
        return mrNodesMonitorMapper.update(id, mrNodesMonitor, null);
    }

    @Override
    public Integer delete(Long id) {
        return mrNodesMonitorMapper.delete(id);
    }

    @Override
    public MrNodesMonitor selectById(Long id) {
        return mrNodesMonitorMapper.selectById(id);
    }

    @Override
    public Page<MrNodesMonitor> page(Page<MrNodesMonitor> page) {
        Integer total = mrNodesMonitorMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(mrNodesMonitorMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public void dealTaskPerformance(DTaskPerformance performance) {
        MrNodesMonitor mrNodesMonitor = new MrNodesMonitor(performance);
        String nodeId = mrNodesMonitor.getNodeId();
        String dataTimes = DateFormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", mrNodesMonitor.getMonitorDate());
        String key = nodeId + dataTimes;
        Object lock = map.get(key);
        if (null == lock) {
            Object tmp = new Object();
            Object old = map.putIfAbsent(key, tmp);
            if (null != old) {
                lock = old;
            } else {
                lock = tmp;
            }
        }
        try {
            synchronized (lock) {
                dealTaskPerformanceSync(nodeId, dataTimes, mrNodesMonitor);
            }
        } finally {
            map.remove(key);
        }
    }

    @Override
    public MrNodeMonitor obNodeMonitor(String nodeId, Long intervalTime, Long intervalCount) {
        Long startRow = intervalTime * intervalCount;
        List<MrNodesMonitor> list = mrNodesMonitorMapper.selectByNodeId(nodeId, startRow, intervalTime);
        return new MrNodeMonitor(list);
    }

    @Override
    public MrNodeMonitor obNodeMonitorDetail(String nodeId, String date, Long intervalTime, Long intervalCount) {
        String newDate = DateFormatUtils.formatDate("yyyy-MM-dd", new Date());
        // 如果是当前日期则显示最近的时间，否则显示一天的数据
        if (!newDate.equals(date)) {
            intervalTime = 1440L;
        }
        Long startRow = intervalTime * intervalCount;
        // 拼出表名
        String monitorTable = convert(date);
        List<MrNodesMonitor> mrNodesMonitors = mrNodesMonitorMapper.selectByNodeIdDetail(nodeId, startRow, intervalTime, date, monitorTable);
        return new MrNodeMonitor(mrNodesMonitors);
    }

    /**
     * 把yyyy-MM-dd格式的日期转化为yyyyMMdd格式
     *
     * @param date
     * @return
     */
    private String convert(String date) {
        String tableDate = null;
        try {
            Date nowDate = DateFormatUtils.pareDate("yyyy-MM-dd", date);
            tableDate = DateFormatUtils.formatDate("yyyyMMdd", nowDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        StringBuffer monitorTable = new StringBuffer("mr_nodes_monitor_");
        monitorTable.append(tableDate);
        return monitorTable.toString();
    }

    /**
     * dealTaskPerformanceSync
     * @param nodeId
     * @param dataTimes
     * @param mrNodesMonitor
     */
    private void dealTaskPerformanceSync(String nodeId, String dataTimes, MrNodesMonitor mrNodesMonitor) {
        MrNodesMonitor old = mrNodesMonitorMapper.selectByNodeIdAndTime(nodeId, dataTimes);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = simpleDateFormat.format(new Date());
        // 拼出表名
        StringBuffer monitorTable = new StringBuffer("mr_nodes_monitor_");
        monitorTable.append(date);
        if (old == null || old.getId() == null) {
            mrNodesMonitorMapper.insert(mrNodesMonitor, monitorTable.toString());
        } else {
            mrNodesMonitor.setId(old.getId());
            mrNodesMonitor.setMonitorTps(mrNodesMonitor.getMonitorTps() + old.getMonitorTps());
            mrNodesMonitor.setMonitorAlarm(mrNodesMonitor.getMonitorAlarm() + old.getMonitorAlarm());
            mrNodesMonitorMapper.update(old.getId(), mrNodesMonitor, monitorTable.toString());
        }
    }
}
