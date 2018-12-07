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
     * @author he_xin
     * @return
     * @param state
     */
    List<CMenu> getAll(@Param("state") Integer state);

    /**
     * 新增菜单
     *
     * @author he_xin
     * @param cMenu
     * @return
     */
    Integer addMenu(CMenu cMenu);

    /**
     * 新增菜单时用于检查编号是否重复
     *
     * @author he_xin
     * @param code
     * @return
     */
    Integer checkCode(@Param("code") String code);

    /**
     * 获取当前code编号的子菜单
     *
     * @author he_xin
     * @param code
     * @return
     */
    List<String> getCode(@Param("code") String code);

    /**
     * 一级菜单连带二级菜单的启用或者停用
     *
     * @author he_xin
     * @param codeList
     * @param state
     * @return
     */
    Integer updateState(@Param("codeList") List<String> codeList, @Param("state") Integer state);

    /**
     * 二级菜单的启用或者停用
     *
     * @author he_xin
     * @param code
     * @param state
     * @return
     */
    Integer updateSingleState(@Param("code") String code, @Param("state") Integer state);
    /**
     * 修改菜单的信息
     *
     * @author he_xin
     * @param cMenu
     * @return
     */
    Integer updateMenu(CMenu cMenu);

    /**
     * 一级菜单连带二级菜单的逻辑删除菜单信息
     *
     * @author he_xin
     * @param codeList
     * @return
     */
    Integer deleteMenu(@Param("codeList") List<String> codeList);

    /**
     * 二级菜单的逻辑删除菜单信息
     *
     * @author he_xin
     * @param code
     * @return
     */
    Integer deleteSingleMenu(@Param("code") String code);
}