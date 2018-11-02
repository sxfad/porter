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

import java.util.List;

import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.service.JobTaskNodesService;
import cn.vbill.middleware.porter.manager.core.entity.JobTaskNodes;
import cn.vbill.middleware.porter.manager.core.mapper.JobTaskNodesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 任务节点分发表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-04-16 14:12:10
 * @version: V1.0-auto
 * @review: FairyHood/2018-04-16 14:12:10
 */
@Service
public class JobTaskNodesServiceImpl implements JobTaskNodesService {

    @Autowired
    private JobTaskNodesMapper jobTaskNodesMapper;

    @Override
    public Integer insert(JobTaskNodes jobTaskNodes) {
        return jobTaskNodesMapper.insert(jobTaskNodes);
    }

    @Override
    public void insertList(JobTasks jobTasks) {
        if (jobTasks.getNodeIds() != null && jobTasks.getNodeIds().size() > 0) {
            jobTaskNodesMapper.insertList(jobTasks.getId(), jobTasks.getNodeIds());
        }
    }

    @Override
    public Integer update(Long id, JobTaskNodes jobTaskNodes) {
        return jobTaskNodesMapper.update(id, jobTaskNodes);
    }

    @Override
    public Integer delete(Long jobTaskId) {
        return jobTaskNodesMapper.delete(jobTaskId);
    }

    @Override
    public List<JobTaskNodes> selectById(Long jobTaskId) {
        return jobTaskNodesMapper.selectById(jobTaskId);
    }

    @Override
    public Page<JobTaskNodes> page(Page<JobTaskNodes> page) {
        Integer total = jobTaskNodesMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTaskNodesMapper.page(page, 1));
        }
        return page;
    }
}
