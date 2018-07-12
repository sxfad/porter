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
