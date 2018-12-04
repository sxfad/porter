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

import cn.vbill.middleware.porter.manager.core.entity.CMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单目录表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface CMenuMapper {

    /**
     * 查询父节点
     *
     * @author FuZizheng
     * @date 2018/8/9 下午5:31
     * @param: [fatherCode]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.CMenu>
     */
    List<CMenu> findByFatherCode(String fatherCode);

    /**
     * 查询所有
     *
     * @author FuZizheng
     * @date 2018/8/9 下午5:39
     * @param: []
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.CMenu>
     */
    List<CMenu> findAll();

    /**
     * 根据角色code查询
     *
     * @author FuZizheng
     * @date 2018/8/9 下午5:39
     * @param: [roleCode]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.CMenu>
     */
    List<CMenu> findByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2018/8/9 下午5:40
     * @param: [cMenu]
     * @return: java.lang.Integer
     */
    Integer insert(CMenu cMenu);

    /**
     * 修改
     *
     * @author FuZizheng
     * @date 2018/8/9 下午5:40
     * @param: [id, cMenu]
     * @return: java.lang.Integer
     */
    Integer update(@Param("id") Long id, @Param("cMenu") CMenu cMenu);

    /**
     * 根据id查询
     *
     * @author FuZizheng
     * @date 2018/8/9 下午5:40
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.CMenu
     */
    CMenu findById(Long id);

    /**
     * 删除
     *
     * @author FuZizheng
     * @date 2018/8/9 下午5:40
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 获取所有的一级菜单和二级菜单
     *
     * @return
     */
    List<CMenu> getAll();
}