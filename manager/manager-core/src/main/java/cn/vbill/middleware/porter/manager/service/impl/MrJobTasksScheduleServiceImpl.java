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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vbill.middleware.porter.common.cluster.data.DTaskStat;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import cn.vbill.middleware.porter.common.dic.TaskStatusType;
import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.core.entity.MrJobTasksSchedule;
import cn.vbill.middleware.porter.manager.core.init.ResourceUtils;
import cn.vbill.middleware.porter.manager.core.mapper.MrJobTasksScheduleMapper;
import cn.vbill.middleware.porter.manager.service.JobTasksService;
import cn.vbill.middleware.porter.manager.service.MrJobTasksScheduleService;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 任务泳道进度表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class MrJobTasksScheduleServiceImpl implements MrJobTasksScheduleService {

    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(128);

    @Autowired
    private MrJobTasksScheduleMapper mrJobTasksScheduleMapper;

    @Autowired
    private JobTasksService jobTasksService;

    @Override
    public Integer insert(MrJobTasksSchedule mrJobTasksSchedule) {
        return mrJobTasksScheduleMapper.insert(mrJobTasksSchedule);
    }

    @Override
    public Integer update(Long id, MrJobTasksSchedule mrJobTasksSchedule) {
        return mrJobTasksScheduleMapper.update(id, mrJobTasksSchedule);
    }

    @Override
    public Integer delete(Long id) {
        return mrJobTasksScheduleMapper.delete(id);
    }

    @Override
    public MrJobTasksSchedule selectById(Long id) {
        return mrJobTasksScheduleMapper.selectById(id);
    }

    @Override
    public Integer updateState(Long id, TaskStatusType taskStatusType) {
        return jobTasksService.updateState(id, taskStatusType);
    }

    @Override
    public Page<MrJobTasksSchedule> page(Page<MrJobTasksSchedule> page) {
        Integer total = mrJobTasksScheduleMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(mrJobTasksScheduleMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public void dealDTaskStat(DTaskStat stat) {
        MrJobTasksSchedule mrJobTasksSchedule = new MrJobTasksSchedule(stat);
        String jobId = mrJobTasksSchedule.getJobId();
        String swimlaneId = mrJobTasksSchedule.getSwimlaneId();
        String schemaTable = mrJobTasksSchedule.getSchemaTable();
        String key = jobId + swimlaneId + schemaTable;
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
                dealDTaskStatSync(jobId, swimlaneId, schemaTable, mrJobTasksSchedule);
            }
        } finally {
            map.remove(key);
        }
    }

    @Override
    public void dealJobJsonText(TaskConfig task, String taskConfigJson) {
        String jobId = task.getTaskId();
        String key = jobId;
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
                dealJobJsonTextSysn(task, taskConfigJson);
            }
        } finally {
            map.remove(key);
        }
    }

    @Override
    public List<MrJobTasksSchedule> selectSwimlaneByJobId(String jobId) {
        return mrJobTasksScheduleMapper.selectSwimlaneByJobId(jobId);
    }

    @Override
    public List<MrJobTasksSchedule> list(String jobId, String heartBeatBeginDate, String heartBeatEndDate) {
        return mrJobTasksScheduleMapper.list(jobId, heartBeatBeginDate, heartBeatEndDate);
    }

    @Override
    public List<MrJobTasksSchedule> listJobTasks(String jobId, String heartBeatBeginDate, String heartBeatEndDate) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = simpleDateFormat.format(new Date());
        // 拼出表名
        StringBuffer stringBuffer = new StringBuffer("mr_job_tasks_monitor_");
        stringBuffer.append(date);
        return mrJobTasksScheduleMapper.listJobTasks(jobId, heartBeatBeginDate, heartBeatEndDate, stringBuffer.toString());
    }

    /**
     * dealDTaskStatSync
     *
     * @date 2018/8/10 下午2:18
     * @param: [jobId,
     *             swimlaneId, schemaTable, mrJobTasksSchedule]
     * @return: void
     */
    private void dealDTaskStatSync(String jobId, String swimlaneId, String schemaTable,
            MrJobTasksSchedule mrJobTasksSchedule) {
        MrJobTasksSchedule old = mrJobTasksScheduleMapper.selectByJobIdAndSwimlaneId(jobId, swimlaneId, schemaTable);
        if (old == null || old.getId() == null) {
            mrJobTasksScheduleMapper.insert(mrJobTasksSchedule);
        } else {
            mrJobTasksSchedule.setId(old.getId());
            mrJobTasksScheduleMapper.updateSelective(old.getId(), mrJobTasksSchedule);
        }

        // 考虑
        Boolean key = ResourceUtils.existJob(jobId);
        if (!key) {
            jobTasksService.insertCapture(new JobTasks(jobId));
        }
    }

    private void dealJobJsonTextSysn(TaskConfig task, String taskConfigJson) {
        if (task.isLocalTask()) {
            JobTasks jobTasks = new JobTasks(task, taskConfigJson);
            JobTasks old = jobTasksService.selectByIdOne(jobTasks.getId());
            if (old == null || old.getId() == null) {
                jobTasksService.insertZKCapture(jobTasks, TaskStatusType.WORKING);
            } else {
                jobTasks.setId(old.getId());
                jobTasksService.updateZKCapture(jobTasks, TaskStatusType.WORKING);
            }
        }
    }
}