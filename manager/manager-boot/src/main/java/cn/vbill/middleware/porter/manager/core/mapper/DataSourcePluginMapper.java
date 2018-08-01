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

import cn.vbill.middleware.porter.manager.core.entity.DataSourcePlugin;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据源信息关联表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
public interface DataSourcePluginMapper {

    /**
     * 新增
     *
     * @param dataSourcePlugin
     */
    Integer insert(DataSourcePlugin dataSourcePlugin);

    /**
     * 修改
     *
     * @param dataSourcePlugin
     */
    Integer update(@Param("id") Long id, @Param("dataSourcePlugin") DataSourcePlugin dataSourcePlugin);

    /**
     * 刪除
     *
     * @param sourceId
     * @return
     */
    Integer delete(Long sourceId);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    DataSourcePlugin selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<DataSourcePlugin> page(@Param("page") Page<DataSourcePlugin> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 批量新增数据源关联信息
     *
     * @param plugins
     */
    void insertDataSourcePlugins(List<DataSourcePlugin> plugins);

    /**
     * 根据数据源id查找关联信息
     *
     * @param sourceId
     * @return
     */
    List<DataSourcePlugin> findListBySourceID(Long sourceId);

}