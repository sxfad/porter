/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suixingpay.datas.common.alert.AlertReceiver;
import com.suixingpay.datas.common.config.DataConsumerConfig;
import com.suixingpay.datas.common.config.DataLoaderConfig;
import com.suixingpay.datas.common.config.TableMapperConfig;
import com.suixingpay.datas.common.config.TaskConfig;
import com.suixingpay.datas.common.dic.TaskStatusType;
import com.suixingpay.datas.manager.core.dto.JDBCVo;
import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.core.entity.DataSourcePlugin;
import com.suixingpay.datas.manager.core.entity.DataTable;
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
    private CUserService cUserService;

    @Override
    @Transactional
    public Integer insert(JobTasks jobTasks) {
        // 新增 JobTasks
        Integer number = jobTasksMapper.insert(jobTasks);
        //新增 JobTasksUser
        jobTasksUserService.insertList(jobTasks);
        //新增 JobTasksTable
        jobTasksTableService.insertList(jobTasks);
        return number;
    }

    @Override
    public Integer update(Long id, JobTasks jobTasks) {
        return jobTasksMapper.update(id, jobTasks);
    }

    @Override
    public Integer delete(Long id) {
        return jobTasksMapper.delete(id);
    }

    @Override
    public JobTasks selectById(Long id) {

        JobTasks jobTasks = jobTasksMapper.selectById(id);
        // 根据 JobTasksId 查询 JobTasksTable 详情
        List<JobTasksTable> tables = jobTasksTableService.selectById(id);
        jobTasks.setTables(tables);
        //根据 JobTasksId 查询 CUser 详情
        List<CUser> cusers = cUserService.selectByJobTasksId(id);
        jobTasks.setUsers(cusers);

        return jobTasks;
    }

    @Override
    public Page<JobTasks> page(Page<JobTasks> page, String jobName, String beginTime, String endTime) {
        Integer total = jobTasksMapper.pageAll(1, jobName, beginTime, endTime);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTasksMapper.page(page, 1, jobName, beginTime, endTime));
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
        DataSource dataSource = dataSourceService.selectById(sourceId);
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
        DbSelectService dbSelectService = ApplicationContextUtil.getBean("db" + query.getDbType() + "SelectService");
        List<String> fields = dbSelectService.fieldList(new JDBCVo(query.getDriverName(), url, username, password), sql,
                tableAllName);
        return fields;
    }

    @Override
    public Integer updateState(Long id, TaskStatusType taskStatusType) {
        return jobTasksMapper.updateState(id, taskStatusType.getCode());
    }

    @Override
    public TaskConfig fitJobTask(Long id, TaskStatusType status) {
        JobTasks jobTasks = this.selectById(id);
        // 来源数据-消费插件.
        String sourceConsumeAdt = jobTasks.getSourceConsumeAdt();
        // 来源数据-消费转换插件.
        String sourceConvertAdt = jobTasks.getSourceConvertAdt();
        // 来源数据-元数据表分组id.
        Long sourceDataTablesId = jobTasks.getSourceDataTablesId();
        DataTable souDataTable = dataTableService.selectById(sourceDataTablesId);
        DataSource souDataSource = dataSourceService.selectById(souDataTable.getSourceId());
        // 来源数据-同步数据源id.
        Long sourceDataId = jobTasks.getSourceDataId();
        DataSource syncDataSource = dataSourceService.selectById(sourceDataId);

        // 目标数据-载入插件.
        String targetLoadAdt = jobTasks.getTargetLoadAdt();
        // 目标数据-载入源id.
        Long targetDataTablesId = jobTasks.getTargetDataTablesId();
        DataTable tarDataTable = dataTableService.selectById(targetDataTablesId);
        DataSource tarDataSource = dataSourceService.selectById(tarDataTable.getSourceId());

        // 告警人id列表
        List<CUser> cusers = null;
        // 表对照关系
        List<JobTasksTable> tables = jobTasks.getTables();

        // 来源数据构造函数
        DataConsumerConfig dataConsumerConfig = new DataConsumerConfig(sourceConsumeAdt, sourceConvertAdt, "",
                dataSourceMap(souDataSource), dataSourceMap(syncDataSource));
        // 目标数据构造函数
        DataLoaderConfig loader = new DataLoaderConfig(targetLoadAdt, dataSourceMap(tarDataSource));
        // 表对应关系映射
        List<TableMapperConfig> tableMapper = tableMapperList(tables);
        // 告警用户信息
        AlertReceiver[] receiver = receiver(cusers);
        // 返回构造函数
        return new TaskConfig(status, id.toString(), dataConsumerConfig, loader, tableMapper, receiver);
    }

    private AlertReceiver[] receiver(List<CUser> cusers) {
        AlertReceiver[] alertReceivers = new AlertReceiver[cusers.size()];
        for (int i = 0; i < cusers.size(); i++) {
            alertReceivers[i] = null;
        }
        return alertReceivers;
    }

    private List<TableMapperConfig> tableMapperList(List<JobTasksTable> tables) {
        List<TableMapperConfig> tableList = new ArrayList<>();
        TableMapperConfig tableMapperConfig = null;
        for (JobTasksTable jobTasksTable : tables) {
            String[] schema = jobTasksTable.getSourceTableName().split(".");
            String[] table = jobTasksTable.getTargetTableName().split(".");
            Map<String, String> column = fieldsMap(jobTasksTable.getFields());
            tableMapperConfig = new TableMapperConfig(schema, table, column);
            tableList.add(tableMapperConfig);
        }
        return tableList;
    }

    private Map<String, String> fieldsMap(List<JobTasksField> fields) {
        Map<String, String> map = new HashMap<>();
        for (JobTasksField jobTasksField : fields) {
            map.put(jobTasksField.getSourceTableField(), jobTasksField.getTargetTableField());
        }
        return map;
    }

    private Map<String, String> dataSourceMap(DataSource souDataSource) {
        Map<String, String> sourceMap = new HashMap<>();
        List<DataSourcePlugin> plugins = souDataSource.getPlugins();
        for (DataSourcePlugin dataSourcePlugin : plugins) {
            sourceMap.put(dataSourcePlugin.getFieldCode(), dataSourcePlugin.getFieldValue());
        }
        return sourceMap;
    }
}
