package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.core.entity.JobTasksField;
import com.suixingpay.datas.manager.core.entity.JobTasksTable;
import com.suixingpay.datas.manager.web.page.Page;

import java.util.List;

public interface JobTasksFieldService {

    Integer insert(JobTasksField jobTasksField);

    Integer update(Long id, JobTasksField jobTasksField);

    Integer delete(Long id);

    JobTasksField selectById(Long id);

    Page<JobTasksField> page(Page<JobTasksField> page);

    void insertList(JobTasks jobTasks);

    List<JobTasksField> selectInfo(Long id, Long jobTasksTableId);
}
