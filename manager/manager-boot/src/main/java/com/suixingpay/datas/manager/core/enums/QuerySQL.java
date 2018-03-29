/**
 *
 */
package com.suixingpay.datas.manager.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum QuerySQL {

    MYSQL("MYSQL",
            "SELECT distinct table_schema AS prefixName FROM information_schema.tables WHERE "
                    + "table_schema NOT IN ('test','mysql','information_schema','performance_schema','sys') ORDER BY table_schema,table_rows",
            "SELECT table_schema AS prefixName,table_name AS tableName, CONCAT(table_schema,'.',table_name) AS tableAllName FROM information_schema.tables WHERE "
                    + "table_schema NOT IN ('test','mysql','information_schema','performance_schema','sys') ORDER BY table_schema,table_rows",
            "SELECT column_name AS fieldName FROM information_schema.COLUMNS WHERE lower(CONCAT(table_schema,'.',table_name)) = lower('%s') ORDER BY ordinal_position",
            "com.mysql.cj.jdbc.Driver"),

    ORACLE("ORACLE",
            "select distinct OWNER AS prefixName from all_tables WHERE "
                    + "OWNER NOT LIKE '%SYS%' AND OWNER NOT LIKE 'APEX%' AND OWNER NOT LIKE 'XDB' order by OWNER",
            "select OWNER AS prefixName,TABLE_NAME as tableName, OWNER||'.'||TABLE_NAME AS tableAllName from all_tables WHERE "
                    + "OWNER NOT LIKE '%SYS%' AND OWNER NOT LIKE 'APEX%' AND OWNER NOT LIKE 'XDB' ORDER BY OWNER,TABLE_NAME",
            "select COLUMN_NAME as fieldName from all_TAB_COLUMNS where lower(owner||'.'||TABLE_NAME) = lower('%s') order by COLUMN_ID",
            "oracle.jdbc.driver.OracleDriver");

    /**
     * 数据库类型.
     */
    private final String dbType;

    /**
     * 前缀.
     */
    private final String prefixSql; //select * from (prefixSql) as tab

    /**
     * 表名 sql.
     */
    private final String tablesSql;

    /**
     * 表字段 sql.
     */
    private final String tableFieldsSql;

    /**
     * 数据源.
     */
    private final String driverName;

    public String getDbType() {
        return dbType;
    }

    public String getTablesSql() {
        return tablesSql;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getPrefixSql() {
        return prefixSql;
    }

    public String getTableFieldsSql() {
        return tableFieldsSql;
    }
}
