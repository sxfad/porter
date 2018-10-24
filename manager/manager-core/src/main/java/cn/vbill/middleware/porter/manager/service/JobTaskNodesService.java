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

    /**
     * 新增
     *
     * @date 2018/8/10 上午11:39
     * @param: [jobTaskNodes]
     * @return: java.lang.Integer
     */
    Integer insert(JobTaskNodes jobTaskNodes);

    /**
     * 新增列表
     *
     * @date 2018/8/10 上午11:40
     * @param: [jobTasks]
     * @return: void
     */
    void insertList(JobTasks jobTasks);

    /**
     * 更新
     *
     * @date 2018/8/10 上午11:40
     * @param: [id, jobTaskNodes]
     * @return: java.lang.Integer
     */
    Integer update(Long id, JobTaskNodes jobTaskNodes);

    /**
     * 删除
     *
     * @date 2018/8/10 上午11:40
     * @param: [jobTaskId]
     * @return: java.lang.Integer
     */
    Integer delete(Long jobTaskId);

    /**
     * 根据id查找
     *
     * @date 2018/8/10 上午11:40
     * @param: [jobTaskId]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.JobTaskNodes>
     */
    List<JobTaskNodes> selectById(Long jobTaskId);

    /**
     * 分页
     *
     * @date 2018/8/10 上午11:40
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.JobTaskNodes>
     */
    Page<JobTaskNodes> page(Page<JobTaskNodes> page);

}
