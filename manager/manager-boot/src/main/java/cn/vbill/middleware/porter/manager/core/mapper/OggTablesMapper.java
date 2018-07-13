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

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.vbill.middleware.porter.manager.core.entity.OggTables;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * ogg表数据信息 Mapper接口
 * 
 * @author: FairyHood
 * @date: 2018-05-25 16:30:41
 * @version: V1.0-auto
 * @review: FairyHood/2018-05-25 16:30:41
 */
public interface OggTablesMapper {

    /**
     * 新增(插入非空字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer insert(OggTables oggTables);

    /**
     * 新增(插入全部字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer insertAllColumn(OggTables oggTables);

    /**
     * 修改(修改非空字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer update(@Param("id") Long id, @Param("oggTables") OggTables oggTables);

    /**
     * 修改(修改全部字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer updateAllColumn(@Param("id") Long id, @Param("oggTables") OggTables oggTables);

    /**
     * 根据主键查找实体
     * 
     * @param id
     * @return OggTables
     */
    OggTables selectById(Long id);

    /**
     * list数据
     * 
     * @return List
     */
    List<OggTables> selectList(@Param("ipAddress") String ipAddress, @Param("tableValue") String tableValue);

    /**
     * 分頁total
     * 
     * @param other
     * @return Integer
     */
    Integer pageAll(@Param("ipAddress") String ipAddress, @Param("tableValue") String tableValue);

    /**
     * 分頁
     * 
     * @param page
     * @param other
     * @return List
     */
    List<OggTables> page(@Param("page") Page<OggTables> page, @Param("ipAddress") String ipAddress,
            @Param("tableValue") String tableValue);

    /**
     * 刪除
     * 
     * @param id
     * @return Integer
     */
    Integer delete(Long id);

    String relatedTask(@Param("tableName") String tableName);
}
