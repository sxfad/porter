package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.JobTasksField;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 任务数据字段对照关系表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface JobTasksFieldService {

    Integer insert(JobTasksField jobTasksField);

    Integer update(Long id, JobTasksField jobTasksField);

    Integer delete(Long id);

    JobTasksField selectById(Long id);

    Page<JobTasksField> page(Page<JobTasksField> page);

}
