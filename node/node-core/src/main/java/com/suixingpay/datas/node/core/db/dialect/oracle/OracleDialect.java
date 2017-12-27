/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 14:10
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.dialect.oracle;

import com.suixingpay.datas.node.core.db.dialect.AbstractDbDialect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 * oracle特殊处理定义
 */
public class OracleDialect extends AbstractDbDialect {

    public OracleDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler){
        super(jdbcTemplate, lobHandler);
        sqlTemplate = new OracleSqlTemplate();
    }

    public OracleDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler, String name, int majorVersion,
                         int minorVersion){
        super(jdbcTemplate, lobHandler, name, majorVersion, minorVersion);
        sqlTemplate = new OracleSqlTemplate();
    }

    public boolean isCharSpacePadded() {
        return true;
    }

    public boolean isCharSpaceTrimmed() {
        return false;
    }

    public boolean isEmptyStringNulled() {
        return true;
    }

    public boolean storesUpperCaseNamesInCatalog() {
        return true;
    }

    public boolean isSupportMergeSql() {
        return true;
    }

    public String getDefaultCatalog() {
        return null;
    }

    public String getDefaultSchema() {
        return (String) jdbcTemplate.queryForObject("SELECT sys_context('USERENV', 'CURRENT_SCHEMA') FROM dual",
                                                    String.class);
    }

}
