/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 14:10
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.dialect;

import com.suixingpay.datas.node.core.db.utils.meta.DdlUtils;
import com.suixingpay.datas.node.core.db.utils.meta.DdlUtilsFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.ddlutils.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public abstract class AbstractDbDialect implements DbDialect {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractDbDialect.class);
    protected int                      databaseMajorVersion;
    protected int                      databaseMinorVersion;
    protected String                   databaseName;
    protected SqlTemplate              sqlTemplate;
    protected JdbcTemplate             jdbcTemplate;
    protected TransactionTemplate      transactionTemplate;
    protected LobHandler               lobHandler;
    protected final Map<List<String>, Table> tables = new ConcurrentHashMap<>();

    public AbstractDbDialect(final JdbcTemplate jdbcTemplate, LobHandler lobHandler){
        this.jdbcTemplate = jdbcTemplate;
        this.lobHandler = lobHandler;
        // 初始化transction
        this.transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(new DataSourceTransactionManager(jdbcTemplate.getDataSource()));
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        // 初始化一些数据
        jdbcTemplate.execute(new ConnectionCallback() {

            public Object doInConnection(Connection c) throws SQLException, DataAccessException {
                DatabaseMetaData meta = c.getMetaData();
                databaseName = meta.getDatabaseProductName();
                databaseMajorVersion = meta.getDatabaseMajorVersion();
                databaseMinorVersion = meta.getDatabaseMinorVersion();

                return null;
            }
        });

        if (jdbcTemplate instanceof JdbcTransactionTemplate) {
            ((JdbcTransactionTemplate) jdbcTemplate).setTransactionTemplate(this.transactionTemplate);
        }
    }

    public AbstractDbDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler, String name, int majorVersion,
                             int minorVersion){
        this.jdbcTemplate = jdbcTemplate;
        this.lobHandler = lobHandler;
        // 初始化transction
        this.transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(new DataSourceTransactionManager(jdbcTemplate.getDataSource()));
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        this.databaseName = name;
        this.databaseMajorVersion = majorVersion;
        this.databaseMinorVersion = minorVersion;

        if (jdbcTemplate instanceof JdbcTransactionTemplate) {
            ((JdbcTransactionTemplate) jdbcTemplate).setTransactionTemplate(this.transactionTemplate);
        }
    }

    public Table findTable(String schema, String table, boolean useCache) {
        List<String> key = Arrays.asList(schema, table);
        if (useCache == false) {
            tables.remove(key);
        }
        Table findTable = tables.containsKey(key) ? tables.get(key) : tables.putIfAbsent(key, initTables(schema, table));
        findTable = null == findTable ? tables.get(key) : findTable;
        return findTable;
    }

    public Table findTable(String schema, String table) {
        return findTable(schema, table, true);
    }

    public void reloadTable(String schema, String table) {
        if (StringUtils.isNotEmpty(table)) {
            List<String> key = Arrays.asList(schema, table);
            findTable(schema, table, false);
        } else {
            Set<List<String>> keys = tables.keySet();
            for (List<String> key : keys) {
                findTable(key.get(0), key.get(1), false);
            }
        }
    }

    public String getName() {
        return databaseName;
    }

    public int getMajorVersion() {
        return databaseMajorVersion;
    }

    @Override
    public int getMinorVersion() {
        return databaseMinorVersion;
    }

    public String getVersion() {
        return databaseMajorVersion + "." + databaseMinorVersion;
    }

    public LobHandler getLobHandler() {
        return lobHandler;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public SqlTemplate getSqlTemplate() {
        return sqlTemplate;
    }

    public boolean isDRDS() {
        return false;
    }

    public String getShardColumns(String schema, String table) {
        return null;
    }

    public void destory() {
    }


    private Table initTables(String schema, String tableName) {
        try {
            beforeFindTable(jdbcTemplate, schema, schema, tableName);
            DdlUtilsFilter filter = getDdlUtilsFilter(jdbcTemplate, schema, schema, tableName);
            Table table = DdlUtils.findTable(jdbcTemplate, schema, schema, tableName, filter);
            afterFindTable(table, jdbcTemplate, schema, schema, tableName);
            if (table == null) {
                throw new NestableRuntimeException("no found table [" + schema + "." + tableName
                        + "] , pls check");
            }
            return table;
        } catch (Exception e) {
            throw new NestableRuntimeException("find table [" + schema + "." + tableName + "] error",
                    e);
        }
    }

    protected DdlUtilsFilter getDdlUtilsFilter(JdbcTemplate jdbcTemplate, String catalogName, String schemaName,
                                               String tableName) {
        // we need to return null for backward compatibility
        return null;
    }

    protected void beforeFindTable(JdbcTemplate jdbcTemplate, String catalogName, String schemaName, String tableName) {
        // for subclass to extend
    }

    protected void afterFindTable(Table table, JdbcTemplate jdbcTemplate, String catalogName, String schemaName,
                                  String tableName) {
        // for subclass to extend
    }
}
