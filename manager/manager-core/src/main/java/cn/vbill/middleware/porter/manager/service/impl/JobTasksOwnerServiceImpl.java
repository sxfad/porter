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

import cn.vbill.middleware.porter.manager.core.entity.JobTasksOwner;
import cn.vbill.middleware.porter.manager.core.mapper.JobTasksOwnerMapper;
import cn.vbill.middleware.porter.manager.service.JobTasksOwnerService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务所有权控制表 服务实现类
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
@Service
public class JobTasksOwnerServiceImpl implements JobTasksOwnerService {

    @Autowired
    private JobTasksOwnerMapper jobTasksOwnerMapper;

    @Override
    public Integer insert(JobTasksOwner jobTasksOwner) {
        return jobTasksOwnerMapper.insert(jobTasksOwner);
    }

    @Override
    public Integer update(Long id, JobTasksOwner jobTasksOwner) {
        return jobTasksOwnerMapper.update(id, jobTasksOwner);
    }

    @Override
    public Integer delete(Long id) {
        return jobTasksOwnerMapper.delete(id);
    }

    @Override
    public JobTasksOwner selectById(Long id) {
        return jobTasksOwnerMapper.selectById(id);
    }

    @Override
    public Page<JobTasksOwner> page(Page<JobTasksOwner> page) {
        Integer total = jobTasksOwnerMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTasksOwnerMapper.page(page, 1));
        }
        return page;
    }

}
