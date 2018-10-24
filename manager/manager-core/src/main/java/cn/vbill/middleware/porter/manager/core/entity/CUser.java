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

package cn.vbill.middleware.porter.manager.core.entity;

/**
 * 登陆用户表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class CUser implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 登陆账户.
     */
    private String loginname;

    /**
     * 登陆密码.
     */
    private String loginpw;

    /**
     * 昵称.
     */
    private String nickname;

    /**
     * 邮箱.
     */
    private String email;

    /**
     * 手机号.
     */
    private String mobile;

    /**
     * 部门名称.
     */
    private String departMent;

    /**
     * 角色id.
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private CRole cRole;

    /**
     * 状态 1正常，0禁止登陆，-1删除.
     */
    private Integer state;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 主键 get方法.
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键 set方法.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 登陆账户 get方法.
     */
    public String getLoginname() {
        return loginname == null ? null : loginname.trim();
    }

    /**
     * 登陆账户 set方法.
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname == null ? null : loginname.trim();
    }

    /**
     * 登陆密码 get方法.
     */
    public String getLoginpw() {
        return loginpw == null ? null : loginpw.trim();
    }

    /**
     * 登陆密码 set方法.
     */
    public void setLoginpw(String loginpw) {
        this.loginpw = loginpw == null ? null : loginpw.trim();
    }

    /**
     * 昵称 get方法.
     */
    public String getNickname() {
        return nickname == null ? null : nickname.trim();
    }

    /**
     * 昵称 set方法.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * 邮箱 get方法.
     */
    public String getEmail() {
        return email == null ? null : email.trim();
    }

    /**
     * 邮箱 set方法.
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 手机号 get方法.
     */
    public String getMobile() {
        return mobile == null ? null : mobile.trim();
    }

    /**
     * 手机号 set方法.
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 部门名称 get方法.
     */
    public String getDepartMent() {
        return departMent == null ? null : departMent.trim();
    }

    /**
     * 部门名称 set方法.
     */
    public void setDepartMent(String departMent) {
        this.departMent = departMent == null ? null : departMent.trim();
    }

    /**
     * 角色id get方法.
     */
    public String getRoleCode() {
        return roleCode == null ? null : roleCode.trim();
    }

    /**
     * 角色id set方法.
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    /**
     * 状态 1正常，0禁止登陆，-1删除 get方法.
     */
    public Integer getState() {
        return state;
    }

    /**
     * 状态 1正常，0禁止登陆，-1删除 set方法.
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 备注 get方法.
     */
    public String getRemark() {
        return remark == null ? null : remark.trim();
    }

    /**
     * 备注 set方法.
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 角色名称 get方法
     *
     * @return
     */
    public CRole getcRole() {
        return cRole;
    }

    /**
     * 角色名称 set方法
     *
     * @param cRole
     */
    public void setcRole(CRole cRole) {
        this.cRole = cRole;
    }
}
