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

import cn.vbill.middleware.porter.manager.core.entity.JobTasksOwner;
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
     * 根据任务id和类型查询ownerId
     *
     * @param jobId
     * @param type
     * @return
     */
    List<Long> selectOwnerIdByJobIdOrTypeOne(@Param("jobId") Long jobId, @Param("type") Integer type);

    /**
     * 根据任务id和用户id查询该用户type
     *
     * @param jobId
     * @param userId
     * @return
     */
    Integer findOwnerTypeByJobIdAndUserId(@Param("jobId") Long jobId, @Param("userId") Long userId);

    /**
     * 新增
     *
     * @param jobTasksOwner
     */
    Integer insert(JobTasksOwner jobTasksOwner);
}