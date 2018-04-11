/**
 * 
 */
package com.suixingpay.datas.manager.core.init;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suixingpay.datas.manager.core.entity.CMenu;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
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

    }
}
