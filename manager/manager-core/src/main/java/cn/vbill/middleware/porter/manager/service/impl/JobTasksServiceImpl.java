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

package cn.vbill.middleware.porter.manager.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.config.JavaFileConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.task.config.DataConsumerConfig;
import cn.vbill.middleware.porter.common.task.config.DataLoaderConfig;
import cn.vbill.middleware.porter.common.task.config.TableMapperConfig;
import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import cn.vbill.middleware.porter.common.task.dic.TaskStatusType;
import cn.vbill.middleware.porter.common.warning.entity.WarningOwner;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import cn.vbill.middleware.porter.manager.core.dto.JDBCVo;
import cn.vbill.middleware.porter.manager.core.dto.RoleDataControl;
import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.core.entity.DataSource;
import cn.vbill.middleware.porter.manager.core.entity.DataSourcePlugin;
import cn.vbill.middleware.porter.manager.core.entity.DataTable;
import cn.vbill.middleware.porter.manager.core.entity.JobTaskNodes;
import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksField;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksTable;
import cn.vbill.middleware.porter.manager.core.enums.ConsumeConverterPlugin;
import cn.vbill.middleware.porter.manager.core.enums.ConsumerPlugin;
import cn.vbill.middleware.porter.manager.core.enums.LoaderPlugin;
import cn.vbill.middleware.porter.manager.core.enums.QuerySQL;
import cn.vbill.middleware.porter.manager.core.enums.SourceType;
import cn.vbill.middleware.porter.manager.core.mapper.JobTasksMapper;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.service.CUserService;
import cn.vbill.middleware.porter.manager.service.DataSourceService;
import cn.vbill.middleware.porter.manager.service.DataTableService;
import cn.vbill.middleware.porter.manager.service.DbSelectService;
import cn.vbill.middleware.porter.manager.service.JobTaskNodesService;
import cn.vbill.middleware.porter.manager.service.JobTasksFieldService;
import cn.vbill.middleware.porter.manager.service.JobTasksOwnerService;
import cn.vbill.middleware.porter.manager.service.JobTasksService;
import cn.vbill.middleware.porter.manager.service.JobTasksTableService;
import cn.vbill.middleware.porter.manager.service.JobTasksUserService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;

/**
 * 同步任务表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class JobTasksServiceImpl implements JobTasksService {

    private Logger logger = LoggerFactory.getLogger(JobTasksServiceImpl.class);

    @Autowired
    private JobTasksMapper jobTasksMapper;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private DataTableService dataTableService;

    @Autowired
    private JobTasksTableService jobTasksTableService;

    @Autowired
    private JobTasksUserService jobTasksUserService;

    @Autowired
    private JobTasksFieldService jobTasksFieldService;

    @Autowired
    private CUserService cUserService;

    @Autowired
    private JobTaskNodesService jobTaskNodesService;

    @Autowired
    private JobTasksOwnerService jobTasksOwnerService;

    @Override
    @Transactional
    public Integer insert(JobTasks jobTasks) {
        // 新增 JobTasks
        Integer number = jobTasksMapper.insert(jobTasks);
        // 新增 JobTasksUser
        jobTasksUserService.insertList(jobTasks);
        // 新增 JobTasksTable
        jobTasksTableService.insertList(jobTasks);
        // 新增 jobtaskNode
        jobTaskNodesService.insertList(jobTasks);
        // 新增jobtaskOwner
        jobTasksOwnerService.insertByJobTasks(jobTasks.getId());
        return number;
    }

    @Override
    @Transactional
    public Integer insertZKCapture(JobTasks jobTasks, TaskStatusType jobState) {
        jobTasks.setJobState(jobState);
        TaskConfig task = JSONObject.parseObject(jobTasks.getJobJsonText(), TaskConfig.class);
        jobTasks.setId(Long.valueOf(task.getTaskId()));
        if (jobTasks.getJobName() == null) {
            jobTasks.setJobName("本地任务-" + task.getTaskId());
        }
        // 来源数据-消费插件
        ConsumerPlugin sourceConsumeAdt = ConsumerPlugin.enumByCode(task.getConsumer().getConsumerName());
        jobTasks.setSourceConsumeAdt(sourceConsumeAdt);
        // 来源数据-消费转换插件
        ConsumeConverterPlugin sourceConvertAdt = ConsumeConverterPlugin.enumByCode(task.getConsumer().getConverter());
        jobTasks.setSourceConvertAdt(sourceConvertAdt);
        // 目标插件
        LoaderPlugin targetLoadAdt = LoaderPlugin.enumByCode(task.getLoader().getLoaderName());
        jobTasks.setTargetLoadAdt(targetLoadAdt);
        // 创建人
        if (RoleCheckContext.getUserIdHolder() == null || RoleCheckContext.getUserIdHolder().getUserId() == null) {
            jobTasks.setCreater(-1L);
        } else {
            jobTasks.setCreater(RoleCheckContext.getUserIdHolder().getUserId());
        }
        Integer number = jobTasksMapper.insertZKCapture(jobTasks);
        // 新增 JobTasksOwner
        jobTasksOwnerService.insertByJobTasks(jobTasks.getId());
        return number;
    }

    @Override
    public Integer insertCapture(JobTasks jobTasks) {
        Integer number = 0;
        try {
            number = jobTasksMapper.insertCapture(jobTasks);
            logger.info("抓取任务id[{}]插入数据库Success.", jobTasks.getId());
        } catch (Exception e) {
            logger.warn("抓取任务id[{}]插入数据库Error.", jobTasks.getId());
        }
        return number;
    }

    @Override
    @Transactional
    public Integer update(JobTasks jobTasks) {
        // 删除关联表字段
        jobTasksUserService.delete(jobTasks.getId());
        jobTasksTableService.delete(jobTasks.getId());
        jobTasksFieldService.delete(jobTasks.getId());
        jobTaskNodesService.delete(jobTasks.getId());
        // 修改主表
        Integer number = jobTasksMapper.update(jobTasks);
        // 新增 JobTasksUser
        jobTasksUserService.insertList(jobTasks);
        // 新增 JobTasksTable
        jobTasksTableService.insertList(jobTasks);
        // 新增 jobtaskNode
        jobTaskNodesService.insertList(jobTasks);
        return number;
    }

    @Override
    public Integer delete(Long id) {
        return jobTasksMapper.delete(id);
    }

    @Override
    public JobTasks selectById(Long id) {

        JobTasks jobTasks = jobTasksMapper.selectById(id);
        if (jobTasks.getJobType() == 2) {
            return jobTasks;
        }
        // 根据 sourceDataId 查询 同步数据来源实体信息
        if (jobTasks != null && jobTasks.getSourceDataId() != null) {
            DataSource sourceDataEntity = dataSourceService.selectById(jobTasks.getSourceDataId());
            jobTasks.setSourceDataEntity(sourceDataEntity);
        }
        // 根据 SourceDataSourceId 查询来源数据数据源信息
        if (jobTasks != null && jobTasks.getSourceDataSourceId() != null) {
            DataSource sourceDataSourceDba = dataSourceService.selectById(jobTasks.getSourceDataSourceId());
            jobTasks.setSourceDataSourceDba(sourceDataSourceDba);
        }
        // 根据TargetDataSourceId 查询目标数据源信息
        if (jobTasks != null && jobTasks.getTargetDataSourceId() != null) {
            DataSource sourceDataSourceDba = dataSourceService.selectById(jobTasks.getTargetDataSourceId());
            jobTasks.setTargetDataSourceDba(sourceDataSourceDba);
        }
        // 根据 JobTasksId 查询 JobTasksTable 详情
        List<JobTasksTable> tables = jobTasksTableService.selectById(id);
        if (tables != null && !tables.isEmpty()) {
            jobTasks.setTables(tables);
        }
        // 根据 JobTasksId 查询 CUser 详情
        List<CUser> cusers = cUserService.selectByJobTasksId(id);
        if (cusers != null && !cusers.isEmpty()) {
            jobTasks.setUsers(cusers);
            // 获取 CUser 主键集合
            List<Long> userIds = new ArrayList<>();
            for (CUser cUser : cusers) {
                userIds.add(cUser.getId());
            }
            jobTasks.setUserIds(userIds);
        }
        // 查询jobtasknodeid 详情
        List<JobTaskNodes> nodes = jobTaskNodesService.selectById(id);
        if (nodes != null && !nodes.isEmpty()) {
            jobTasks.setNodes(nodes);
        }
        return jobTasks;
    }

    @Override
    public JobTasks selectByIdOne(Long id) {
        return jobTasksMapper.selectById(id);
    }

    @Override
    public Integer updateZKCapture(JobTasks jobTasks, TaskStatusType jobState) {
        jobTasks.setJobState(jobState);
        TaskConfig task = JSONObject.parseObject(jobTasks.getJobJsonText(), TaskConfig.class);
        // 来源数据-消费插件
        ConsumerPlugin sourceConsumeAdt = ConsumerPlugin.enumByCode(task.getConsumer().getConsumerName());
        jobTasks.setSourceConsumeAdt(sourceConsumeAdt);
        // 来源数据-消费转换插件
        ConsumeConverterPlugin sourceConvertAdt = ConsumeConverterPlugin.enumByCode(task.getConsumer().getConverter());
        jobTasks.setSourceConvertAdt(sourceConvertAdt);
        // 目标插件
        LoaderPlugin targetLoadAdt = LoaderPlugin.enumByCode(task.getLoader().getLoaderName());
        jobTasks.setTargetLoadAdt(targetLoadAdt);
        return jobTasksMapper.updateZKCapture(jobTasks);
    }

    @Override
    public JobTasks selectEntityById(Long id) {
        JobTasks jobTasks = jobTasksMapper.selectById(id);
        return jobTasks;
    }

    @Override
    public Page<JobTasks> page(Page<JobTasks> page, String jobName, Long jobId, String beginTime, String endTime,
            TaskStatusType jobState, Integer jobType, Long id) {
        String jobStateBck = null;
        if (null != jobState) {
            jobStateBck = jobState.getCode();
        }
        // 数据权限
        RoleDataControl roleDataControl = RoleCheckContext.getUserIdHolder();
        Integer total = jobTasksMapper.pageAll(1, jobType, jobName, jobId, beginTime, endTime, jobStateBck, id,
                roleDataControl);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTasksMapper.page(page, 1, jobType, jobName, jobId, beginTime, endTime, jobStateBck, id,
                    roleDataControl));
        }
        return page;
    }

    @Override
    public Object tableNames(Long tablesId) {
        DataTable dataTable = dataTableService.selectById(tablesId);
        if (dataTable != null && dataTable.getTableName() != null) {
            return dataTable.getTableName().split(",");
        }
        return null;
    }

    @Override
    public List<String> fields(Long sourceId, Long tablesId, String tableAllName) {
        List<String> fields = null;
        DataSource dataSource = dataSourceService.selectById(sourceId);
        if (dataSource.getDataType() == SourceType.JDBC) {
            List<DataSourcePlugin> list = dataSource.getPlugins();
            String url = null;
            String username = null;
            String password = null;
            QuerySQL query = null;
            for (DataSourcePlugin dataSourcePlugin : list) {
                if (dataSourcePlugin.getFieldCode().equalsIgnoreCase("dbtype")) {
                    if (dataSourcePlugin.getFieldValue().equalsIgnoreCase("mysql")) {
                        query = QuerySQL.MYSQL;
                    }
                    if (dataSourcePlugin.getFieldValue().equalsIgnoreCase("oracle")) {
                        query = QuerySQL.ORACLE;
                    }
                }
                if (dataSourcePlugin.getFieldCode().equalsIgnoreCase("url")) {
                    url = dataSourcePlugin.getFieldValue();
                }
                if (dataSourcePlugin.getFieldCode().equalsIgnoreCase("username")) {
                    username = dataSourcePlugin.getFieldValue();
                }
                if (dataSourcePlugin.getFieldCode().equalsIgnoreCase("password")) {
                    password = dataSourcePlugin.getFieldValue();
                }
            }
            String sql = query.getTableFieldsSql();
            DbSelectService dbSelectService = ApplicationContextUtil
                    .getBean("dbJDBC" + query.getDbType() + "SelectService");
            fields = dbSelectService.fieldList(dataSource, new JDBCVo(query.getDriverName(), url, username, password),
                    sql, tableAllName);
            return fields;
        } else {
            DbSelectService dbSelectService = ApplicationContextUtil
                    .getBean("db" + dataSource.getDataType() + "SelectService");
            fields = dbSelectService.fieldList(dataSource, null, null, tableAllName);
            return fields;
        }

    }

    @Override
    public Integer updateState(Long id, TaskStatusType taskStatusType) {
        return jobTasksMapper.updateState(id, taskStatusType.getCode());
    }

    @Override
    public TaskConfig fitJobTask(Long id, TaskStatusType status) {
        JobTasks jobTasks = this.selectById(id);
        // 告警人id列表
        List<CUser> cusers = jobTasks.getUsers();
        // 权限所有者和共享者列表
        // ownerType=1:任务所有者
        List<CUser> userOwner = cUserService.selectOwnersByJobId(id, 1);
        cusers.addAll(userOwner);
        // shareType=2:任务共享者
        List<CUser> userShares = cUserService.selectOwnersByJobId(id, 2);
        cusers.addAll(userShares);
        // 告警用户信息(设置的告警人+任务权限所有者)
        WarningReceiver[] receiver = receiver(cusers);
        // 任务类型
        if (jobTasks.getJobType() == 2) {
            TaskConfig task = JSONObject.parseObject(jobTasks.getJobJsonText(), TaskConfig.class);
            task.setStatus(status);
            logger.info("taskConfig:" + JSON.toJSONString(task));
            return task;
        }
        // 来源数据-消费插件.
        ConsumerPlugin sourceConsumeAdt = jobTasks.getSourceConsumeAdt();
        // 来源数据-消费转换插件.
        ConsumeConverterPlugin sourceConvertAdt = jobTasks.getSourceConvertAdt();
        // 来源数据-元数据表分组id.
        Long sourceDataTablesId = jobTasks.getSourceDataTablesId();
        DataTable souDataTable = dataTableService.selectById(sourceDataTablesId);
        DataSource souDataSource = dataSourceService.selectById(souDataTable.getSourceId());
        // 来源数据-同步数据源id.
        Long sourceDataId = jobTasks.getSourceDataId();
        DataSource syncDataSource = dataSourceService.selectById(sourceDataId);

        // 目标数据-载入插件.
        LoaderPlugin targetLoadAdt = jobTasks.getTargetLoadAdt();
        // 目标数据-载入源id.
        Long targetDataTablesId = jobTasks.getTargetDataTablesId();
        DataTable tarDataTable = dataTableService.selectById(targetDataTablesId);
        DataSource tarDataSource = dataSourceService.selectById(tarDataTable.getSourceId());

        // 表对照关系
        List<JobTasksTable> tables = jobTasks.getTables();

        // 自定义类
        JavaFileConfig javaFileConfig = null;
        if (StringUtils.isNotEmpty(jobTasks.getJavaClassName())
                && StringUtils.isNotEmpty(jobTasks.getJavaClassContent())) {
            javaFileConfig = new JavaFileConfig(jobTasks.getJavaClassName(), jobTasks.getJavaClassContent());
        }

        // 来源数据构造函数
        DataConsumerConfig dataConsumerConfig = new DataConsumerConfig(sourceConsumeAdt.getCode(),
                sourceConvertAdt.getCode(), jobTasks.getSourceTablesName(), dataSourceMap(id, syncDataSource),
                dataSourceMap(id, souDataSource), javaFileConfig);
        // 目标数据构造函数
        DataLoaderConfig loader = new DataLoaderConfig(targetLoadAdt.getCode(), dataSourceMap(id, tarDataSource));
        // 表对应关系映射
        List<TableMapperConfig> tableMapper = tableMapperList(tables);
        // 返回构造函数
        TaskConfig taskConfig = new TaskConfig(status, id.toString(), jobTasks.getNodesString(), dataConsumerConfig,
                loader, tableMapper, receiver);
        logger.info("taskConfig:" + JSON.toJSONString(taskConfig));
        return taskConfig;
    }

    /**
     * receiver
     *
     * @param cusers
     * @return
     */
    private WarningReceiver[] receiver(List<CUser> cusers) {
        cusers = this.removeDuplicateWithOrder(cusers);
        WarningReceiver[] warningReceivers = new WarningReceiver[cusers.size()];
        for (int i = 0; i < cusers.size(); i++) {
            warningReceivers[i] = new WarningReceiver(cusers.get(i).getNickname(), cusers.get(i).getEmail(),
                    cusers.get(i).getMobile());
        }
        return warningReceivers;
    }

    /**
     * 排重下邮箱
     * @param list
     * @return
     */
    private List<CUser> removeDuplicateWithOrder(List<CUser> list) {
        Set<String> set = new HashSet<String>();
        List<CUser> newList = new ArrayList<CUser>();
        for (Iterator<CUser> iter = list.iterator(); iter.hasNext();) {
            CUser element = iter.next();
            if (set.add(element.getEmail()))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        return list;
    }

    /**
     * tableMapperList
     *
     * @param tables
     * @return
     */
    private List<TableMapperConfig> tableMapperList(List<JobTasksTable> tables) {
        List<TableMapperConfig> tableList = new ArrayList<>();
        TableMapperConfig tableMapperConfig = null;
        for (JobTasksTable jobTasksTable : tables) {
            String[] schema = {jobTasksTable.getSourceTableName().split("[.]")[0],
                    jobTasksTable.getTargetTableName().split("[.]")[0] };
            String[] table = {jobTasksTable.getSourceTableName().split("[.]")[1],
                    jobTasksTable.getTargetTableName().split("[.]")[1] };

            Map<String, String> column = null;
            if (!jobTasksTable.isDirectMapTable()) {
                column = fieldsMap(jobTasksTable.getFields());
            }
            tableMapperConfig = new TableMapperConfig(schema, table, column, jobTasksTable.isIgnoreTargetCase(),
                    jobTasksTable.isForceMatched());
            tableList.add(tableMapperConfig);
        }
        logger.info("tableMapper:" + JSON.toJSONString(tableList));
        return tableList;
    }

    /**
     * fieldsMap
     *
     * @param fields
     * @return
     */
    private Map<String, String> fieldsMap(List<JobTasksField> fields) {
        Map<String, String> map = new HashMap<>();
        for (JobTasksField jobTasksField : fields) {
            map.put(jobTasksField.getSourceTableField(), jobTasksField.getTargetTableField());
        }
        return map;
    }

    /**
     * dataSourceMap
     *
     * @param id
     * @param souDataSource
     * @return
     */
    private Map<String, String> dataSourceMap(Long id, DataSource souDataSource) {
        Map<String, String> sourceMap = new HashMap<>();
        List<DataSourcePlugin> plugins = souDataSource.getPlugins();
        for (DataSourcePlugin dataSourcePlugin : plugins) {
            sourceMap.put(dataSourcePlugin.getFieldCode(), dataSourcePlugin.getFieldValue());
        }
        sourceMap.put(SourceConfig.CLIENT_TYPE_KEY, souDataSource.getDataType().getCode());
        if (souDataSource.getDataType() == SourceType.KAFKA
                || souDataSource.getDataType() == SourceType.KAFKA_PRODUCE) {
            if (sourceMap.get("group") == null || "".equals(sourceMap.get("group"))) {
                sourceMap.put("group", "task{" + id + "}-group");
            }
        }
        return sourceMap;
    }

    @Override
    public List<JobTasks> selectList() {
        return jobTasksMapper.selectList();
    }

    @Override
    public List<JobTasks> selectJobNameList() {
        return jobTasksMapper.selectJobNameList();
    }

    @Override
    public TaskConfig dealSpecialJson(String jobXmlText) {
        TaskConfig config = new TaskConfig();
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(jobXmlText.getBytes()));
            ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
            Binder binder = new Binder(source);
            config = binder.bind("", TaskConfig.class).get();
        } catch (IOException e) {
            logger.error("解析jobXmlText失败，请注意！！", e);
        }
        return config;
    }

    @Override
    public List<Long> showjobIdList() {
        return jobTasksMapper.showjobIdList();
    }

    @Override
    public WarningOwner selectJobWarningOwner(Long id) {
        WarningOwner warningOwner = cUserService.selectJobWarningOwner(id);
        return warningOwner;
    }
}
