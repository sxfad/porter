package com.suixingpay.datas.manager.service;

import java.util.List;

import com.suixingpay.datas.common.config.TaskConfig;
import com.suixingpay.datas.common.dic.TaskStatusType;
import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 同步任务表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface JobTasksService {

    Integer insert(JobTasks jobTasks);

    Integer update(Long id, JobTasks jobTasks);

    Integer delete(Long id);

    JobTasks selectById(Long id);

    Page<JobTasks> page(Page<JobTasks> page, String jobName, String beginTime, String endTime);

    Object tableNames(Long tablesId);

    List<String> fields(Long sourceId, Long tablesId, String tableAllName);

    Integer updateState(Long id, TaskStatusType taskStatusType);

    TaskConfig fitJobTask(Long id,TaskStatusType status);
}
