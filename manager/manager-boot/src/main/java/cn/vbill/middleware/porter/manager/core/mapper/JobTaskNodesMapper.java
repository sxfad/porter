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

import cn.vbill.middleware.porter.manager.core.entity.JobTaskNodes;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务节点分发表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-04-16 14:12:10
 * @version: V1.0-auto
 * @review: FairyHood/2018-04-16 14:12:10
 */
public interface JobTaskNodesMapper {

    /**
     * 新增
     *
     * @param jobTaskNodes
     */
    Integer insert(JobTaskNodes jobTaskNodes);

    /**
     * 修改
     *
     * @param jobTaskNodes
     */
    Integer update(@Param("id") Long id, @Param("jobTaskNodes") JobTaskNodes jobTaskNodes);

    /**
     * 刪除
     *
     * @param jobTaskId
     * @return
     */
    Integer delete(Long jobTaskId);

    /**
     * 根據jobtaskid查找數據
     *
     * @param nodeIds
     * @return
     */
    List<JobTaskNodes> selectById(Long nodeIds);

    /**
     * 分頁
     *
     * @return
     */
    List<JobTaskNodes> page(@Param("page") Page<JobTaskNodes> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 保存
     *
     * @param jobTaskId
     * @param nodeIds
     */
    void insertList(@Param("jobTaskId") Long jobTaskId, @Param("nodeIds") List<String> nodeIds);
}