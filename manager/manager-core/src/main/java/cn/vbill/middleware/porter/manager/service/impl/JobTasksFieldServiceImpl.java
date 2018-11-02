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
import cn.vbill.middleware.porter.manager.core.entity.JobTasksField;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksTable;
import cn.vbill.middleware.porter.manager.core.mapper.JobTasksFieldMapper;
import cn.vbill.middleware.porter.manager.service.JobTasksFieldService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务数据字段对照关系表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
@Service
public class JobTasksFieldServiceImpl implements JobTasksFieldService {

    @Autowired
    private JobTasksFieldMapper jobTasksFieldMapper;

    @Override
    public Integer insert(JobTasksField jobTasksField) {
        return jobTasksFieldMapper.insert(jobTasksField);
    }

    @Override
    public Integer update(Long id, JobTasksField jobTasksField) {
        return jobTasksFieldMapper.update(id, jobTasksField);
    }

    @Override
    public Integer delete(Long jobTaskId) {
        return jobTasksFieldMapper.delete(jobTaskId);
    }

    @Override
    public JobTasksField selectById(Long id) {
        return jobTasksFieldMapper.selectById(id);
    }

    @Override
    public Page<JobTasksField> page(Page<JobTasksField> page) {
        Integer total = jobTasksFieldMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTasksFieldMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public void insertList(JobTasks jobTasks) {

        for (JobTasksTable jobTasksTable : jobTasks.getTables()) {
            if (jobTasksTable.getFields() == null || jobTasksTable.getFields().size() == 0) {
                continue;
            }
            for (JobTasksField jobTasksField : jobTasksTable.getFields()) {
                jobTasksField.setJobTaskId(jobTasks.getId());
                jobTasksField.setJobTasksTableId(jobTasksTable.getId());
            }
            if (!jobTasksTable.isDirectMapTable()) {
                jobTasksFieldMapper.insertList(jobTasksTable.getFields());
            }
        }
    }

    @Override
    public List<JobTasksField> selectInfo(Long jobTaskId, Long jobTasksTableId) {
        return jobTasksFieldMapper.selectInfo(jobTaskId, jobTasksTableId);
    }
}
