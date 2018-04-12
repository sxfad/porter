/**
 *
 */
package com.suixingpay.datas.manager.core.init;

import com.suixingpay.datas.manager.core.entity.CMenu;
import com.suixingpay.datas.manager.core.entity.CRole;
import com.suixingpay.datas.manager.core.util.ApplicationContextUtil;
import com.suixingpay.datas.manager.service.CMenuService;
import com.suixingpay.datas.manager.service.CRoleService;
import com.suixingpay.datas.manager.service.impl.CMenuServiceImpl;
import com.suixingpay.datas.manager.service.impl.CRoleServiceImpl;
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

    public static Map<String, CMenu> ROLE_MENU = new HashMap<>();

    public static Map<String, Map<String, Object>> ROLE_MENU_URL = new HashMap<>();

    public static MenuUtils instance;

    public static MenuUtils getInstance() {
        if (instance == null) {
            instance = new MenuUtils();
        }
        return instance;
    }

    public void init() {
        logger.info("MenuUtils init");
        loadRoleMenu();
    }

    private void loadRoleMenu() {
        CMenuService cMenuService = ApplicationContextUtil.getBean(CMenuServiceImpl.class);
        CRoleService cRoleService = ApplicationContextUtil.getBean(CRoleServiceImpl.class);
        //查询所有角色
        List<CRole> roles = cRoleService.findAll();
        for (CRole cRole : roles) {
            //根据不同角色生成菜单树
            CMenu cMenu = cMenuService.menuTree("-1",cRole.getRoleCode());
            ROLE_MENU.put(cRole.getRoleCode(), cMenu);
        }
    }
}
