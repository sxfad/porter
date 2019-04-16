/*
吗 * Copyright ©2018 vbill.cn.
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
import cn.vbill.middleware.porter.manager.core.entity.DataAuthority;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据权限控制表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2019-03-28 15:21:58
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-28 15:21:58
 */
public interface DataAuthorityMapper {

    /**
     * 新增
     *
     * @param dataAuthority
     */
    Integer insert(DataAuthority dataAuthority);

    /**
     * 新增（非空字段）
     * 
     * @param dataAuthority
     * @return
     */
    Integer insertSelective(DataAuthority dataAuthority);

    /**
     * 修改
     *
     * @param dataAuthority
     */
    Integer update(@Param("id") Long id, @Param("dataAuthority") DataAuthority dataAuthority);

    /**
     * 修改（非空字段）
     * 
     * @param id
     * @param dataAuthority
     * @return
     */
    Integer updateSelective(@Param("id") Long id, @Param("dataAuthority") DataAuthority dataAuthority);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 组合条件删除
     * 
     * @param objectTable
     * @param objectId
     * @param userId
     * @param type
     * @return
     */
    Integer deleteByMores(@Param("objectTable") String objectTable, @Param("objectId") Long objectId,
            @Param("userId") Long userId, @Param("type") Integer type);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    DataAuthority selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<DataAuthority> page(@Param("page") Page<DataAuthority> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 查询与ObjectId有关的权限所有者用户(type=null查询全部 type=1查询所有者 type=2查询共享者)
     * 
     * @param ObjectName
     * @param ObjectId
     * @param type
     * @return
     */
    List<CUser> selectOwnersByObjectId(@Param("objectName") String objectName, @Param("objectId") Long objectId,
            @Param("type") Integer type);

    /**
     * 根据多个条件查询唯一数据
     * 
     * @param ObjectName
     * @param ObjectId
     * @param type
     * @param ownerId
     * @return
     */
    DataAuthority selectOneByConditions(@Param("objectName") String objectName, @Param("objectId") Long objectId,
            @Param("ownerLevel") Integer ownerLevel, @Param("ownerId") Long ownerId);
}