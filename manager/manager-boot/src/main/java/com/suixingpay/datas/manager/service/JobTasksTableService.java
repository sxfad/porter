package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.core.entity.JobTasksTable;
import com.suixingpay.datas.manager.web.page.Page;

import java.util.List;

/**
 * 任务数据表对照关系表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
public interface JobTasksTableService {

    Integer insert(JobTasksTable jobTasksTable);

    Integer update(Long id, JobTasksTable jobTasksTable);

    Integer delete(Long id);

    List<JobTasksTable> selectById(Long id);

    Page<JobTasksTable> page(Page<JobTasksTable> page);

    void insertList(JobTasks jobTasks);
}
