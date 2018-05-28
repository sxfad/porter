package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

/**
 * ogg表数据信息 实体Entity
 * 
 * @author: FairyHood
 * @date: 2018-05-25 16:30:41
 * @version: V1.0-auto
 * @review: FairyHood/2018-05-25 16:30:41
 */
public class OggTables implements java.io.Serializable {

    public OggTables() {

    }

    public OggTables(String ipAddress, String tableValue, String heartBeatTime) {
        this.ipAddress = ipAddress;
        this.tableValue = tableValue;
        this.heartBeatTime = heartBeatTime;
    }

    public OggTables(Long id, String heartBeatTime) {
        this.id = id;
        this.heartBeatTime = heartBeatTime;
    }

    private static final long serialVersionUID = 1L;

    /** 主键. */
    private Long id;

    /** ip地址. */
    private String ipAddress;

    /** ip名称. */
    private String ipName;

    /** 表名汉字. */
    private String tableName;

    /** 表名字段. */
    private String tableValue;

    /** 表名标识. */
    private String tableMarker;

    /** 心跳时间. */
    private String heartBeatTime;

    /** 创建时间. */
    private Date createTime;

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

    /** ip地址 get方法. */
    public String getIpAddress() {
        return ipAddress == null ? null : ipAddress.trim();
    }

    /** ip地址 set方法. */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    /** ip名称 get方法. */
    public String getIpName() {
        return ipName == null ? null : ipName.trim();
    }

    /** ip名称 set方法. */
    public void setIpName(String ipName) {
        this.ipName = ipName == null ? null : ipName.trim();
    }

    /** 表名汉字 get方法. */
    public String getTableName() {
        return tableName == null ? null : tableName.trim();
    }

    /** 表名汉字 set方法. */
    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    /** 表名字段 get方法. */
    public String getTableValue() {
        return tableValue == null ? null : tableValue.trim();
    }

    /** 表名字段 set方法. */
    public void setTableValue(String tableValue) {
        this.tableValue = tableValue == null ? null : tableValue.trim();
    }

    /** 表名标识 get方法. */
    public String getTableMarker() {
        return tableMarker == null ? null : tableMarker.trim();
    }

    /** 表名标识 set方法. */
    public void setTableMarker(String tableMarker) {
        this.tableMarker = tableMarker == null ? null : tableMarker.trim();
    }

    /** 心跳时间 get方法. */
    public String getHeartBeatTime() {
        return heartBeatTime == null ? null : heartBeatTime.trim();
    }

    /** 心跳时间 set方法. */
    public void setHeartBeatTime(String heartBeatTime) {
        this.heartBeatTime = heartBeatTime == null ? null : heartBeatTime.trim();
    }

    /** 创建时间 get方法. */
    public Date getCreateTime() {
        return createTime;
    }

    /** 创建时间 set方法. */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
