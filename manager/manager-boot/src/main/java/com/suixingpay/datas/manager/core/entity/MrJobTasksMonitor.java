package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

/**
 * 任务泳道实时监控表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class MrJobTasksMonitor implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 消费器泳道.
     */
    private String consumeLane;

    /**
     * 实时监控时间.
     */
    private Date monitorDate;

    /**
     * 实时监控年月日yyyy-mm-dd.
     */
    private String monitorYmd;

    /**
     * 实时监控小时.
     */
    private Integer monitorHour;

    /**
     * 实时监控分.
     */
    private Integer monitorMinute;

    /**
     * 实时监控秒.
     */
    private Integer monitorSecond;

    /**
     * 插入成功.
     */
    private Long insertSucces;

    /**
     * 插入失败.
     */
    private Long insertFailure;

    /**
     * 更新成功.
     */
    private Long updateSucces;

    /**
     * 更新失败.
     */
    private Long updateFailure;

    /**
     * 删除成功.
     */
    private Long deleteSucces;

    /**
     * 删除失败.
     */
    private Long deleteFailure;

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
     * 消费器泳道 get方法.
     */
    public String getConsumeLane() {
        return consumeLane == null ? null : consumeLane.trim();
    }

    /**
     * 消费器泳道 set方法.
     */
    public void setConsumeLane(String consumeLane) {
        this.consumeLane = consumeLane == null ? null : consumeLane.trim();
    }

    /**
     * 实时监控时间 get方法.
     */
    public Date getMonitorDate() {
        return monitorDate;
    }

    /**
     * 实时监控时间 set方法.
     */
    public void setMonitorDate(Date monitorDate) {
        this.monitorDate = monitorDate;
    }

    /**
     * 实时监控年月日yyyy-mm-dd get方法.
     */
    public String getMonitorYmd() {
        return monitorYmd == null ? null : monitorYmd.trim();
    }

    /**
     * 实时监控年月日yyyy-mm-dd set方法.
     */
    public void setMonitorYmd(String monitorYmd) {
        this.monitorYmd = monitorYmd == null ? null : monitorYmd.trim();
    }

    /**
     * 实时监控小时 get方法.
     */
    public Integer getMonitorHour() {
        return monitorHour;
    }

    /**
     * 实时监控小时 set方法.
     */
    public void setMonitorHour(Integer monitorHour) {
        this.monitorHour = monitorHour;
    }

    /**
     * 实时监控分 get方法.
     */
    public Integer getMonitorMinute() {
        return monitorMinute;
    }

    /**
     * 实时监控分 set方法.
     */
    public void setMonitorMinute(Integer monitorMinute) {
        this.monitorMinute = monitorMinute;
    }

    /**
     * 实时监控秒 get方法.
     */
    public Integer getMonitorSecond() {
        return monitorSecond;
    }

    /**
     * 实时监控秒 set方法.
     */
    public void setMonitorSecond(Integer monitorSecond) {
        this.monitorSecond = monitorSecond;
    }

    /**
     * 插入成功 get方法.
     */
    public Long getInsertSucces() {
        return insertSucces;
    }

    /**
     * 插入成功 set方法.
     */
    public void setInsertSucces(Long insertSucces) {
        this.insertSucces = insertSucces;
    }

    /**
     * 插入失败 get方法.
     */
    public Long getInsertFailure() {
        return insertFailure;
    }

    /**
     * 插入失败 set方法.
     */
    public void setInsertFailure(Long insertFailure) {
        this.insertFailure = insertFailure;
    }

    /**
     * 更新成功 get方法.
     */
    public Long getUpdateSucces() {
        return updateSucces;
    }

    /**
     * 更新成功 set方法.
     */
    public void setUpdateSucces(Long updateSucces) {
        this.updateSucces = updateSucces;
    }

    /**
     * 更新失败 get方法.
     */
    public Long getUpdateFailure() {
        return updateFailure;
    }

    /**
     * 更新失败 set方法.
     */
    public void setUpdateFailure(Long updateFailure) {
        this.updateFailure = updateFailure;
    }

    /**
     * 删除成功 get方法.
     */
    public Long getDeleteSucces() {
        return deleteSucces;
    }

    /**
     * 删除成功 set方法.
     */
    public void setDeleteSucces(Long deleteSucces) {
        this.deleteSucces = deleteSucces;
    }

    /**
     * 删除失败 get方法.
     */
    public Long getDeleteFailure() {
        return deleteFailure;
    }

    /**
     * 删除失败 set方法.
     */
    public void setDeleteFailure(Long deleteFailure) {
        this.deleteFailure = deleteFailure;
    }

}
