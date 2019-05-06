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
import cn.vbill.middleware.porter.manager.core.entity.MrNodesMonitor;
import cn.vbill.middleware.porter.manager.core.icon.MrNodeMonitor;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 节点任务实时监控表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrNodesMonitorService {

    /**
     * 新增
     *
     * @date 2018/8/10 下午2:04
     * @param: [mrNodesMonitor]
     * @return: java.lang.Integer
     */
    Integer insert(MrNodesMonitor mrNodesMonitor);

    /**
     * 更新
     *
     * @date 2018/8/10 下午2:05
     * @param: [id, mrNodesMonitor]
     * @return: java.lang.Integer
     */
    Integer update(Long id, MrNodesMonitor mrNodesMonitor);

    /**
     * 删除
     *
     * @date 2018/8/10 下午2:05
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 下午2:05
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.event.MrNodesMonitor
     */
    MrNodesMonitor selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 下午2:05
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.event.MrNodesMonitor>
     */
    Page<MrNodesMonitor> page(Page<MrNodesMonitor> page);

    /**
     * 节点区间数据统计.
     */
    void dealTaskPerformance(DTaskPerformance performance);

    /**
     * obNodeMonitor
     *
     * @date 2018/8/10 下午2:05
     * @param: [nodeId, intervalTime, intervalCount]
     * @return: cn.vbill.middleware.porter.manager.core.icon.MrNodeMonitor
     */
    MrNodeMonitor obNodeMonitor(String nodeId, Long intervalTime, Long intervalCount);

    /**
     * 查询节点数据，新增查询过去某天的节点数据
     *
     * @param nodeId
     * @param date
     * @param intervalTime
     * @param intervalCount
     * @return
     */
    MrNodeMonitor obNodeMonitorDetail(String nodeId, String date, Long intervalTime, Long intervalCount);

}
