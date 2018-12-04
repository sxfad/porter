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

package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.CMenu;

import java.util.List;

/**
 * 菜单目录表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface CMenuService {

    /**
     * 生成菜单树
     *
     * @date 2018/8/10 上午10:17
     * @param: [roleCode]
     * @return: cn.vbill.middleware.porter.manager.core.entity.CMenu
     */
    CMenu menuTree(String roleCode);

    /**
     * 查询全部
     *
     * @date 2018/8/10 上午10:28
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.core.entity.CMenu
     */
    CMenu findAll();

    /**
     * 新增
     *
     * @date 2018/8/10 上午10:28
     * @param: [cMenu]
     * @return: java.lang.Integer
     */
    Integer insert(CMenu cMenu);

    /**
     * 根据父节点查询
     *
     * @date 2018/8/10 上午10:28
     * @param: [fatherCode]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.CMenu>
     */
    List<CMenu> findByFatherCode(String fatherCode);

    /**
     * 更新
     *
     * @date 2018/8/10 上午10:33
     * @param: [id, cMenu]
     * @return: java.lang.Integer
     */
    Integer update(Long id, CMenu cMenu);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 上午10:33
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.CMenu
     */
    CMenu findById(Long id);

    /**
     * 删除
     *
     * @date 2018/8/10 上午10:34
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 获取全部的一级菜单和二级菜单
     *
     * @return
     */
    List<CMenu> getAll();
}