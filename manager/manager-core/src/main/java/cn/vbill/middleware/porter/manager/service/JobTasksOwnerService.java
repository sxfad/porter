/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.JobTasksOwner;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 任务所有权控制表 服务接口类
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
public interface JobTasksOwnerService {

    /**
     * 新增
     *
     * @param jobTasksOwner
     */
    Integer insert(JobTasksOwner jobTasksOwner);

    /**
     * 修改
     *
     * @param id, jobTasksOwner
     */
    Integer update(Long id, JobTasksOwner jobTasksOwner);

    /**
     * 删除
     *
     * @param id
     */
    Integer delete(Long id);

    /**
     * 查询详情
     *
     * @param id
     */
    JobTasksOwner selectById(Long id);

    /**
     * 分页查询
     *
     * @param page
     */
    Page<JobTasksOwner> page(Page<JobTasksOwner> page);
}
