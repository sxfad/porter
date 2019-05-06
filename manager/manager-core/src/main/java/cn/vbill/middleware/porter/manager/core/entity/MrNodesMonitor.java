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

import java.util.Calendar;
import java.util.Date;

import cn.vbill.middleware.porter.common.task.statistics.DTaskPerformance;

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

    public MrNodesMonitor() {
    }

    public MrNodesMonitor(DTaskPerformance performance) {
        // 节点id
        this.nodeId = performance.getNodeId();
        // 实际监控日期
        this.monitorDate = performance.getTime();
        // 实际监控年月日
        this.monitorYmd = performance.getTime();
        // Calendar计算
        Calendar cal = Calendar.getInstance();
        cal.setTime(performance.getTime());
        // 实时监控小时(24h)
        this.monitorHour = cal.get(Calendar.HOUR_OF_DAY);
        // 实时监控分.
        this.monitorMinute = cal.get(Calendar.MINUTE);
        // 实时监控秒.
        this.monitorSecond = cal.get(Calendar.SECOND);
        // 每分钟总数
        Long minutetotal = performance.getInsertRow() + performance.getErrorInsertRow() + performance.getUpdateRow()
                + performance.getErrorUpdateRow() + performance.getDeleteRow() + performance.getErrorDeleteRow();
        this.monitorTps = minutetotal;
        // 告警次数
        this.monitorAlarm = performance.getAlertedTimes();
        // 预留分区字段
        this.partitionDay = performance.getTime();
    }

    /**
     * 主键.
     */
    private Long id;

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
    private Integer monitorSecond;

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
