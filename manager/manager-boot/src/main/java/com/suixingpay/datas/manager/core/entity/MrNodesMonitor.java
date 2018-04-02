package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

/**
 * 节点任务实时监控表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class MrNodesMonitor implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 监控节点id.
     */
    private Long scheduleId;

    /**
     * 节点id.
     */
    private String nodeId;

    /**
     * 实时监控时间.
     */
    private Date monitorDate;

    /**
     * 实时监控年月日.
     */
    private Date monitorYmd;

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
    private Long monitorSecond;

    /**
     * 并发数.
     */
    private Long monitorTps;

    /**
     * 告警次数.
     */
    private Long monitorAlarm;

    /**
     * 预留时间分区字段.
     */
    private Date partitionDay;

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
     * 监控节点id get方法.
     */
    public Long getScheduleId() {
        return scheduleId;
    }

    /**
     * 监控节点id set方法.
     */
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * 节点id get方法.
     */
    public String getNodeId() {
        return nodeId == null ? null : nodeId.trim();
    }

    /**
     * 节点id set方法.
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
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
     * 实时监控年月日 get方法.
     */
    public Date getMonitorYmd() {
        return monitorYmd;
    }

    /**
     * 实时监控年月日 set方法.
     */
    public void setMonitorYmd(Date monitorYmd) {
        this.monitorYmd = monitorYmd;
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
    public Long getMonitorSecond() {
        return monitorSecond;
    }

    /**
     * 实时监控秒 set方法.
     */
    public void setMonitorSecond(Long monitorSecond) {
        this.monitorSecond = monitorSecond;
    }

    /**
     * 并发数 get方法.
     */
    public Long getMonitorTps() {
        return monitorTps;
    }

    /**
     * 并发数 set方法.
     */
    public void setMonitorTps(Long monitorTps) {
        this.monitorTps = monitorTps;
    }

    /**
     * 告警次数 get方法.
     */
    public Long getMonitorAlarm() {
        return monitorAlarm;
    }

    /**
     * 告警次数 set方法.
     */
    public void setMonitorAlarm(Long monitorAlarm) {
        this.monitorAlarm = monitorAlarm;
    }

    /**
     * 预留时间分区字段 get方法.
     */
    public Date getPartitionDay() {
        return partitionDay;
    }

    /**
     * 预留时间分区字段 set方法.
     */
    public void setPartitionDay(Date partitionDay) {
        this.partitionDay = partitionDay;
    }

}
