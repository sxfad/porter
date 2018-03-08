package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

/**
 * 告警配置表 实体Entity
 * 
 * @author: FairyHood
 * @date: 2018-03-08 10:44:50
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:44:50
 */
public class Alarm implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键. */
    private Long id;

    /** 发件人. */
    private String addResser;

    /** 邮件服务器. */
    private String emailServer;

    /** 邮箱账户. */
    private String emailAccount;

    /** 邮箱密码. */
    private String emailPasswd;

    /** 创建人. */
    private Long createUserId;

    /** 修改人. */
    private Long updateUserId;

    /** 创建时间. */
    private Date createTime;

    /** 修改时间. */
    private Date updateTime;

    /** 状态. */
    private Integer state;

    /** 是否作废. */
    private Integer iscancel;

    /** 备注. */
    private String remark;

    /** 主键 get方法. */
    public Long getId() {
        return id;
    }

    /** 主键 set方法. */
    public void setId(Long id) {
        this.id = id;
    }

    /** 发件人 get方法. */
    public String getAddResser() {
        return addResser == null ? null : addResser.trim();
    }

    /** 发件人 set方法. */
    public void setAddResser(String addResser) {
        this.addResser = addResser == null ? null : addResser.trim();
    }

    /** 邮件服务器 get方法. */
    public String getEmailServer() {
        return emailServer == null ? null : emailServer.trim();
    }

    /** 邮件服务器 set方法. */
    public void setEmailServer(String emailServer) {
        this.emailServer = emailServer == null ? null : emailServer.trim();
    }

    /** 邮箱账户 get方法. */
    public String getEmailAccount() {
        return emailAccount == null ? null : emailAccount.trim();
    }

    /** 邮箱账户 set方法. */
    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount == null ? null : emailAccount.trim();
    }

    /** 邮箱密码 get方法. */
    public String getEmailPasswd() {
        return emailPasswd == null ? null : emailPasswd.trim();
    }

    /** 邮箱密码 set方法. */
    public void setEmailPasswd(String emailPasswd) {
        this.emailPasswd = emailPasswd == null ? null : emailPasswd.trim();
    }

    /** 创建人 get方法. */
    public Long getCreateUserId() {
        return createUserId;
    }

    /** 创建人 set方法. */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /** 修改人 get方法. */
    public Long getUpdateUserId() {
        return updateUserId;
    }

    /** 修改人 set方法. */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /** 创建时间 get方法. */
    public Date getCreateTime() {
        return createTime;
    }

    /** 创建时间 set方法. */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /** 修改时间 get方法. */
    public Date getUpdateTime() {
        return updateTime;
    }

    /** 修改时间 set方法. */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /** 状态 get方法. */
    public Integer getState() {
        return state;
    }

    /** 状态 set方法. */
    public void setState(Integer state) {
        this.state = state;
    }

    /** 是否作废 get方法. */
    public Integer getIscancel() {
        return iscancel;
    }

    /** 是否作废 set方法. */
    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
    }

    /** 备注 get方法. */
    public String getRemark() {
        return remark == null ? null : remark.trim();
    }

    /** 备注 set方法. */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

}
