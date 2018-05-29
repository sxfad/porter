
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.core.entity.JobTasksField;
import com.suixingpay.datas.manager.core.entity.JobTasksTable;
import com.suixingpay.datas.manager.core.mapper.JobTasksFieldMapper;
import com.suixingpay.datas.manager.service.JobTasksFieldService;
import com.suixingpay.datas.manager.web.page.Page;
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
            for (JobTasksField jobTasksField : jobTasksTable.getFields()) {
                jobTasksField.setJobTaskId(jobTasks.getId());
                jobTasksField.setJobTasksTableId(jobTasksTable.getId());
            }
            if(!jobTasksTable.isDirectMapTable()) {
                jobTasksFieldMapper.insertList(jobTasksTable.getFields());
            }
        }
    }

    @Override
    public List<JobTasksField> selectInfo(Long jobTaskId, Long jobTasksTableId) {
        return jobTasksFieldMapper.selectInfo(jobTaskId, jobTasksTableId);
    }
}
