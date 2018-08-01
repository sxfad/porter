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

    Integer insert(MrJobTasksSchedule mrJobTasksSchedule);

    Integer update(Long id, MrJobTasksSchedule mrJobTasksSchedule);

    Integer delete(Long id);

    MrJobTasksSchedule selectById(Long id);

    Page<MrJobTasksSchedule> page(Page<MrJobTasksSchedule> page);

    /** 解析处理 任务进度状态汇总. */
    void dealDTaskStat(DTaskStat stat);

    List<MrJobTasksSchedule> selectSwimlaneByJobId(String jobId);

    List<MrJobTasksSchedule> list(String jobId, String heartBeatBeginDate, String heartBeatEndDate);
}
