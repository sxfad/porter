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

import cn.vbill.middleware.porter.common.statistics.NodeLog;
import cn.vbill.middleware.porter.manager.core.entity.MrLogMonitor;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 日志监控信息表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrLogMonitorService {

    /**
     * 新增
     *
     * @date 2018/8/10 下午2:02
     * @param: [mrLogMonitor]
     * @return: java.lang.Integer
     */
    Integer insert(MrLogMonitor mrLogMonitor);

    /**
     * 更新
     *
     * @date 2018/8/10 下午2:02
     * @param: [id, mrLogMonitor]
     * @return: java.lang.Integer
     */
    Integer update(Long id, MrLogMonitor mrLogMonitor);

    /**
     * 删除
     *
     * @date 2018/8/10 下午2:02
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 下午2:02
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.MrLogMonitor
     */
    MrLogMonitor selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 下午2:04
     * @param: [page, ipAddress, state, beginTime, endTime]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.MrLogMonitor>
     */
    Page<MrLogMonitor> page(Page<MrLogMonitor> page, String ipAddress, Integer state, String date);

    /** 解析任务日志. */
    void dealNodeLog(NodeLog log);

}
