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
import cn.vbill.middleware.porter.manager.core.mapper.JobTasksTableMapper;
import cn.vbill.middleware.porter.manager.service.JobTasksFieldService;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksTable;
import cn.vbill.middleware.porter.manager.service.JobTasksTableService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 任务数据表对照关系表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
@Service
public class JobTasksTableServiceImpl implements JobTasksTableService {

    @Autowired
    private JobTasksTableMapper jobTasksTableMapper;

    @Autowired
    private JobTasksFieldService jobTasksFieldService;


    @Override
    public Integer insert(JobTasksTable jobTasksTable) {
        return jobTasksTableMapper.insert(jobTasksTable);
    }

    @Override
    public Integer update(Long id, JobTasksTable jobTasksTable) {
        return jobTasksTableMapper.update(id, jobTasksTable);
    }

    @Override
    public Integer delete(Long jobTaskId) {
        return jobTasksTableMapper.delete(jobTaskId);
    }

    @Override
    public List<JobTasksTable> selectById(Long id) {

        List<JobTasksField> fields;
        List<JobTasksTable> tables = jobTasksTableMapper.selectById(id);

        for (JobTasksTable jobTasksTable : tables) {
            //根据 jobTasksId 和 jobTasksTableId 查询详情
            fields = jobTasksFieldService.selectInfo(id, jobTasksTable.getId());
            jobTasksTable.setFields(fields);
        }

        return tables;
    }

    @Override
    public Page<JobTasksTable> page(Page<JobTasksTable> page) {
        Integer total = jobTasksTableMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTasksTableMapper.page(page, 1));
        }
        return page;
    }

    @Override
    @Transactional
    public void insertList(JobTasks jobTasks) {
        //获取 JobTasks 自增id
        for (JobTasksTable jobTasksTable : jobTasks.getTables()) {
            jobTasksTable.setJobTaskId(jobTasks.getId());
        }
        jobTasksTableMapper.insertList(jobTasks.getTables());
        //新增 JobTasksField
        jobTasksFieldService.insertList(jobTasks);
    }
}
