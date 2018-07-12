package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksField;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 任务数据字段对照关系表 服务接口类
 * @author: FairyHood
 * @date: 2018-04-16 14:12:10
 * @version: V1.0-auto
 * @review: FairyHood/2018-04-16 14:12:10
 */
public interface JobTasksFieldService {

    Integer insert(JobTasksField jobTasksField);

    Integer update(Long id, JobTasksField jobTasksField);

    Integer delete(Long jobTaskId);

    JobTasksField selectById(Long id);

    Page<JobTasksField> page(Page<JobTasksField> page);

    void insertList(JobTasks jobTasks);

    List<JobTasksField> selectInfo(Long id, Long jobTasksTableId);
}
