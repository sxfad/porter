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
import jdk.nashorn.internal.ir.ReturnNode;
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

    @Override
    public List<CMenu> getAll(Integer state) {
        List<CMenu> menuList = cmenuMapper.getAll(state);
        return listMenu(menuList);
    }

    @Override
    public Integer addMenu(CMenu cMenu) {
        Integer num = cmenuMapper.checkCode(cMenu.getCode());
        // 检查code编码是否重复
        if(num != null) {
            return -1;
        }
        return cmenuMapper.addMenu(cMenu);
    }

    @Override
    public Integer updateState(String code, Integer state) {
        // 拿到当前菜单的所有子菜单
        List<String> codeList = cmenuMapper.getCode(code);
        // 如果集合为空则认为此菜单为子菜单
        if(codeList == null || codeList.size() == 0){
            cmenuMapper.updateSingleState(code, state);
        }
        // 把当前菜单放入到list中
        codeList.add(code);
        // 修改状态
        return cmenuMapper.updateState(codeList, state);
    }

    @Override
    public Integer updateMenu(CMenu cMenu) {
        return cmenuMapper.updateMenu(cMenu);
    }

    @Override
    public Integer deleteMenu(String code) {
        // 拿到当前菜单的所有子菜单
        List<String> codeList = cmenuMapper.getCode(code);
        // 如果集合为空则认为此菜单为子菜单
        if (codeList == null || codeList.size() == 0) {
            cmenuMapper.deleteSingleMenu(code);
        }
        // 把当前菜单放入到list中
        codeList.add(code);
        // 逻辑删除菜单信息
        return cmenuMapper.deleteMenu(codeList);
    }

    /**
     * 查询出所有的一级二级菜单
     *
     * @param menuList
     * @return
     */
    private List<CMenu> listMenu(List<CMenu> menuList) {
        // 保存所有的父菜单
        List<CMenu> parentMenu = new ArrayList<CMenu>();
        for (CMenu cMenu : menuList) {
            // 一级菜单的编号的-1
            if ("-1".equals(cMenu.getFathercode())) {
                parentMenu.add(cMenu);
            }
        }

        for (CMenu cMenu : parentMenu) {
            List<CMenu> childMenu = getChildMenu(cMenu.getCode(), menuList);
            cMenu.setMenus(childMenu);
        }
        return parentMenu;
    }

    /**
     * 查询出一级菜单下的所有的子菜单
     *
     * @param fathercode
     * @param menuList
     * @return
     */
    private List<CMenu> getChildMenu(String fathercode, List<CMenu> menuList) {
        // 保存所有的二级菜单
        List<CMenu> childMenu = new ArrayList<CMenu>();
        for (CMenu cMenu : menuList) {
            // 如果菜单的父菜单编号和这个菜单编号相同则认为这个菜单为二级菜单
            if (cMenu.getFathercode().equals(fathercode)) {
                childMenu.add(cMenu);
            }
        }
        // 递归得到此一级菜单下所有的子菜单
        for (CMenu cMenu : childMenu) {
            cMenu.setMenus(getChildMenu(cMenu.getCode(), menuList));
        }
        // 如果子菜单集合为空，则菜单下没有子菜单了
        if (childMenu.size() == 0) {
            return new ArrayList<CMenu>();
        }
        return childMenu;
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
