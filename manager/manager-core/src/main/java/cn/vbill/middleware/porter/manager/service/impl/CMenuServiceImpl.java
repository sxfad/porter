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

package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.entity.CMenu;
import cn.vbill.middleware.porter.manager.core.mapper.CMenuMapper;
import cn.vbill.middleware.porter.manager.service.CMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单目录表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class CMenuServiceImpl implements CMenuService {

    @Autowired
    private CMenuMapper cmenuMapper;

    @Override
    public CMenu menuTree(String roleCode) {
        List<CMenu> menulist = cmenuMapper.findByRoleCode(roleCode);
        return parentMenu(menulist);
    }

    @Override
    public CMenu findAll() {
        List<CMenu> menulist = cmenuMapper.findAll();
        return parentMenu(menulist);
    }

    @Override
    public Integer insert(CMenu cMenu) {
        return cmenuMapper.insert(cMenu);
    }

    @Override
    public List<CMenu> findByFatherCode(String fatherCode) {
        return cmenuMapper.findByFatherCode(fatherCode);
    }

    @Override
    public Integer update(Long id, CMenu cMenu) {
        return cmenuMapper.update(id, cMenu);
    }

    @Override
    public CMenu findById(Long id) {
        return cmenuMapper.findById(id);
    }

    @Override
    public Integer delete(Long id) {
        return cmenuMapper.delete(id);
    }

    /**
     * 菜单树
     *
     * @author FuZizheng
     * @date 2018/8/10 下午2:15
     * @param: [menulist]
     * @return: cn.vbill.middleware.porter.manager.core.entity.CMenu
     */
    private CMenu parentMenu(List<CMenu> menulist) {

        CMenu parentMenu = new CMenu(0);
        parentMenu.setMenus(menus("-1", listtomap(menulist)));

        return parentMenu;
    }

    /**
     * 组装map
     *
     * @author FuZizheng
     * @date 2018/8/10 下午2:15
     * @param: [menulist]
     * @return: java.util.Map<java.lang.String,java.util.List<cn.vbill.middleware.porter.manager.core.entity.CMenu>>
     */
    private Map<String, List<CMenu>> listtomap(List<CMenu> menulist) {
        Map<String, List<CMenu>> menuMap = new HashMap<>();

        for (CMenu cMenu : menulist) {
            List<CMenu> menus = menuMap.get(cMenu.getFathercode());
            if (menus == null) {
                menus = new ArrayList<>();
            }
            menus.add(cMenu);
            menuMap.put(cMenu.getFathercode(), menus);
        }

        return menuMap;
    }

    /**
     * 递归方法
     *
     * @author FuZizheng
     * @date 2018/8/10 下午2:16
     * @param: [fatherCode, menuMap]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.CMenu>
     */
    private List<CMenu> menus(String fatherCode, Map<String, List<CMenu>> menuMap) {
        List<CMenu> childlist = menuMap.get(fatherCode);
        if (childlist == null || childlist.isEmpty()) {
            return null;
        }
        //根据父类id查询旗下子类
        for (CMenu cMenu : childlist) {
            if (cMenu == null || cMenu.getIsleaf() == null || cMenu.getIsleaf() == 1) {
                continue;
            }
            cMenu.setMenus(menus(cMenu.getCode(), menuMap));
        }
        return childlist;
    }

}
