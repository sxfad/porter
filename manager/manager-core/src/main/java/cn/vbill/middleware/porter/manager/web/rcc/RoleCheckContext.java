package cn.vbill.middleware.porter.manager.web.rcc;

import cn.vbill.middleware.porter.manager.core.dto.LoginUserToken;
import cn.vbill.middleware.porter.manager.core.dto.RoleDataControl;
import cn.vbill.middleware.porter.manager.web.token.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: murasakiseifu
 * @date: 2019-03-11 10:06}
 * @version: V1.0
 * @review: murasakiseifu/2019-03-11 10:06}
 */
public class RoleCheckContext {

    protected static Logger LOGGER = LoggerFactory.getLogger(RoleCheckContext.class);

    private static ThreadLocal<RoleDataControl> USERIDHOLDER = new ThreadLocal<>();

    /**
     * 判断登录用户角色当前
     *
     * @author FuZizheng
     * @date 2019-03-11 10:30
     * @param: [token]
     * @return: void
     */
    public static void checkUserRole(String token) {
        LoginUserToken loginUserToken = null;
        try {
            loginUserToken = TokenUtil.unsign(token, LoginUserToken.class);
            RoleDataControl roleDataControl = new RoleDataControl();
            roleDataControl.setUserId(loginUserToken.getUserId());
            roleDataControl.setRoleCode(loginUserToken.getRoleCode());
            USERIDHOLDER.set(roleDataControl);
        } catch (Exception e) {
            LOGGER.error("RoleCheckContext解析Token失败：[{}]", token);
        }
    }

    /**
     * 获取UserId
     *
     * @author FuZizheng
     * @date 2019-03-11 10:30
     * @param: []
     * @return: java.lang.Long
     */
    public static RoleDataControl getUserIdHolder() {
        if (USERIDHOLDER == null || USERIDHOLDER.get() == null) {
            return new RoleDataControl();
        }
        return USERIDHOLDER.get();
    }
}
