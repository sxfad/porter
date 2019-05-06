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

import cn.vbill.middleware.porter.common.task.statistics.DTaskPerformance;
import cn.vbill.middleware.porter.manager.core.entity.MrJobTasksMonitor;
import cn.vbill.middleware.porter.manager.core.icon.MrJobMonitor;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 任务泳道实时监控表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrJobTasksMonitorService {

    /**
     * 新增
     *
     * @date 2018/8/10 下午1:49
     * @param: [mrJobTasksMonitor]
     * @return: java.lang.Integer
     */
    Integer insert(MrJobTasksMonitor mrJobTasksMonitor);

    /**
     * 修改
     *
     * @date 2018/8/10 下午1:49
     * @param: [id, mrJobTasksMonitor]
     * @return: java.lang.Integer
     */
    Integer update(Long id, MrJobTasksMonitor mrJobTasksMonitor);

    /**
     * 修改
     *
     * @date 2018/8/10 下午1:49
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据Id查询
     *
     * @date 2018/8/10 下午1:49
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.event.MrJobTasksMonitor
     */
    MrJobTasksMonitor selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 下午1:49
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.event.MrJobTasksMonitor>
     */
    Page<MrJobTasksMonitor> page(Page<MrJobTasksMonitor> page);

    /** 解析任务每秒统计信息. */
    void dealTaskPerformance(DTaskPerformance performance);

    /** 获取实时监控数据 . */
    MrJobMonitor obMrJobMonitor(String jobId, String swimlaneId, String schemaTable, Long intervalTime, Long intervalCount);

    /**
     * 获取实时监控数据(增加查看过去某一天的数据)
     *
     * @param jobId
     * @param swimlaneId
     * @param schemaTable
     * @param date
     * @param intervalTime
     * @param intervalCount
     * @return
     */
    MrJobMonitor obMrJobMonitorDetail(String jobId, String swimlaneId, String schemaTable, String date, Long intervalTime, Long intervalCount);
}
