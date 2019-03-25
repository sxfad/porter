/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
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
