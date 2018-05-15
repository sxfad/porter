/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.alert.AlertReceiver;
import com.suixingpay.datas.common.config.DataConsumerConfig;
import com.suixingpay.datas.common.config.DataLoaderConfig;
import com.suixingpay.datas.common.config.JavaFileConfig;
import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.config.TableMapperConfig;
import com.suixingpay.datas.common.config.TaskConfig;
import com.suixingpay.datas.common.dic.ConsumeConverterPlugin;
import com.suixingpay.datas.common.dic.ConsumerPlugin;
import com.suixingpay.datas.common.dic.LoaderPlugin;
import com.suixingpay.datas.common.dic.SourceType;
import com.suixingpay.datas.common.dic.TaskStatusType;
import com.suixingpay.datas.manager.core.dto.JDBCVo;
import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.core.entity.DataSourcePlugin;
import com.suixingpay.datas.manager.core.entity.DataTable;
import com.suixingpay.datas.manager.core.entity.JobTaskNodes;
import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.core.entity.JobTasksField;
import com.suixingpay.datas.manager.core.entity.JobTasksTable;
import com.suixingpay.datas.manager.core.enums.QuerySQL;
import com.suixingpay.datas.manager.core.mapper.JobTasksMapper;
import com.suixingpay.datas.manager.core.util.ApplicationContextUtil;
import com.suixingpay.datas.manager.service.CUserService;
import com.suixingpay.datas.manager.service.DataSourceService;
import com.suixingpay.datas.manager.service.DataTableService;
import com.suixingpay.datas.manager.service.DbSelectService;
import com.suixingpay.datas.manager.service.JobTaskNodesService;
import com.suixingpay.datas.manager.service.JobTasksFieldService;
import com.suixingpay.datas.manager.service.JobTasksService;
import com.suixingpay.datas.manager.service.JobTasksTableService;
import com.suixingpay.datas.manager.service.JobTasksUserService;
import com.suixingpay.datas.manager.web.page.Page;

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
        if (tables != null && tables.size() > 0) {
            jobTasks.setTables(tables);
        }
        // 根据 JobTasksId 查询 CUser 详情
        List<CUser> cusers = cUserService.selectByJobTasksId(id);
        if (cusers != null && cusers.size() > 0) {
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
        if (nodes != null && nodes.size() > 0) {
            jobTasks.setNodes(nodes);
        }
        return jobTasks;
    }

    @Override
    public Page<JobTasks> page(Page<JobTasks> page, String jobName, String beginTime, String endTime,
            TaskStatusType jobState) {
        String code = "";
        if (null != jobState) {
            code = jobState.getCode();
        }
        Integer total = jobTasksMapper.pageAll(1, jobName, beginTime, endTime, code);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTasksMapper.page(page, 1, jobName, beginTime, endTime, code));
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
                if (dataSourcePlugin.getFieldCode().toLowerCase().equals("dbtype")) {
                    if (dataSourcePlugin.getFieldValue().toLowerCase().equals("mysql")) {
                        query = QuerySQL.MYSQL;
                    }
                    if (dataSourcePlugin.getFieldValue().toLowerCase().equals("oracle")) {
                        query = QuerySQL.ORACLE;
                    }
                }
                if (dataSourcePlugin.getFieldCode().toLowerCase().equals("url")) {
                    url = dataSourcePlugin.getFieldValue();
                }
                if (dataSourcePlugin.getFieldCode().toLowerCase().equals("username")) {
                    username = dataSourcePlugin.getFieldValue();
                }
                if (dataSourcePlugin.getFieldCode().toLowerCase().equals("password")) {
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

        // 告警人id列表
        List<CUser> cusers = jobTasks.getUsers();
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
        // 告警用户信息
        AlertReceiver[] receiver = receiver(cusers);
        // 返回构造函数
        TaskConfig taskConfig = new TaskConfig(status, id.toString(), jobTasks.getNodesString(), dataConsumerConfig,
                loader, tableMapper, receiver);
        logger.info("taskConfig:" + JSON.toJSONString(taskConfig));
        return taskConfig;
    }

    private AlertReceiver[] receiver(List<CUser> cusers) {
        AlertReceiver[] alertReceivers = new AlertReceiver[cusers.size()];
        for (int i = 0; i < cusers.size(); i++) {
            alertReceivers[i] = new AlertReceiver(cusers.get(0).getNickname(), cusers.get(0).getEmail(),
                    cusers.get(0).getMobile());
        }
        return alertReceivers;
    }

    private List<TableMapperConfig> tableMapperList(List<JobTasksTable> tables) {
        List<TableMapperConfig> tableList = new ArrayList<>();
        TableMapperConfig tableMapperConfig = null;
        for (JobTasksTable jobTasksTable : tables) {
            String[] schema = { jobTasksTable.getSourceTableName().split("[.]")[0],
                    jobTasksTable.getTargetTableName().split("[.]")[0] };
            String[] table = { jobTasksTable.getSourceTableName().split("[.]")[1],
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

    private Map<String, String> fieldsMap(List<JobTasksField> fields) {
        Map<String, String> map = new HashMap<>();
        for (JobTasksField jobTasksField : fields) {
            map.put(jobTasksField.getSourceTableField(), jobTasksField.getTargetTableField());
        }
        return map;
    }

    private Map<String, String> dataSourceMap(Long id, DataSource souDataSource) {
        Map<String, String> sourceMap = new HashMap<>();
        List<DataSourcePlugin> plugins = souDataSource.getPlugins();
        for (DataSourcePlugin dataSourcePlugin : plugins) {
            sourceMap.put(dataSourcePlugin.getFieldCode(), dataSourcePlugin.getFieldValue());
        }
        sourceMap.put(SourceConfig.SOURCE_TYPE_KEY, souDataSource.getDataType().getCode());
        if (souDataSource.getDataType() == SourceType.KAFKA) {
            sourceMap.put("group", "task{" + id + "}-group");
        }
        return sourceMap;
    }

    @Override
    public List<JobTasks> selectList() {
        return jobTasksMapper.selectList();
    }
}
