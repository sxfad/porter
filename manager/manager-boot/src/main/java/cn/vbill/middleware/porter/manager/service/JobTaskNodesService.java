/**
 *
 */
package cn.vbill.middleware.porter.manager.service;

import java.util.List;

import cn.vbill.middleware.porter.manager.core.entity.JobTaskNodes;
import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.web.page.Page;

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
