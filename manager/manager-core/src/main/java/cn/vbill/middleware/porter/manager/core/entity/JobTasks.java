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

import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import cn.vbill.middleware.porter.manager.core.enums.ConsumeConverterPlugin;
import cn.vbill.middleware.porter.manager.core.enums.ConsumerPlugin;
import cn.vbill.middleware.porter.manager.core.enums.LoaderPlugin;
import cn.vbill.middleware.porter.common.task.dic.TaskStatusType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 同步任务表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class JobTasks implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public JobTasks() {

    }

    public JobTasks(String jobId) {
        this.id = Long.valueOf(jobId);
    }

    public JobTasks(TaskConfig task, String taskConfigJson) {
        this.id = Long.valueOf(task.getTaskId());
        this.jobName = "本地任务:" + task.getTaskId();
        this.jobJsonText = taskConfigJson;
    }

    /**
     * 主键.
     */
    private Long id;

    /**
     * 任务名称.
     */
    private String jobName;

    /**
     * 自定义处理类包路径和类名 .
     */
    private String javaClassName;

    /**
     * 自定义处理类文件路径.
     */
    private String javaClassContent;

    /**
     * xml配置文档.
     */
    private String jobXmlText;

    /**
     * josn配置文档.
     */
    private String jobJsonText;

    /**
     * 任务状态.
     */
    private TaskStatusType jobState;

    /**
     * 任务类型
     */
    private Integer jobType;

    /**
     * 来源数据-消费插件.
     */
    private ConsumerPlugin sourceConsumeAdt;

    /**
     * 来源数据-消费插件.页面展示
     */
    private String sourceConsumeAdtName;

    /**
     * 来源数据-消费转换插件.
     */
    private ConsumeConverterPlugin sourceConvertAdt;

    /**
     * 来源数据-消费转换插件.页面展示
     */
    private String sourceConvertAdtName;

    /**
     * 来源数据-元数据表分组id.
     */
    private Long sourceDataTablesId;

    /**
     * 来源数据-元数据表数据源id.
     */
    private Long sourceDataSourceId;

    /**
     * 来源数据-元数据数据源信息.
     */
    private DataSource sourceDataSourceDba;

    /**
     * 来源数据-元数据表分组名称.
     */
    private String sourceDataTablesName;

    /**
     * 来源数据-同步数据源id.(kafka\cancl)
     */
    private Long sourceDataId;

    /**
     * 来源数据-同步数据源实体.(kafka\cancl)
     */
    private DataSource sourceDataEntity;

    /**
     * 来源数据-同步数据源名称.
     */
    private String sourceDataName;

    /**
     * 目标数据-载入插件. 页面显示
     */
    private String targetLoadAdtName;

    /**
     * 目标数据-载入插件.
     */
    private LoaderPlugin targetLoadAdt;

    /**
     * 目标数据-载入源id.
     */
    private Long targetDataTablesId;

    /**
     * 目标数据-数据源id.
     */
    private Long targetDataSourceId;

    /**
     * 目标数据-数据源信息.
     */
    private DataSource targetDataSourceDba;

    /**
     * 目标数据-载入源名称.
     */
    private String targetDataTablesName;

    /**
     * 创建人.
     */
    private Long creater;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 状态.
     */
    private Integer state;

    /**
     * 任务类型
     */
    private Integer type;

    /**
     * 是否作废.
     */
    private Integer iscancel;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 分发节点
     */
    private List<JobTaskNodes> nodes = new ArrayList<>();

    /**
     * 分发节点id
     */
    private List<String> nodeIds = new ArrayList<>();

    /**
     * 分发节点id字符串-,隔开
     */
    private String nodesString;

    /**
     * 告警人id列表.
     */
    private List<Long> userIds = new ArrayList<>();

    /**
     * 告警人信息列表
     */
    private List<CUser> users = new ArrayList<>();

    /**
     * 表对照关系.
     */
    private List<JobTasksTable> tables = new ArrayList<>();

    /**
     * 源表表名 多个用,隔开
     */
    private String sourceTablesName;

    /**
     * 目标表表名 多个用,隔开
     */
    private String targetTablesName;

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
     * 任务名称 get方法.
     */
    public String getJobName() {
        return jobName == null ? null : jobName.trim();
    }

    /**
     * 任务名称 set方法.
     */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /**
     * 任务状态 get方法.
     */
    public TaskStatusType getJobState() {
        return jobState;
    }

    /**
     * 任务状态 set方法.
     */
    public void setJobState(TaskStatusType jobState) {
        this.jobState = jobState;
    }

    /**
     * 来源数据-消费插件 get方法.
     */
    public ConsumerPlugin getSourceConsumeAdt() {
        return sourceConsumeAdt;
    }

    /**
     * 来源数据-消费插件 set方法.
     */
    public void setSourceConsumeAdt(ConsumerPlugin sourceConsumeAdt) {
        this.sourceConsumeAdtName = null != sourceConsumeAdt ? sourceConsumeAdt.toString() : null;
        this.sourceConsumeAdt = sourceConsumeAdt;
    }

    /**
     * 来源数据-消费转换插件 get方法.
     */
    public ConsumeConverterPlugin getSourceConvertAdt() {
        return sourceConvertAdt;
    }

    /**
     * 来源数据-消费转换插件 set方法.
     */
    public void setSourceConvertAdt(ConsumeConverterPlugin sourceConvertAdt) {
        this.sourceConvertAdtName = null != sourceConvertAdt ? sourceConvertAdt.toString() : null;
        this.sourceConvertAdt = sourceConvertAdt;
    }

    /**
     * 来源数据-元数据表分组id get方法.
     */
    public Long getSourceDataTablesId() {
        return sourceDataTablesId;
    }

    /**
     * 来源数据-元数据表分组id set方法.
     */
    public void setSourceDataTablesId(Long sourceDataTablesId) {
        this.sourceDataTablesId = sourceDataTablesId;
    }

    /**
     * 来源数据-元数据表分组名称 get方法.
     */
    public String getSourceDataTablesName() {
        return sourceDataTablesName == null ? null : sourceDataTablesName.trim();
    }

    /**
     * 来源数据-元数据表分组名称 set方法.
     */
    public void setSourceDataTablesName(String sourceDataTablesName) {
        this.sourceDataTablesName = sourceDataTablesName == null ? null : sourceDataTablesName.trim();
    }

    /**
     * 来源数据-同步数据源id get方法.
     */
    public Long getSourceDataId() {
        return sourceDataId;
    }

    /**
     * 来源数据-同步数据源id set方法.
     */
    public void setSourceDataId(Long sourceDataId) {
        this.sourceDataId = sourceDataId;
    }

    /**
     * 来源数据-同步数据源名称 get方法.
     */
    public String getSourceDataName() {
        return sourceDataName == null ? null : sourceDataName.trim();
    }

    /**
     * 来源数据-同步数据源名称 set方法.
     */
    public void setSourceDataName(String sourceDataName) {
        this.sourceDataName = sourceDataName == null ? null : sourceDataName.trim();
    }

    /**
     * 目标数据-载入插件 get方法.
     */
    public LoaderPlugin getTargetLoadAdt() {
        return targetLoadAdt;
    }

    /**
     * 目标数据-载入插件 set方法.
     */
    public void setTargetLoadAdt(LoaderPlugin targetLoadAdt) {
        this.targetLoadAdtName = null != targetLoadAdt ? targetLoadAdt.toString() :null;
        this.targetLoadAdt = targetLoadAdt;
    }

    /**
     * 目标数据-载入数据组id get方法.
     */
    public Long getTargetDataTablesId() {
        return targetDataTablesId;
    }

    /**
     * 目标数据-载入数据组id set方法.
     */
    public void setTargetDataTablesId(Long targetDataTablesId) {
        this.targetDataTablesId = targetDataTablesId;
    }

    /**
     * 目标数据-载入数据组名称 get方法.
     */
    public String getTargetDataTablesName() {
        return targetDataTablesName == null ? null : targetDataTablesName.trim();
    }

    /**
     * 目标数据-载入数据组名称 set方法.
     */
    public void setTargetDataTablesName(String targetDataTablesName) {
        this.targetDataTablesName = targetDataTablesName == null ? null : targetDataTablesName.trim();
    }

    /**
     * 创建人 get方法.
     */
    public Long getCreater() {
        return creater;
    }

    /**
     * 创建人 set方法.
     */
    public void setCreater(Long creater) {
        this.creater = creater;
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

    public String getJavaClassName() {
        return javaClassName;
    }

    public void setJavaClassName(String javaClassName) {
        this.javaClassName = javaClassName == null ? null : javaClassName.trim();
    }

    public String getJavaClassContent() {
        return javaClassContent;
    }

    public void setJavaClassContent(String javaClassContent) {
        this.javaClassContent = javaClassContent == null ? null : javaClassContent.trim();
    }

    /**
     * 告警人id列表 get方法.
     */
    public List<Long> getUserIds() {
        return userIds;
    }

    /**
     * 告警人id列表 set方法.
     */
    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    /**
     * 告警人信息列表 get方法.
     */
    public List<CUser> getUsers() {
        return users;
    }

    /**
     * 告警人信息列表 set方法.
     */
    public void setUsers(List<CUser> users) {
        this.users = users;
    }

    public List<JobTasksTable> getTables() {
        return tables;
    }

    /**
     * setTables
     *
     * @param tables
     */
    public void setTables(List<JobTasksTable> tables) {

        StringBuffer linkSource = new StringBuffer();
        StringBuffer linkTarget = new StringBuffer();
        // 连接 源表表名 用,隔开 连接 目标表表名 用,隔开
        for (int i = 0; i < tables.size(); i++) {
            if (i == 0) {
                linkSource.append(tables.get(i).getSourceTableName());
                linkTarget.append(tables.get(i).getTargetTableName());
            } else {
                linkTarget.append(",").append(tables.get(i).getTargetTableName());
                linkSource.append(",").append(tables.get(i).getSourceTableName());
            }
        }

        this.sourceTablesName = linkSource.toString();
        this.targetTablesName = linkTarget.toString();
        this.tables = tables;
    }

    /**
     * 拼接后源表表名 get方法.
     */
    public String getSourceTablesName() {
        return sourceTablesName;
    }

    /**
     * 连接源表表名，set方法.
     */
    public void setSourceTablesName(String sourceTablesName) {
        this.sourceTablesName = sourceTablesName;
    }

    /**
     * 拼接后目标表表名 get方法.
     */
    public String getTargetTablesName() {
        return targetTablesName;
    }

    /**
     * 连接目标表表名，set方法.
     */
    public void setTargetTablesName(String targetTablesName) {
        this.targetTablesName = targetTablesName;
    }

    /**
     * 来源数据-元数据表数据源id. get方法
     */
    public Long getSourceDataSourceId() {
        return sourceDataSourceId;
    }

    /**
     * 来源数据-元数据表数据源id. set方法
     */
    public void setSourceDataSourceId(Long sourceDataSourceId) {
        this.sourceDataSourceId = sourceDataSourceId;
    }

    /**
     * 目标数据-数据源id. get方法
     */
    public Long getTargetDataSourceId() {
        return targetDataSourceId;
    }

    /**
     * 目标数据-数据源id. set方法
     */
    public void setTargetDataSourceId(Long targetDataSourceId) {
        this.targetDataSourceId = targetDataSourceId;
    }

    public String getSourceConsumeAdtName() {
        return sourceConsumeAdtName;
    }

    public void setSourceConsumeAdtName(String sourceConsumeAdtName) {
        this.sourceConsumeAdtName = sourceConsumeAdtName;
    }

    public String getSourceConvertAdtName() {
        return sourceConvertAdtName;
    }

    public void setSourceConvertAdtName(String sourceConvertAdtName) {
        this.sourceConvertAdtName = sourceConvertAdtName;
    }

    public String getTargetLoadAdtName() {
        return targetLoadAdtName;
    }

    public void setTargetLoadAdtName(String targetLoadAdtName) {
        this.targetLoadAdtName = targetLoadAdtName;
    }

    public List<JobTaskNodes> getNodes() {
        return nodes;
    }

    /**
     * setNodes
     *
     * @param nodes
     */
    public void setNodes(List<JobTaskNodes> nodes) {
        List<String> nodesIds = new ArrayList<>();
        StringBuffer nodesString = new StringBuffer();
        for (int i = 0; i < nodes.size(); i++) {
            nodesIds.add(nodes.get(i).getNodeId());
            if (i == 0) {
                nodesString.append(nodes.get(i).getNodeId());
            } else {
                nodesString.append(",").append(nodes.get(i).getNodeId());
            }
        }
        this.nodeIds = nodesIds;
        this.nodesString = nodesString.toString();
        this.nodes = nodes;
    }

    public String getNodesString() {
        return nodesString;
    }

    public void setNodesString(String nodesString) {
        this.nodesString = nodesString;
    }

    public List<String> getNodeIds() {
        return nodeIds;
    }

    public void setNodeIds(List<String> nodeIds) {
        this.nodeIds = nodeIds;
    }

    public DataSource getSourceDataEntity() {
        return sourceDataEntity;
    }

    public void setSourceDataEntity(DataSource sourceDataEntity) {
        this.sourceDataEntity = sourceDataEntity;
    }

    public DataSource getSourceDataSourceDba() {
        return sourceDataSourceDba;
    }

    public void setSourceDataSourceDba(DataSource sourceDataSourceDba) {
        this.sourceDataSourceDba = sourceDataSourceDba;
    }

    public DataSource getTargetDataSourceDba() {
        return targetDataSourceDba;
    }

    public void setTargetDataSourceDba(DataSource targetDataSourceDba) {
        this.targetDataSourceDba = targetDataSourceDba;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getJobXmlText() {
        return jobXmlText;
    }

    public void setJobXmlText(String jobXmlText) {
        this.jobXmlText = jobXmlText;
    }

    public String getJobJsonText() {
        return jobJsonText;
    }

    public void setJobJsonText(String jobJsonText) {
        this.jobJsonText = jobJsonText;
    }

}
