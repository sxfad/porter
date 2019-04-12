package cn.vbill.middleware.porter.manager.core.dto;

import java.io.Serializable;

/**
 * 登录用户vo
 *
 * @author: murasakiseifu
 * @date: 2019-03-11 10:38}
 * @version: V1.0
 * @review: murasakiseifu/2019-03-11 10:38
 */
public class RoleDataControl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录用户id
     */
    private Long userId = -1L;

    /**
     * 登录用户角色
     */
    private String roleCode = "-1";

    /**
     * 登录用户id.get
     * 
     * @return
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 登录用户id.set
     * 
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 登录用户角色.get
     * 
     * @return
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * 登录用户角色.set
     * 
     * @param roleCode
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
