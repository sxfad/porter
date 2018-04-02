/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.core.entity.JobTasksUser;
import com.suixingpay.datas.manager.core.mapper.JobTasksUserMapper;
import com.suixingpay.datas.manager.service.JobTasksUserService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * job_tasks_user 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class JobTasksUserServiceImpl implements JobTasksUserService {

    @Autowired
    private JobTasksUserMapper jobTasksUserMapper;

    @Override
    public Integer insert(JobTasksUser jobTasksUser) {
        return jobTasksUserMapper.insert(jobTasksUser);
    }

    @Override
    public Integer update(Long id, JobTasksUser jobTasksUser) {
        return jobTasksUserMapper.update(id, jobTasksUser);
    }

    @Override
    public Integer delete(Long jobTaskId) {
        return jobTasksUserMapper.delete(jobTaskId);
    }

    @Override
    public JobTasksUser selectById(Long id) {
        return jobTasksUserMapper.selectById(id);
    }

    @Override
    public Page<JobTasksUser> page(Page<JobTasksUser> page) {
        Integer total = jobTasksUserMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTasksUserMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public void insertList(JobTasks jobTasks) {
        jobTasksUserMapper.insertList(jobTasks.getId(), jobTasks.getUserIds());
    }

}
