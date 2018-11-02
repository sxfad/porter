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

import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksUser;
import cn.vbill.middleware.porter.manager.web.page.Page;
import cn.vbill.middleware.porter.manager.core.mapper.JobTasksUserMapper;
import cn.vbill.middleware.porter.manager.service.JobTasksUserService;
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
