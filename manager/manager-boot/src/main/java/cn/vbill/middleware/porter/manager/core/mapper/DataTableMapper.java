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

import cn.vbill.middleware.porter.manager.core.entity.DataTable;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据表信息表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataTableMapper {

    /**
     * 验证新增
     *
     * @param dataTable
     * @return
     */
    Integer insertSelective(DataTable dataTable);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    DataTable selectById(Long id);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<DataTable> page(@Param("page") Page<DataTable> page, @Param("state") Integer state,
            @Param("bankName") String bankName, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state, @Param("bankName") String bankName,
            @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 目标/元数据表组分页All
     *
     * @param state
     * @return
     */
    Integer dataTableAll(@Param("state") Integer state);

    /**
     * 目标/元数据表组分页方法
     *
     * @param page
     * @param state
     * @return
     */
    List<DataTable> dataTablePage(@Param("page") Page<DataTable> page, @Param("state") Integer state);

    // *
    // * 新增
    // *
    // * @param dataTable
    //
    // Integer insert(DataTable dataTable);
    //
    // *
    // * 修改
    // *
    // * @param dataTable
    //
    // Integer update(@Param("id") Long id, @Param("dataTable") DataTable
    // dataTable);
    //

}