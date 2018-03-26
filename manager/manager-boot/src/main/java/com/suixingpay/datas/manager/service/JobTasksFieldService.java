package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.JobTasksField;
import com.suixingpay.datas.manager.web.page.Page;

public interface JobTasksFieldService {

    Integer insert(JobTasksField jobTasksField);

    Integer update(Long id, JobTasksField jobTasksField);

    Integer delete(Long id);

    JobTasksField selectById(Long id);

    Page<JobTasksField> page(Page<JobTasksField> page);

}
