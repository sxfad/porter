/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.JobTasksField;
import com.suixingpay.datas.manager.core.mapper.JobTasksFieldMapper;
import com.suixingpay.datas.manager.service.JobTasksFieldService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务数据字段对照关系表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
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
    public Integer delete(Long id) {
        return jobTasksFieldMapper.delete(id);
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

}
