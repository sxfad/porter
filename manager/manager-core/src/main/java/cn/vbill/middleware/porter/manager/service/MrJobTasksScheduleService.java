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

import cn.vbill.middleware.porter.manager.core.entity.MrJobTasksSchedule;
import cn.vbill.middleware.porter.common.cluster.data.DTaskStat;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import cn.vbill.middleware.porter.common.dic.TaskStatusType;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 任务泳道进度表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrJobTasksScheduleService {

    /**
     * 新增
     *
     * @date 2018/8/10 下午1:58
     * @param: [mrJobTasksSchedule]
     * @return: java.lang.Integer
     */
    Integer insert(MrJobTasksSchedule mrJobTasksSchedule);

    /**
     * 更新
     *
     * @date 2018/8/10 下午1:58
     * @param: [id,
     *             mrJobTasksSchedule]
     * @return: java.lang.Integer
     */
    Integer update(Long id, MrJobTasksSchedule mrJobTasksSchedule);

    /**
     * 删除
     *
     * @date 2018/8/10 下午1:59
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 下午1:59
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.MrJobTasksSchedule
     */
    MrJobTasksSchedule selectById(Long id);

    /**
     * 变更任务状态
     * 
     * @param id
     * @param taskStatusType
     * @return
     */
    Integer updateState(Long id, TaskStatusType taskStatusType);

    /**
     * 分页
     *
     * @date 2018/8/10 下午1:59
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.MrJobTasksSchedule>
     */
    Page<MrJobTasksSchedule> page(Page<MrJobTasksSchedule> page);

    /** 解析处理 任务进度状态汇总. */
    void dealDTaskStat(DTaskStat stat);

    /**
     * 接收任务
     * 
     * @param task
     * @param taskConfigJson
     */
    void dealJobJsonText(TaskConfig task, String taskConfigJson);

    /**
     * 根据JobId查询
     *
     * @date 2018/8/10 下午2:00
     * @param: [jobId]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.MrJobTasksSchedule>
     */
    List<MrJobTasksSchedule> selectSwimlaneByJobId(String jobId);

    /**
     * 列表
     *
     * @date 2018/8/10 下午2:01
     * @param: [jobId,
     *             heartBeatBeginDate, heartBeatEndDate]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.MrJobTasksSchedule>
     */
    List<MrJobTasksSchedule> list(String jobId, String heartBeatBeginDate, String heartBeatEndDate);

    /**
     * 列表
     *
     * @param jobId
     * @param heartBeatBeginDate
     * @param heartBeatEndDate
     * @return
     */
    List<MrJobTasksSchedule> listJobTasks(String jobId, String heartBeatBeginDate, String heartBeatEndDate);
}
