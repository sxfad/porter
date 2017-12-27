/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 14:10
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.dialect.oracle;


import com.suixingpay.datas.node.core.db.dialect.AbstractSqlTemplate;

/**
 * oracle生成模板
 */
public class OracleSqlTemplate extends AbstractSqlTemplate {

    private static final String ESCAPE = "\"";

    /**
     * http://en.wikipedia.org/wiki/Merge_(SQL)
     */
    public String getMergeSql(String schemaName, String tableName, String[] keyNames, String[] columnNames,
                              String[] viewColumnNames, boolean includePks) {
        final String aliasA = "a";
        final String aliasB = "b";
        StringBuilder sql = new StringBuilder();

        sql.append("merge /*+ use_nl(a b)*/ into ")
            .append(getFullName(schemaName, tableName))
            .append(" ")
            .append(aliasA);
        sql.append(" using (select ");

        int size = columnNames.length;
        // 构建 (select ? as col1, ? as col2 from dual)
        for (int i = 0; i < size; i++) {
            sql.append("? as " + appendEscape(columnNames[i])).append(" , ");
        }
        size = keyNames.length;
        for (int i = 0; i < size; i++) {
            sql.append("? as " + appendEscape(keyNames[i])).append((i + 1 < size) ? " , " : "");
        }
        sql.append(" from dual) ").append(aliasB);
        sql.append(" on (");

        size = keyNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(aliasA + "." + appendEscape(keyNames[i]))
                .append("=")
                .append(aliasB + "." + appendEscape(keyNames[i]));
            sql.append((i + 1 < size) ? " and " : "");
        }

        sql.append(") when matched then update set ");

        size = columnNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(aliasA + "." + appendEscape(columnNames[i]))
                .append("=")
                .append(aliasB + "." + appendEscape(columnNames[i]));
            sql.append((i + 1 < size) ? " , " : "");
        }

        sql.append(" when not matched then insert (");
        size = columnNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(aliasA + "." + appendEscape(columnNames[i])).append(" , ");
        }
        size = keyNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(aliasA + "." + appendEscape(keyNames[i])).append((i + 1 < size) ? " , " : "");
        }

        sql.append(" ) values (");
        size = columnNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(aliasB + "." + appendEscape(columnNames[i])).append(" , ");
        }
        size = keyNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(aliasB + "." + appendEscape(keyNames[i])).append((i + 1 < size) ? " , " : "");
        }
        sql.append(" )");
        return sql.toString().intern(); // intern优化，避免出现大量相同的字符串
    }

    protected String appendEscape(String columnName) {
        return columnName;
    }

}
