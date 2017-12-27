/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 09:52
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.dialect.mysql;

import com.suixingpay.datas.node.core.db.dialect.AbstractDbDialect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 基于mysql的一些特殊处理定义
 * 
 */
public class MysqlDialect extends AbstractDbDialect {

    private boolean                   isDRDS = false;
    private Map<List<String>, String> shardColumns;

    public MysqlDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler){
        super(jdbcTemplate, lobHandler);
        sqlTemplate = new MysqlSqlTemplate();
    }

    public MysqlDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler, String name, String databaseVersion,
                        int majorVersion, int minorVersion){
        super(jdbcTemplate, lobHandler, name, majorVersion, minorVersion);
        sqlTemplate = new MysqlSqlTemplate();

    }

    public boolean isCharSpacePadded() {
        return false;
    }

    public boolean isCharSpaceTrimmed() {
        return true;
    }

    public boolean isEmptyStringNulled() {
        return false;
    }

    public boolean isSupportMergeSql() {
        return true;
    }

    public String getDefaultSchema() {
        return null;
    }

    public boolean isDRDS() {
        return isDRDS;
    }

    public String getShardColumns(String schema, String table) {
        if (isDRDS()) {
            return shardColumns.get(Arrays.asList(schema, table));
        } else {
            return null;
        }
    }

    public String getDefaultCatalog() {
        return (String) jdbcTemplate.queryForObject("select database()", String.class);
    }

}
