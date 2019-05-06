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

import cn.vbill.middleware.porter.common.task.statistics.DTaskPerformance;
import cn.vbill.middleware.porter.manager.core.entity.MrJobTasksMonitor;
import cn.vbill.middleware.porter.manager.core.icon.MrJobMonitor;
import cn.vbill.middleware.porter.manager.core.mapper.MrJobTasksMonitorMapper;
import cn.vbill.middleware.porter.manager.core.util.DateFormatUtils;
import cn.vbill.middleware.porter.manager.web.page.Page;
import cn.vbill.middleware.porter.manager.service.MrJobTasksMonitorService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务泳道实时监控表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class MrJobTasksMonitorServiceImpl implements MrJobTasksMonitorService {

    @Autowired
    private MrJobTasksMonitorMapper mrJobTasksMonitorMapper;

    @Override
    public Integer insert(MrJobTasksMonitor mrJobTasksMonitor) {
        return mrJobTasksMonitorMapper.insert(mrJobTasksMonitor, null);
    }

    @Override
    public Integer update(Long id, MrJobTasksMonitor mrJobTasksMonitor) {
        return mrJobTasksMonitorMapper.update(id, mrJobTasksMonitor);
    }

    @Override
    public Integer delete(Long id) {
        return mrJobTasksMonitorMapper.delete(id);
    }

    @Override
    public MrJobTasksMonitor selectById(Long id) {
        return mrJobTasksMonitorMapper.selectById(id);
    }

    @Override
    public Page<MrJobTasksMonitor> page(Page<MrJobTasksMonitor> page) {
        Integer total = mrJobTasksMonitorMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(mrJobTasksMonitorMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public void dealTaskPerformance(DTaskPerformance performance) {
        MrJobTasksMonitor mrJobTasksMonitor = new MrJobTasksMonitor(performance);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = simpleDateFormat.format(new Date());
        // 拼出表名
        StringBuffer monitorTable = new StringBuffer("mr_job_tasks_monitor_");
        monitorTable.append(date);
        mrJobTasksMonitorMapper.insert(mrJobTasksMonitor, monitorTable.toString());
    }

    @Override
    public MrJobMonitor obMrJobMonitor(String jobId, String swimlaneId, String schemaTable, Long intervalTime, Long intervalCount) {
        Long startRow = intervalTime * intervalCount;
        List<MrJobTasksMonitor> list =  mrJobTasksMonitorMapper.selectByJobSwimlane(jobId, swimlaneId, schemaTable, startRow, intervalTime);
        return new MrJobMonitor(list);
    }

    @Override
    public MrJobMonitor obMrJobMonitorDetail(String jobId, String swimlaneId, String schemaTable, String date, Long intervalTime, Long intervalCount) {
        String newDate = DateFormatUtils.formatDate("yyyy-MM-dd", new Date());
        // 如果是当前日期则显示最近的时间，否则显示一天的数据
        if (!newDate.equals(date)) {
            intervalTime = 1440L;
        }
        Long startRow = intervalTime * intervalCount;
        // 拼出表名
        String monitorTable = convert(date);
        List<MrJobTasksMonitor> list = mrJobTasksMonitorMapper.selectByJobSwimlaneDetail(jobId, swimlaneId, schemaTable, date, startRow, intervalTime, monitorTable);
        return new MrJobMonitor(list);
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
        StringBuffer monitorTable = new StringBuffer("mr_job_tasks_monitor_");
        monitorTable.append(tableDate);
        return monitorTable.toString();
    }

}
