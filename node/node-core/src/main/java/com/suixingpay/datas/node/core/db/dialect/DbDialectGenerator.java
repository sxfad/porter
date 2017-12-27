/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 15:16
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.db.dialect;

import com.suixingpay.datas.node.core.db.dialect.mysql.MysqlDialect;
import com.suixingpay.datas.node.core.db.dialect.oracle.OracleDialect;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;

public class DbDialectGenerator {

    protected static final String ORACLE      = "oracle";
    protected static final String MYSQL       = "mysql";

    protected LobHandler          defaultLobHandler;
    protected LobHandler          oracleLobHandler;

    protected DbDialect generate(JdbcTemplate jdbcTemplate, String databaseName, String databaseNameVersion, int databaseMajorVersion, int databaseMinorVersion) {
        DbDialect dialect = null;

        if (StringUtils.startsWithIgnoreCase(databaseName, ORACLE)) { // oracle
            dialect = new OracleDialect(jdbcTemplate,
                oracleLobHandler,
                databaseName,
                databaseMajorVersion,
                databaseMinorVersion);
        } else if (StringUtils.startsWithIgnoreCase(databaseName, MYSQL)) { // for
            dialect = new MysqlDialect(jdbcTemplate,
                defaultLobHandler,
                databaseName,
                databaseNameVersion,
                databaseMajorVersion,
                databaseMinorVersion);
        } else {
            throw new RuntimeException(databaseName + " type is not support!");
        }

        return dialect;
    }

    public void setDefaultLobHandler(LobHandler defaultLobHandler) {
        this.defaultLobHandler = defaultLobHandler;
    }

    public void setOracleLobHandler(LobHandler oracleLobHandler) {
        this.oracleLobHandler = oracleLobHandler;
    }
}
