/**
 *
 */
package com.suixingpay.datas.manager.service;

import java.util.List;

import com.suixingpay.datas.manager.core.entity.JobTaskNodes;
import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 任务节点分发表 服务接口类
 * @author: FairyHood
 * @date: 2018-04-16 14:12:10
 * @version: V1.0-auto
 * @review: FairyHood/2018-04-16 14:12:10
 */
public interface JobTaskNodesService {

    Integer insert(JobTaskNodes jobTaskNodes);

    void insertList(JobTasks jobTasks);

    Integer update(Long id, JobTaskNodes jobTaskNodes);

    Integer delete(Long jobTaskId);

    List<JobTaskNodes> selectById(Long jobTaskId);

    Page<JobTaskNodes> page(Page<JobTaskNodes> page);

}
