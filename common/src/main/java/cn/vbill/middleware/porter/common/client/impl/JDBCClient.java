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

package cn.vbill.middleware.porter.common.client.impl;

import cn.vbill.middleware.porter.common.client.LoadClient;
import cn.vbill.middleware.porter.common.client.MetaQueryClient;
import cn.vbill.middleware.porter.common.config.source.JDBCConfig;
import cn.vbill.middleware.porter.common.db.meta.TableColumn;
import cn.vbill.middleware.porter.common.db.meta.TableSchema;
import cn.vbill.middleware.porter.common.dic.DbType;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.db.DdlUtils;
import cn.vbill.middleware.porter.common.db.SqlTemplate;
import cn.vbill.middleware.porter.common.db.SqlTemplateImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ddlutils.model.Table;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public class JDBCClient extends AbstractClient<JDBCConfig> implements LoadClient, MetaQueryClient {
    private final Map<List<String>, TableSchema> tables = new ConcurrentHashMap<>();

    private BasicDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;
    private final boolean makePrimaryKeyWhenNo;

    @Getter
    private SqlTemplate sqlTemplate;


    public JDBCClient(JDBCConfig config) {
        super(config);
        this.makePrimaryKeyWhenNo = config.isMakePrimaryKeyWhenNo();
        sqlTemplate = new SqlTemplateImpl();
    }

    @Override
    protected void doStart() {
        JDBCConfig config = getConfig();
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(config.getDriverClassName());
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUserName());
        dataSource.setPassword(config.getPassword());
        dataSource.setMaxWait(config.getMaxWait());
        dataSource.setMaxActive(config.getMaxPoolSize());
        dataSource.setMaxIdle(config.getInitialPoolSize());
        //连接错误重试时间间隔
        dataSource.setValidationQueryTimeout(config.getValidationQueryTimeout());
        //数据库重启等因素导致连接池状态异常
        dataSource.setTestOnBorrow(config.isTestOnBorrow());
        dataSource.setTestOnReturn(config.isTestOnReturn());
        dataSource.setTestWhileIdle(true);
        dataSource.setNumTestsPerEvictionRun(config.getMaxPoolSize());
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        if (config.getDbType() == DbType.MYSQL) {
            dataSource.setValidationQuery("select 1");
        } else if (config.getDbType() == DbType.ORACLE) {
            dataSource.setValidationQuery("select 1 from dual");
            dataSource.addConnectionProperty("restrictGetTables", "true");
            // 将0000-00-00的时间类型返回null
            dataSource.addConnectionProperty("zeroDateTimeBehavior", "convertToNull");
            // 直接返回字符串，不做year转换date处理
            dataSource.addConnectionProperty("yearIsDateType", "false");
        }
        jdbcTemplate = new JdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(new DataSourceTransactionManager(jdbcTemplate.getDataSource()));
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }


    @Override
    protected void doShutdown() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    public final TableSchema getTable(String schema, String tableName) throws Exception {
        return getTable(schema, tableName, true);
    }

    private TableSchema getTable(String schema, String tableName, boolean cache) throws Exception {
        List<String> keyList = Arrays.asList(schema, tableName);
        if (!cache) {
            return getTableSchema(schema, tableName);
        }

        return tables.computeIfAbsent(keyList, new Function<List<String>, TableSchema>() {
            //从代码块中抛出异常
            @SneakyThrows(Exception.class)
            public TableSchema apply(List<String> strings) {
                return getTableSchema(schema, tableName);
            }
        });
    }

    /**
     * schema大写
     *
     * @param schema
     * @param tableName
     * @return
     */
    private TableSchema getTableSchema(String schema, String tableName) {
        Table dbTable = DdlUtils.findTable(jdbcTemplate, schema, schema, tableName, makePrimaryKeyWhenNo);
        TableSchema tableSchema = new TableSchema();
        //mysql特殊场景下(例如大小写敏感)，schema字段为空
        tableSchema.setSchemaName(StringUtils.isBlank(dbTable.getSchema()) ? schema : dbTable.getSchema());
        tableSchema.setTableName(StringUtils.isBlank(dbTable.getName()) ? tableName : dbTable.getName());
        Arrays.stream(dbTable.getColumns()).forEach(c -> {
            TableColumn column = new TableColumn();
            column.setDefaultValue(c.getDefaultValue());
            column.setName(c.getName());
            column.setPrimaryKey(c.isPrimaryKey());
            column.setRequired(c.isRequired());
            column.setTypeCode(c.getTypeCode());
            tableSchema.addColumn(column);
        });

        long primaryKeyCount = tableSchema.getColumns().stream().filter(c -> c.isPrimaryKey()).count();
        tableSchema.setNoPrimaryKey(primaryKeyCount < 1);

        LOGGER.info("schema:{},table:{},detail:{}", schema, tableName, JSONObject.toJSONString(tableSchema));
        return tableSchema;
    }

    @Override
    public int getDataCount(String schema, String table, String updateDateColumn, Date startTime, Date endTime) {
        String sql = sqlTemplate.getDataChangedCountSql(schema, table, updateDateColumn);
        return countQuery(sql, startTime, endTime);
    }

    private int countQuery(String sql, Date startDate, Date endDate) {
        //数组形式仅仅是为了处理回调代码块儿对final局部变量的要求
        int[] count = {0};
        try {
            this.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    if (null != rs) {
                        String value = rs.getString(1);
                        if (!StringUtils.isBlank(value) && StringUtils.isNumeric(value)) {
                            count[0] = Integer.valueOf(value).intValue();
                        }
                    }
                }
            }, startDate, endDate);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return count[0];
    }

    public int[] batchUpdate(String sqlType, String sql, List<Object[]> batchArgs) throws TaskStopTriggerException {
        int[] affect = new int[]{};
        try {
            affect = transactionTemplate.execute(new TransactionCallback<int[]>() {
                @Override
                @SneakyThrows(Throwable.class)
                public int[] doInTransaction(TransactionStatus status) {
                    return jdbcTemplate.batchUpdate(sql, batchArgs);
                }
            });
        } catch (Throwable e) {
            if (TaskStopTriggerException.isMatch(e)) throw new TaskStopTriggerException(e);
            e.printStackTrace();
        }
        if (null == affect || affect.length == 0) {
            List<Integer> affectList = new ArrayList<>();
            //分组执行
            batchErroUpdate(sqlType, 50, sql, batchArgs, 0, affectList);

            affect = Arrays.stream(affectList.toArray(new Integer[]{})).mapToInt(Integer::intValue).toArray();
        }
        LOGGER.debug("sql:{},params:{},affect:{}", sql, JSON.toJSONString(batchArgs), affect);
        return affect;
    }


    public int update(String type, String sql, Object... args) throws TaskStopTriggerException {
        int affect = 0;
        try {
            affect = transactionTemplate.execute(new TransactionCallback<Integer>() {
                @Override
                @SneakyThrows(Throwable.class)
                public Integer doInTransaction(TransactionStatus status) {
                    return jdbcTemplate.update(sql, args);
                }
            });
        } catch (Throwable e) {
            if (TaskStopTriggerException.isMatch(e)) throw new TaskStopTriggerException(e);
            e.printStackTrace();
        }
        if (affect < 1) {
            LOGGER.error("sql:{},params:{},affect:{}", sql, JSON.toJSONString(Arrays.asList(args)), affect);
        } else {
            LOGGER.debug("sql:{},params:{},affect:{}", sql, JSON.toJSONString(Arrays.asList(args)), affect);
        }
        return affect;
    }

    public void query(String sql, RowCallbackHandler rch, Object... args) throws TaskStopTriggerException {
        try {
            jdbcTemplate.query(sql, rch, args);
        } catch (DataAccessException accessException) {
            throw new TaskStopTriggerException(accessException);
        } catch (Throwable e) {
            if (TaskStopTriggerException.isMatch(e)) throw new TaskStopTriggerException(e);
        }
    }


    private void batchErroUpdate(String sqlType, int batchSize, String sql, List<Object[]> batchArgs, int from, List<Integer> affect)
            throws TaskStopTriggerException {
        int size = batchArgs.size();
        int batchEnd = from + batchSize;
        //获取当前分组
        List<Object[]> subArgs = new ArrayList<>();
        while (from < batchEnd && from < size) {
            subArgs.add(batchArgs.get(from));
            from++;
        }

        //根据当前分组批量插入
        int[] reGroupAffect = null;
        try {
            reGroupAffect = transactionTemplate.execute(new TransactionCallback<int[]>() {
                @SneakyThrows(Throwable.class)
                public int[] doInTransaction(TransactionStatus status) {
                    return jdbcTemplate.batchUpdate(sql, subArgs);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //如果仍然插入失败,改为单条插入
        if (null == reGroupAffect || reGroupAffect.length == 0) {
            for (int i = 0; i < subArgs.size(); i++) {
                affect.add(update(sqlType, sql, subArgs.get(i)));
            }
        } else {
            Arrays.stream(reGroupAffect).boxed().forEach(i -> affect.add(i));
        }
        //递归下次分组
        if (batchEnd < size) batchErroUpdate(sqlType, batchSize, sql, batchArgs, from, affect);
    }

    @Override
    public String getClientInfo() {
        JDBCConfig config = getConfig();
        return new StringBuilder().append("数据库地址->").append(config.getUrl()).append(",用户->").append(config.getUserName())
                .toString();
    }
}
