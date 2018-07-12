package cn.vbill.middleware.porter.manager.service;

import java.util.List;

import cn.vbill.middleware.porter.common.dic.TaskStatusType;
import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 同步任务表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface JobTasksService {

    Integer insert(JobTasks jobTasks);

    Integer insertCapture(JobTasks jobTasks);

    Integer update(JobTasks jobTasks);

    Integer delete(Long id);

    JobTasks selectById(Long id);

    JobTasks selectEntityById(Long id);

    Page<JobTasks> page(Page<JobTasks> page, String jobName, String beginTime, String endTime, TaskStatusType jobState,
            Integer jobType);

    Object tableNames(Long tablesId);

    List<String> fields(Long sourceId, Long tablesId, String tableAllName);

    Integer updateState(Long id, TaskStatusType taskStatusType);

    TaskConfig fitJobTask(Long id, TaskStatusType status);

    List<JobTasks> selectList();

    List<JobTasks> selectJobNameList();
}
