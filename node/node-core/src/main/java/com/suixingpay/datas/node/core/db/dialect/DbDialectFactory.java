/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.dialect;

import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import com.suixingpay.datas.node.core.datasource.DataSourceFactory;
import com.suixingpay.datas.node.core.db.lob.AutomaticJdbcExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum  DbDialectFactory {
    INSTANCE();
    private static final Logger logger = LoggerFactory.getLogger(DbDialectFactory.class);
    private DbDialectGenerator dbDialectGenerator;
    private final Map<String, DbDialect> dialects = new ConcurrentHashMap<>();

    private DbDialectFactory() {
        dbDialectGenerator = new DbDialectGenerator();
        DefaultLobHandler lobHandler = new DefaultLobHandler();
        lobHandler.setStreamAsLob(true);
        dbDialectGenerator.setDefaultLobHandler(lobHandler);
        OracleLobHandler oracleLobHandler = new OracleLobHandler();
        oracleLobHandler.setNativeJdbcExtractor(new AutomaticJdbcExtractor());
        dbDialectGenerator.setOracleLobHandler(oracleLobHandler);
    }
    public DbDialect getDbDialect(String uniqueId) {
        return getDbDialect(DataSourceFactory.INSTANCE.getDataSource(uniqueId));
    }
    public DbDialect getDbDialect(DataSourceWrapper dataSourceWrapper) {
        String key = dataSourceWrapper.getUniqueId();
        DbDialect dbDialect = dialects.containsKey(key) ? dialects.get(key) : null;
        if (null == dbDialect) {
            synchronized (dialects) {
                dbDialect = dialects.containsKey(key) ? dialects.get(key) : null;
                if (null == dbDialect) {
                    final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceWrapper.getDataSource());
                    dbDialect = (DbDialect)jdbcTemplate.execute(new ConnectionCallback() {
                        public Object doInConnection(Connection c) throws SQLException, DataAccessException {
                            DatabaseMetaData meta = c.getMetaData();
                            String databaseName = meta.getDatabaseProductName();
                            String databaseVersion = meta.getDatabaseProductVersion();
                            int databaseMajorVersion = meta.getDatabaseMajorVersion();
                            int databaseMinorVersion = meta.getDatabaseMinorVersion();
                            DbDialect dialect = dbDialectGenerator.generate(jdbcTemplate,
                                    databaseName,
                                    databaseVersion,
                                    databaseMajorVersion,
                                    databaseMinorVersion);
                            if (dialect == null) {
                                throw new UnsupportedOperationException("no dialect for" + databaseName);
                            }

                            if (logger.isInfoEnabled()) {
                                logger.info(String.format("--- DATABASE: %s, SCHEMA: %s ---",
                                        databaseName,
                                        (dialect.getDefaultSchema() == null) ? dialect.getDefaultCatalog() : dialect.getDefaultSchema()));
                            }

                            return dialect;
                        }
                    });
                    dialects.put(key, dbDialect);
                }
            }
        }
        return dbDialect;
    }

}
