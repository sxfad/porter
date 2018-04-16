/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.JobTaskNodes;
import com.suixingpay.datas.manager.core.mapper.JobTaskNodesMapper;
import com.suixingpay.datas.manager.service.JobTaskNodesService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Integer update(Long id, JobTaskNodes jobTaskNodes) {
        return jobTaskNodesMapper.update(id, jobTaskNodes);
    }

    @Override
    public Integer delete(Long id) {
        return jobTaskNodesMapper.delete(id);
    }

    @Override
    public JobTaskNodes selectById(Long id) {
        return jobTaskNodesMapper.selectById(id);
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
