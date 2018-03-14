package com.suixingpay.datas.manager.core.entity;

import com.suixingpay.datas.common.dic.SourceType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数据源信息表 实体Entity
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class DataSource implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键. */
    private Long id;

    /** 数据源名称. */
    @NotNull(message = "数据源名称不能为空")
    private String name;

    /** 数据源类型. */
    private SourceType dataType;

    /** 数据源组合json. */
    private String dataJson;

    /** 用户名. */
    private String dateUser;

    /** 密码. */
    private String datePasswd;

    /** url. */
    private String dateUrl;

    /** 服务器 多个，隔开. */
    private String dateServers;

    /** 主题 多个，隔开. */
    private String dateTopics;

    /** 创建人. */
    private Long creater;

    /** 创建时间. */
    private Date createTime;

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

    /** 数据源名称 get方法. */
    public String getName() {
        return name == null ? null : name.trim();
    }

    /** 数据源名称 set方法. */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /** 数据源类型 get方法. */
    public SourceType getDataType() {
        return dataType;
    }

    /** 数据源类型 set方法. */
    public void setDataType(SourceType dataType) {
        this.dataType = dataType;
    }

    //    /** 数据源类型 get方法. */
//    public String getDataType() {
//        return dataType == null ? null : dataType.trim();
//    }
//
//    /** 数据源类型 set方法. */
//    public void setDataType(String dataType) {
//        this.dataType = dataType == null ? null : dataType.trim();
//    }

    /** 数据源组合json get方法. */
    public String getDataJson() {
        return dataJson == null ? null : dataJson.trim();
    }

    /** 数据源组合json set方法. */
    public void setDataJson(String dataJson) {
        this.dataJson = dataJson == null ? null : dataJson.trim();
    }

    /** 用户名 get方法. */
    public String getDateUser() {
        return dateUser == null ? null : dateUser.trim();
    }

    /** 用户名 set方法. */
    public void setDateUser(String dateUser) {
        this.dateUser = dateUser == null ? null : dateUser.trim();
    }

    /** 密码 get方法. */
    public String getDatePasswd() {
        return datePasswd == null ? null : datePasswd.trim();
    }

    /** 密码 set方法. */
    public void setDatePasswd(String datePasswd) {
        this.datePasswd = datePasswd == null ? null : datePasswd.trim();
    }

    /** url get方法. */
    public String getDateUrl() {
        return dateUrl == null ? null : dateUrl.trim();
    }

    /** url set方法. */
    public void setDateUrl(String dateUrl) {
        this.dateUrl = dateUrl == null ? null : dateUrl.trim();
    }

    /** 服务器 多个，隔开 get方法. */
    public String getDateServers() {
        return dateServers == null ? null : dateServers.trim();
    }

    /** 服务器 多个，隔开 set方法. */
    public void setDateServers(String dateServers) {
        this.dateServers = dateServers == null ? null : dateServers.trim();
    }

    /** 主题 多个，隔开 get方法. */
    public String getDateTopics() {
        return dateTopics == null ? null : dateTopics.trim();
    }

    /** 主题 多个，隔开 set方法. */
    public void setDateTopics(String dateTopics) {
        this.dateTopics = dateTopics == null ? null : dateTopics.trim();
    }

    /** 创建人 get方法. */
    public Long getCreater() {
        return creater;
    }

    /** 创建人 set方法. */
    public void setCreater(Long creater) {
        this.creater = creater;
    }

    /** 创建时间 get方法. */
    public Date getCreateTime() {
        return createTime;
    }

    /** 创建时间 set方法. */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
