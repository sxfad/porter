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
import cn.vbill.middleware.porter.manager.core.init.ResourceUtils;

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

    public MrJobTasksMonitor() {

    }

    public MrJobTasksMonitor(DTaskPerformance performance) {
        // 任务id
        this.jobId = performance.getTaskId();
        // 节点id
        this.nodeId = performance.getNodeId();
        // 节点id-ip地址
        this.nodeIdIp = ResourceUtils.obtainNodeIp(performance.getNodeId());
        // 泳道ID
        this.swimlaneId = performance.getSwimlaneId();
        // 数据表
        this.schemaTable = performance.getSchema() + "." + performance.getTable();
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
        /** 插入成功. */
        this.insertSucces = performance.getInsertRow();
        /** 插入失败. */
        this.insertFailure = performance.getErrorInsertRow();
        /** 更新成功. */
        this.updateSucces = performance.getUpdateRow();
        /** 更新失败. */
        this.updateFailure = performance.getErrorUpdateRow();
        /** 删除成功. */
        this.deleteSucces = performance.getDeleteRow();
        /** 删除失败. */
        this.deleteFailure = performance.getErrorDeleteRow();
        // 告警次数
        this.alarmNumber = performance.getAlertedTimes();
        // 分区预留字段
        this.partitionDay = performance.getTime();
    }

    /**
     * 主键.
     */
    private Long id;

    /**
     * 任务id.
     */
    private String jobId;

    /**
     * 节点id.
     */
    private String nodeId;

    /**
     * 节点id[ip].
     */
    private String nodeIdIp;

    /**
     * 任务泳道.
     */
    private String swimlaneId;

    /**
     * 表全名
     */
    private String schemaTable;

    /**
     * 实时监控时间.
     */
    private Date monitorDate;

    /**
     * 实时监控年月日.
     */
    private Date monitorYmd;

    /**
     * 实时监控小时(24h).
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
     * 告警次数
     */
    private Long alarmNumber;

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
     * 任务id get方法.
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * 任务id set方法.
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
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
     * 节点id[ip] get方法.
     */
    public String getNodeIdIp() {
        return nodeIdIp == null ? null : nodeIdIp.trim();
    }

    /**
     * 节点id[ip] set方法.
     */
    public void setNodeIdIp(String nodeIdIp) {
        this.nodeIdIp = nodeIdIp == null ? null : nodeIdIp.trim();
    }

    /**
     * 任务泳道 get方法.
     */
    public String getSwimlaneId() {
        return swimlaneId == null ? null : swimlaneId.trim();
    }

    /**
     * 任务泳道 set方法.
     */
    public void setSwimlaneId(String swimlaneId) {
        this.swimlaneId = swimlaneId == null ? null : swimlaneId.trim();
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
     * 实时监控小时(24h) get方法.
     */
    public Integer getMonitorHour() {
        return monitorHour;
    }

    /**
     * 实时监控小时(24h) set方法.
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

    public Long getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(Long alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public String getSchemaTable() {
        return schemaTable;
    }

    public void setSchemaTable(String schemaTable) {
        this.schemaTable = schemaTable;
    }

}
