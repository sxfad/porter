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

import cn.vbill.middleware.porter.manager.core.entity.JobTasksTable;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务数据表对照关系表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
public interface JobTasksTableMapper {

    /**
     * 新增
     *
     * @param jobTasksTable
     */
    Integer insert(JobTasksTable jobTasksTable);

    /**
     * 修改
     *
     * @param jobTasksTable
     */
    Integer update(@Param("id") Long id, @Param("jobTasksTable") JobTasksTable jobTasksTable);

    /**
     * 刪除
     *
     * @param jobTaskId
     * @return
     */
    Integer delete(Long jobTaskId);

    /**
     * 根據主鍵id查找數據
     *
     * @param jobTaskId
     * @return
     */
    List<JobTasksTable> selectById(Long jobTaskId);

    /**
     * 分頁
     *
     * @return
     */
    List<JobTasksTable> page(@Param("page") Page<JobTasksTable> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 批量新增 JobTasksTable
     *
     * @param tables
     */
    void insertList(List<JobTasksTable> tables);

}