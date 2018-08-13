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

package cn.vbill.middleware.porter.manager.core.init;

import cn.vbill.middleware.porter.manager.core.entity.CRole;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.service.CMenuService;
import cn.vbill.middleware.porter.manager.service.CRoleService;
import cn.vbill.middleware.porter.manager.service.impl.CMenuServiceImpl;
import cn.vbill.middleware.porter.manager.service.impl.CRoleServiceImpl;
import cn.vbill.middleware.porter.manager.core.entity.CMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class MenuUtils {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * ROLE_MENU
     */
    public static Map<String, CMenu> ROLE_MENU = new HashMap<>();

    /**
     * ROLE_MENU_URL
     */
    public static Map<String, Map<String, Object>> ROLE_MENU_URL = new HashMap<>();

    /**
     * INSTANCE
     */
    public static MenuUtils INSTANCE;

    /**
     * 初始化菜单
     * @return
     */
    public static MenuUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MenuUtils();
        }
        return INSTANCE;
    }

    /**
     * init
     *
     * @author FuZizheng
     * @date 2018/8/9 下午4:51
     * @param: []
     * @return: void
     */
    public void init() {
        logger.info("MenuUtils init");
        loadRoleMenu();
    }

    /**
     * 生成菜单树
     *
     * @author FuZizheng
     * @date 2018/8/9 下午4:51
     * @param: []
     * @return: void
     */
    private void loadRoleMenu() {
        CMenuService cMenuService = ApplicationContextUtil.getBean(CMenuServiceImpl.class);
        CRoleService cRoleService = ApplicationContextUtil.getBean(CRoleServiceImpl.class);
        // 查询所有角色
        List<CRole> roles = cRoleService.findAll();
        for (CRole cRole : roles) {
            // 根据不同角色生成菜单树
            CMenu cMenu = cMenuService.menuTree(cRole.getRoleCode());
            ROLE_MENU.put(cRole.getRoleCode(), cMenu);
        }
    }
}
