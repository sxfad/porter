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

import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.manager.core.entity.MrNodesSchedule;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 节点任务监控表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrNodesScheduleService {

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2018/8/10 下午2:06
     * @param: [mrNodesSchedule]
     * @return: java.lang.Integer
     */
    Integer insert(MrNodesSchedule mrNodesSchedule);

    /**
     * 更新
     *
     * @author FuZizheng
     * @date 2018/8/10 下午2:06
     * @param: [id, mrNodesSchedule]
     * @return: java.lang.Integer
     */
    Integer update(Long id, MrNodesSchedule mrNodesSchedule);

    /**
     * 删除
     *
     * @author FuZizheng
     * @date 2018/8/10 下午2:06
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查询
     *
     * @author FuZizheng
     * @date 2018/8/10 下午2:06
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.MrNodesSchedule
     */
    MrNodesSchedule selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 下午2:06
     * @param: [page, ipAddress, computerName]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.MrNodesSchedule>
     */
    Page<MrNodesSchedule> page(Page<MrNodesSchedule> page, String ipAddress, String computerName);

    /** 节点监听. */
    void dealDNode(DNode node);
}
