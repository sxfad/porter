/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.LoadClient;
import com.suixingpay.datas.common.client.MetaQueryClient;
import com.suixingpay.datas.common.db.SqlTemplate;
import com.suixingpay.datas.common.config.source.JDBCConfig;
import com.suixingpay.datas.common.db.SqlTemplateImpl;
import com.suixingpay.datas.common.db.meta.DbType;
import com.suixingpay.datas.common.db.DdlUtils;
import com.suixingpay.datas.common.db.meta.TableColumn;
import com.suixingpay.datas.common.db.meta.TableSchema;
import lombok.Getter;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public class JDBCClient extends AbstractClient<JDBCConfig> implements LoadClient, MetaQueryClient {
    private final Map<List<String>, TableSchema> tables = new ConcurrentHashMap<>();

    private DruidDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;

    @Getter private SqlTemplate sqlTemplate;


    public JDBCClient(JDBCConfig config) {
        super(config);
        sqlTemplate = new SqlTemplateImpl();
    }

    @Override
    protected void doStart() {
        JDBCConfig config = getConfig();
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName(config.getDriverClassName());
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUserName());
        dataSource.setPassword(config.getPassword());
        dataSource.setMaxWait(config.getMaxWait());

        if (config.getDbType() == DbType.MYSQL) {
            dataSource.setValidationQuery("select 1");
        } else if (config.getDbType() == DbType.ORACLE) {
            dataSource.setValidationQuery("select 1 from dual");
            dataSource.addConnectionProperty("restrictGetTables", "true");
            dataSource.addConnectionProperty("zeroDateTimeBehavior", "convertToNull");// 将0000-00-00的时间类型返回null
            dataSource.addConnectionProperty("yearIsDateType", "false");// 直接返回字符串，不做year转换date处理
        }
        jdbcTemplate = new JdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(new DataSourceTransactionManager(jdbcTemplate.getDataSource()));
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }



    @Override
    protected void doShutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    public TableSchema getTable(String schema, String tableName) {
        TableSchema table = getTable(schema, tableName, true);
        return null != table ? table : getTable(schema, tableName, false);
    }

    @Override
    public TableSchema getTable(String schema, String tableName, boolean cache) {
        List<String> keyList = Arrays.asList(schema, tableName);
        if (!cache) tables.remove(keyList);
        return tables.computeIfAbsent(keyList, new Function<List<String>, TableSchema>() {
            @Override
            public TableSchema apply(List<String> strings) {
                try {
                    Table dbTable = DdlUtils.findTable(jdbcTemplate, schema, schema, tableName, null);
                    TableSchema tableSchema = new TableSchema();
                    tableSchema.setSchemaName(dbTable.getSchema());
                    tableSchema.setTableName(dbTable.getName());
                    Arrays.stream(dbTable.getColumns()).forEach(c -> {
                        TableColumn column = new TableColumn();
                        column.setDefaultValue(c.getDefaultValue());
                        column.setName(c.getName());
                        column.setPrimaryKey(c.isPrimaryKey());
                        column.setRequired(c.isRequired());
                        column.setTypeCode(c.getTypeCode());
                        tableSchema.addColumn(column);
                    });
                    return tableSchema;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }

    @Override
    public int getDataCount(String schema, String table, String updateDateColumn, Date startTime, Date endTime) {
        String sql = sqlTemplate.getDataChangedCountSql(schema, table, updateDateColumn);
        return countQuery(sql, startTime, endTime);
    }

    private int countQuery(String sql, Date startDate, Date endDate) {
        //数组形式仅仅是为了处理回调代码块儿对final局部变量的要求
        int[] count = {0};
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
        return  count[0];
    }



    public int update(String sql, Object... args) {
        int affect = 0;
        try {
            affect = transactionTemplate.execute(new TransactionCallback<Integer>() {
                @Override
                public Integer doInTransaction(TransactionStatus status) {
                    return jdbcTemplate.update(sql, args);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (affect < 1) {
            LOGGER.error("sql:{},params:{},affect:{}", sql, JSON.toJSONString(Arrays.asList(args)), affect);
        } else {
            LOGGER.debug("sql:{},params:{},affect:{}", sql, JSON.toJSONString(Arrays.asList(args)), affect);
        }
        return  affect;
    }

    public int update(String sql) {
        int affect = 0;
        try {
            affect = transactionTemplate.execute(new TransactionCallback<Integer>() {
                @Override
                public Integer doInTransaction(TransactionStatus status) {
                    return jdbcTemplate.update(sql);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (affect < 1) {
            LOGGER.error("sql:{},affect:{}", sql, affect);
        } else {
            LOGGER.debug("sql:{},affect:{}", sql, affect);
        }
        return affect;
    }

    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        int[] affect = new int[]{};
        try {
            affect = transactionTemplate.execute(new TransactionCallback<int[]>() {
                @Override
                public int[] doInTransaction(TransactionStatus status) {
                    return jdbcTemplate.batchUpdate(sql, batchArgs);
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (null == affect || affect.length == 0) {
            List<Integer> affectList = new ArrayList<>();
            //分组执行
            batchErroUpdate(50, sql, batchArgs, 0, affectList);

            affect = Arrays.stream(affectList.toArray(new Integer[]{})).mapToInt(Integer::intValue).toArray();
        }
        LOGGER.debug("sql:{},params:{},affect:{}", sql, JSON.toJSONString(batchArgs), affect);
        return affect;
    }

    public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
        jdbcTemplate.query(sql, rch, args);
    }

    private void batchErroUpdate(int batchSize, String sql, List<Object[]> batchArgs,int from, List<Integer> affect) {
        int size = batchArgs.size();
        int batchEnd = from + batchSize;
        //获取当前分组
        List<Object[]> subArgs = new ArrayList<>();
        while (from < batchEnd && from < size) {
            subArgs.add(batchArgs.get(from));
            from ++;
        }

        //根据当前分组批量插入
        int[] reGroupAffect = null;
        try {
            reGroupAffect = transactionTemplate.execute(new TransactionCallback<int[]>() {
                @Override
                public int[] doInTransaction(TransactionStatus status) {
                    return jdbcTemplate.batchUpdate(sql, subArgs);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果仍然插入失败,改为单条插入
        if (null == reGroupAffect || reGroupAffect.length == 0) {
            for (int i = 0; i < subArgs.size(); i++) {
                affect.add(update(sql, subArgs.get(i)));
            }
        } else {
            Arrays.stream(reGroupAffect).boxed().forEach(i -> affect.add(i));
        }
        //递归下次分组
        if (batchEnd < size) batchErroUpdate(batchSize, sql, batchArgs, from, affect);
    }

}
