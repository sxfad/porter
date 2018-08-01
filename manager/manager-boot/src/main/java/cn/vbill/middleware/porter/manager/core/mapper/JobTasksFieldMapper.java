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

import cn.vbill.middleware.porter.manager.core.entity.JobTasksField;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务数据字段对照关系表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
public interface JobTasksFieldMapper {

    /**
     * 新增
     *
     * @param jobTasksField
     */
    Integer insert(JobTasksField jobTasksField);

    /**
     * 修改
     *
     * @param jobTasksField
     */
    Integer update(@Param("id") Long id, @Param("jobTasksField") JobTasksField jobTasksField);

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
     * @param id
     * @return
     */
    JobTasksField selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<JobTasksField> page(@Param("page") Page<JobTasksField> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 批量新增
     *
     * @param fields
     */
    void insertList(List<JobTasksField> fields);

    /**
     * 根据jobTasksId和jobTasksTableId查询详情
     *
     * @param jobTaskId
     * @param jobTasksTableId
     * @return
     */
    List<JobTasksField> selectInfo(@Param("jobTaskId") Long jobTaskId, @Param("jobTasksTableId") Long jobTasksTableId);
}