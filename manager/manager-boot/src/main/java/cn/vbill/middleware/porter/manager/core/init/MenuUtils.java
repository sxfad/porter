/**
 *
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

    public static Map<String, CMenu> ROLE_MENU = new HashMap<>();

    public static Map<String, Map<String, Object>> ROLE_MENU_URL = new HashMap<>();

    public static MenuUtils INSTANCE;

    public static MenuUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MenuUtils();
        }
        return INSTANCE;
    }

    public void init() {
        logger.info("MenuUtils init");
        loadRoleMenu();
    }

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
