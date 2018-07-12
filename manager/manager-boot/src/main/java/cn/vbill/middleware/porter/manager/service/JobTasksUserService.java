package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksUser;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * job_tasks_user 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface JobTasksUserService {

    Integer insert(JobTasksUser jobTasksUser);

    Integer update(Long id, JobTasksUser jobTasksUser);

    Integer delete(Long id);

    JobTasksUser selectById(Long id);

    Page<JobTasksUser> page(Page<JobTasksUser> page);

    void insertList(JobTasks jobTasks);
}
