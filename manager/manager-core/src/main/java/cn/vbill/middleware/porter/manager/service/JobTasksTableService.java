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

import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksTable;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 任务数据表对照关系表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
public interface JobTasksTableService {

    /**
     * 新增
     *
     * @date 2018/8/10 上午11:49
     * @param: [jobTasksTable]
     * @return: java.lang.Integer
     */
    Integer insert(JobTasksTable jobTasksTable);

    /**
     * 更新
     *
     * @date 2018/8/10 上午11:50
     * @param: [id, jobTasksTable]
     * @return: java.lang.Integer
     */
    Integer update(Long id, JobTasksTable jobTasksTable);

    /**
     * 删除
     *
     * @date 2018/8/10 上午11:50
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 上午11:50
     * @param: [id]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.JobTasksTable>
     */
    List<JobTasksTable> selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 上午11:51
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.JobTasksTable>
     */
    Page<JobTasksTable> page(Page<JobTasksTable> page);

    /**
     * insertList
     *
     * @date 2018/8/10 上午11:51
     * @param: [jobTasks]
     * @return: void
     */
    void insertList(JobTasks jobTasks);
}