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

import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksOwner;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 任务所有权控制表 服务接口类
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
public interface JobTasksOwnerService {

    /**
     * 新增
     *
     * @param jobTasksOwner
     */
    Integer insert(JobTasksOwner jobTasksOwner);

    /**
     * 修改
     *
     * @param id,
     *            jobTasksOwner
     */
    Integer update(Long id, JobTasksOwner jobTasksOwner);

    /**
     * 删除
     *
     * @param id
     */
    Integer delete(Long id);

    /**
     * 查询详情
     *
     * @param id
     */
    JobTasksOwner selectById(Long id);

    /**
     * 分页查询
     *
     * @param page
     */
    Page<JobTasksOwner> page(Page<JobTasksOwner> page);

    /**
     * 新增任务同时更新任务所有权控制
     *
     * @param jobId
     */
    void insertByJobTasks(Long jobId);

    /**
     * 权限移交
     *
     * @param jobId
     * @param toUserId
     * @return
     */
    Integer changePermission(Long jobId, Long fromUserId, Long toUserId);

    /**
     * 权限共享
     *
     * @param jobId
     * @param toUserIds
     * @return
     */
    Integer sharePermission(Long jobId, List<CUser> toUserIds);
}