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

package cn.vbill.middleware.porter.plugin.connector.jdbc.client;

import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.task.consumer.MetaQueryClient;
import cn.vbill.middleware.porter.common.util.db.DdlUtils;
import cn.vbill.middleware.porter.common.util.db.SqlTemplate;
import cn.vbill.middleware.porter.common.util.db.SqlTemplateImpl;
import cn.vbill.middleware.porter.common.util.db.meta.TableColumn;
import cn.vbill.middleware.porter.common.util.db.meta.TableSchema;
import cn.vbill.middleware.porter.common.dic.DbType;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.plugin.connector.jdbc.config.JdbcConfig;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.ddlutils.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public abstract class JdbcClient extends AbstractClient<JdbcConfig> implements MetaQueryClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcClient.class);
    @Getter
    private final JdbcWapper jdbcProxy;
    @Getter
    private SqlTemplate sqlTemplate;
    private final boolean makePrimaryKeyWhenNo;
    private final int connRetries;

    public JdbcClient(JdbcConfig config) {
        super(config);
        jdbcProxy = new JdbcWapper();
        sqlTemplate = new SqlTemplateImpl();
        this.makePrimaryKeyWhenNo = config.isMakePrimaryKeyWhenNo();
        connRetries = config.getRetries();
    }

    @Override
    protected void doStart() {
        jdbcProxy.start();
    }


    @Override
    protected void doShutdown() throws SQLException {
        if (jdbcProxy != null) {
            jdbcProxy.close();
        }
    }

    @Override
    public final TableSchema getTable(String schema, String tableName) throws Exception {
        return getTableSchema(schema, tableName);
    }

    /**
     * schema大写
     * @param schema
     * @param tableName
     * @return
     */
    private TableSchema getTableSchema(String schema, String tableName) throws InterruptedException {
        Table dbTable = jdbcProxy.findTable(schema, schema, tableName, makePrimaryKeyWhenNo);
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
        return uniqueValueQuery(sql, Integer.class, startTime, endTime);
    }

    /**
     * uniqueValueQuery
     *
     * @param sql
     * @param returnType
     * @param args
     * @param <T>
     * @return
     */
    public  <T> T uniqueValueQuery(String sql, Class<T> returnType, Object... args) {
        //数组形式仅仅是为了处理回调代码块儿对final局部变量的要求
        List<T> results = new ArrayList<>(1);
        try {
            this.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    if (null != rs) {
                        results.add(rs.getObject(1, returnType));
                    }
                }
            }, args);
        } catch (Throwable e) {
            LOGGER.error("sql执行出错:{}", sql, e);
        }
        return  null == results || results.isEmpty() ? null : results.get(0);
    }


    /**
     * query
     *
     * @param sql
     * @param rch
     * @param args
     * @throws TaskStopTriggerException
     */
    public void query(String sql, RowCallbackHandler rch, Object... args) throws TaskStopTriggerException {
        try {
            jdbcProxy.query(sql, rch, args);
        } catch (DataAccessException accessException) {
            throw new TaskStopTriggerException(accessException);
        } catch (Throwable e) {
            if (TaskStopTriggerException.isMatch(e)) {
                throw new TaskStopTriggerException(e);
            }
        }
    }



    @Override
    public String getClientInfo() {
        JdbcConfig config = getConfig();
        return new StringBuilder().append("数据库地址->").append(config.getUrl()).append(",用户->").append(config.getUserName())
                .toString();
    }


    protected final class JdbcWapper {
        private volatile DruidDataSource dataSource;
        private volatile JdbcTemplate jdbcTemplate;
        private volatile TransactionTemplate transactionTemplate;
        private final ReadWriteLock connLock = new ReentrantReadWriteLock();
        private JdbcWapper() {
        }

        private JdbcWapper start() {
            connLock.writeLock().lock();
            try {
                JdbcConfig config = getConfig();
                dataSource = new DruidDataSource();
                dataSource.setDriverClassName(config.getDriverClassName());
                dataSource.setUrl(config.getUrl());
                dataSource.setUsername(config.getUserName());
                dataSource.setPassword(config.getPassword());
                dataSource.setMaxWait(config.getMaxWait());
                //连接错误重试次数
                dataSource.setConnectionErrorRetryAttempts(config.getConnectionErrorRetryAttempts());
                //连接错误重试时间间隔
                //dataSource.setTimeBetweenConnectErrorMillis(1000);
                dataSource.setValidationQueryTimeout(config.getValidationQueryTimeout());
                //超出错误连接次数后是否退出尝试连接
                dataSource.setBreakAfterAcquireFailure(true);
                //数据库重启等因素导致连接池状态异常
                dataSource.setTestOnBorrow(config.isTestOnBorrow());
                dataSource.setTestOnReturn(config.isTestOnReturn());
                dataSource.setTestWhileIdle(true);
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

                return this;
            } finally {
                connLock.writeLock().unlock();
            }
        }

        private void close() {
            connLock.writeLock().lock();
            try {
                AbandonedConnectionCleanupThread.checkedShutdown();
                dataSource.close();
            } catch (Throwable e) {
                LOGGER.warn("关闭jdbc datasource", e);
            } finally {
                connLock.writeLock().unlock();
            }
        }

        private Table findTable(String catalogName, String schema, String tableName, boolean makePrimaryKeyWhenNo) throws InterruptedException {
            boolean sendResult = false;
            Table result = null;
            //做retries-1次尝试
            for (int i = 0; i < connRetries - 1; i++) {
                try {
                    result = nativeFindTable(catalogName, schema, tableName, makePrimaryKeyWhenNo);
                    sendResult = true;
                    break;
                } catch (Throwable e) {
                    LOGGER.warn("got error by findTable:{}.{},times:{}", catalogName, tableName, i, e);
                    Thread.sleep(1000L * 10 * 1);
                    reconnection();
                }
            }
            //做最后一次尝试，否则抛出异常
            if (!sendResult) {
                result = nativeFindTable(catalogName, schema, tableName, makePrimaryKeyWhenNo);
            }
            return result;
        }

        private Table nativeFindTable(String catalogName, String schema, String tableName, boolean makePrimaryKeyWhenNo) {
            connLock.readLock().lock();
            try {
                Table table = DdlUtils.findTable(jdbcTemplate, catalogName, schema, tableName, makePrimaryKeyWhenNo);
                return table;
            } finally {
                connLock.readLock().unlock();
            }

        }

        protected void query(String sql, RowCallbackHandler rch, Object[] args) {
            connLock.readLock().lock();
            try {
                jdbcTemplate.query(sql, rch, args);
            } finally {
                connLock.readLock().unlock();
            }
        }

        protected int[] batchUpdate(String sql, List<Object[]> batchArgs) throws TaskStopTriggerException, InterruptedException {
            return batchUpdate(sql, batchArgs, false);
        }

        protected int[] batchUpdate(String sql, List<Object[]> batchArgs, boolean capture) throws TaskStopTriggerException, InterruptedException {
            int[] result = atomicExecute(new TransactionCallback<int[]>() {
                @Override
                @SneakyThrows(Throwable.class)
                public int[] doInTransaction(TransactionStatus status) {
                    return jdbcTemplate.batchUpdate(sql, batchArgs);
                }
            }, capture);
            return null != result ? result : new int[] {};
        }

        protected int update(String sql, Object... args) throws TaskStopTriggerException, InterruptedException {
            return update(sql, false, args);
        }

        protected int update(String sql, boolean capture, Object... args) throws TaskStopTriggerException, InterruptedException {
            Integer result = atomicExecute(new TransactionCallback<Integer>() {
                @Override
                @SneakyThrows(Throwable.class)
                public Integer doInTransaction(TransactionStatus status) {
                    return jdbcTemplate.update(sql, args);
                }
            }, capture);
            return null != result ? result : -1;
        }

        private <T> T atomicExecute(TransactionCallback<T> action, boolean capture) throws TaskStopTriggerException, InterruptedException {
            boolean sendResult = false;
            T result = null;
            //做retries-1次尝试
            for (int i = 0; i < connRetries - 1; i++) {
                try {
                    result = nativeAtomicExecute(action, capture);
                    sendResult = true;
                    break;
                } catch (TaskStopTriggerException e) {
                    LOGGER.warn("got error by execute sql,times:{}", i, e);
                    Thread.sleep(1000L * 10 * 1);
                    reconnection();
                }
            }
            //做最后一次尝试，否则抛出异常
            if (!sendResult) {
                result = nativeAtomicExecute(action, capture);
            }
            return result;
        }

        private <T> T nativeAtomicExecute(TransactionCallback<T> action, boolean capture) throws TaskStopTriggerException {
            connLock.readLock().lock();
            try {
                return transactionTemplate.execute(action);
            } catch (Throwable e) {
                if (!capture && TaskStopTriggerException.isMatch(e)) {
                    throw new TaskStopTriggerException(e);
                }
                LOGGER.warn("got error by execute sql,but ignored.");
                e.printStackTrace();
                return null;
            } finally {
                connLock.readLock().unlock();
            }
        }

        synchronized void reconnection() {
            close();
            start();
        }
    }
}