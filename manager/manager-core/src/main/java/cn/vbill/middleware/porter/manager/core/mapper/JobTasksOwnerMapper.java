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
package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksOwner;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务所有权控制表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
public interface JobTasksOwnerMapper {

    /**
     * 新增
     *
     * @param jobTasksOwner
     */
    Integer insert(JobTasksOwner jobTasksOwner);

    /**
     * 修改
     *
     * @param jobTasksOwner
     */
    Integer update(@Param("id") Long id, @Param("jobTasksOwner") JobTasksOwner jobTasksOwner);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    JobTasksOwner selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<JobTasksOwner> page(@Param("page") Page<JobTasksOwner> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 权限移交：移除当前所有者权限
     *
     * @param jobId
     * @param ownerId
     */
    Integer deleteByOwnerIdAndJobId(@Param("jobId") Long jobId, @Param("ownerId") Long ownerId);

    /**
     * 权限移交：权限移交给toUserId
     *
     * @param jobId
     * @param ownerId
     * @return
     */
    Integer changePermission(@Param("jobId") Long jobId, @Param("ownerId") Long ownerId);

    /**
     * 权限共享
     *
     * @param jobId
     * @param toUserIds
     * @return
     */
    Integer sharePermission(@Param("jobId") Long jobId, @Param("toUserIds") List<CUser> toUserIds);
}