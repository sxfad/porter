package cn.vbill.middleware.porter.manager.core.entity;

import cn.vbill.middleware.porter.common.dic.AlertPlugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 告警配置表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-08 11:10:56
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 11:10:56
 */
public class Alarm implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键.
     */
    private Long id;

    /**
     * 告警方式.
     */
    private AlertPlugin alarmType;

    /**
     * 创建人.
     */
    private Long createUserId;

    /**
     * 修改人.
     */
    private Long updateUserId;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 修改时间.
     */
    private Date updateTime;

    /**
     * 状态.
     */
    private Integer state;

    /**
     * 是否作废.
     */
    private Integer iscancel;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 告警配置策略内容list
     */
    private List<AlarmPlugin> alarmPlugins = new ArrayList<>();

    /**
     * 用户列表
     */
    private List<AlarmUser> alarmUsers = new ArrayList<>();

    /** 用户列表. */
    private List<CUser> cusers = new ArrayList<>();

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
     * 告警方式 get方法.
     */
    public AlertPlugin getAlarmType() {
        return alarmType;
    }

    /**
     * 告警方式 set方法.
     */
    public void setAlarmType(AlertPlugin alarmType) {
        this.alarmType = alarmType;
    }

    /**
     * 创建人 get方法.
     */
    public Long getCreateUserId() {
        return createUserId;
    }

    /**
     * 创建人 set方法.
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 修改人 get方法.
     */
    public Long getUpdateUserId() {
        return updateUserId;
    }

    /**
     * 修改人 set方法.
     */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * 创建时间 get方法.
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间 set方法.
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 修改时间 get方法.
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 修改时间 set方法.
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 状态 get方法.
     */
    public Integer getState() {
        return state;
    }

    /**
     * 状态 set方法.
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 是否作废 get方法.
     */
    public Integer getIscancel() {
        return iscancel;
    }

    /**
     * 是否作废 set方法.
     */
    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
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

    public List<AlarmPlugin> getAlarmPlugins() {
        return alarmPlugins;
    }

    public void setAlarmPlugins(List<AlarmPlugin> alarmPlugins) {
        this.alarmPlugins = alarmPlugins;
    }

    public List<AlarmUser> getAlarmUsers() {
        return alarmUsers;
    }

    public void setAlarmUsers(List<AlarmUser> alarmUsers) {
        this.alarmUsers = alarmUsers;
    }

    public List<CUser> getCusers() {
        return cusers;
    }

    public void setCusers(List<CUser> cusers) {
        this.cusers = cusers;
    }

}
