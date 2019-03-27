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

import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公共数据源配置表 Mapper接口
 * 
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
public interface PublicDataSourceMapper {

    /**
     * 新增
     * 
     * @param publicDataSource
     * @return Integer
     */
    Integer insert(PublicDataSource publicDataSource);

    /**
     * 新增(非空字段)
     * 
     * @param publicDataSource
     * @return Integer
     */
    Integer insertSelective(PublicDataSource publicDataSource);

    /**
     * 修改
     * 
     * @param publicDataSource
     * @return Integer
     */
    Integer update(@Param("id") Long id, @Param("publicDataSource") PublicDataSource publicDataSource);

    /**
     * 修改(修改非空字段)
     * 
     * @param publicDataSource
     * @return Integer
     */
    Integer updateSelective(@Param("id") Long id, @Param("publicDataSource") PublicDataSource publicDataSource);

    /**
     * 根据主键查找实体
     * 
     * @param id
     * @return LoginUser
     */
    PublicDataSource selectById(Long id);

    /**
     * 分頁total
     * 
     * @param state
     * @return Integer
     */
    Integer pageAll(@Param("id") Long id, @Param("code") String code, @Param("name") String name,
            @Param("ipsite") String ipsite);

    /**
     * 分頁
     * 
     * @param page
     * @param state
     * @return List
     */
    List<PublicDataSource> page(@Param("page") Page<PublicDataSource> page, @Param("id") Long id,
            @Param("code") String code, @Param("name") String name, @Param("ipsite") String ipsite);

    /**
     * 作废cancel
     * 
     * @param id
     * @return
     */
    Integer updateCancel(@Param("id") Long id);

    /**
     * 修改推送状态
     * 
     * @param id
     * @return
     */
    Integer updatePush(@Param("id") Long id, @Param("ispush") Integer ispush);

    /**
     * 刪除
     * 
     * @param id
     * @return Integer
     */
    Integer delete(Long id);

}