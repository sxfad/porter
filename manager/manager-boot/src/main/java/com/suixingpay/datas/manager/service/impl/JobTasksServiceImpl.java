/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.core.mapper.JobTasksMapper;
import com.suixingpay.datas.manager.service.JobTasksService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 同步任务表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class JobTasksServiceImpl implements JobTasksService {

    @Autowired
    private JobTasksMapper jobTasksMapper;

    @Override
    public Integer insert(JobTasks jobTasks) {
        return jobTasksMapper.insert(jobTasks);
    }

    @Override
    public Integer update(Long id, JobTasks jobTasks) {
        return jobTasksMapper.update(id, jobTasks);
    }

    @Override
    public Integer delete(Long id) {
        return jobTasksMapper.delete(id);
    }

    @Override
    public JobTasks selectById(Long id) {
        return jobTasksMapper.selectById(id);
    }

    @Override
    public Page<JobTasks> page(Page<JobTasks> page) {
        Integer total = jobTasksMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTasksMapper.page(page, 1));
        }
        return page;
    }

}
