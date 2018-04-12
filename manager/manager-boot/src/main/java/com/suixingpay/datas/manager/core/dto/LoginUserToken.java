/**
 *
 */
package com.suixingpay.datas.manager.core.dto;

import com.suixingpay.datas.manager.web.token.Token;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class LoginUserToken extends Token {

    private Long userId;
    private String loginName;
    private String passwd;
    private String roleCode;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
