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

package cn.vbill.middleware.porter.common.db;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 11:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 11:45
 */
public class SqlTemplateImpl implements SqlTemplate {

    private static final String DOT = ".";

    public String getSelectSql(String schemaName, String tableName, String[] pkNames, String[] columnNames) {
        StringBuilder sql = new StringBuilder("select ");
        int size = columnNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(appendEscape(columnNames[i])).append((i + 1 < size) ? " , " : "");
        }

        sql.append(" from ").append(getFullName(schemaName, tableName)).append(" where ( ");
        appendColumnEquals(sql, pkNames, "and");
        sql.append(" ) ");
        // 不使用intern，避免方法区内存消耗过多
        return sql.toString().intern();
    }

    public String getUpdateSql(String schemaName, String tableName, String[] pkNames, String[] columnNames) {
        StringBuilder sql = new StringBuilder("update " + getFullName(schemaName, tableName) + " set ");
        appendColumnEquals(sql, columnNames, ",");
        sql.append(" where (");
        appendColumnEquals(sql, pkNames, "and");
        sql.append(")");
        // 不使用intern，避免方法区内存消耗过多
        return sql.toString().intern();
    }

    public String getUpdateSql(String schemaName, String tableName, String[] columns) {
        return getUpdateSql(schemaName, tableName, columns, columns);
    }


    public String getInsertSql(String schemaName, String tableName, String[] allColumns) {
        StringBuilder sql = new StringBuilder("insert into " + getFullName(schemaName, tableName) + "(");
        int size = allColumns.length;
        for (int i = 0; i < size; i++) {
            sql.append(appendEscape(allColumns[i])).append((i + 1 < size) ? "," : "");
        }

        sql.append(") values (");
        appendColumnQuestions(sql, allColumns);
        sql.append(")");
        // intern优化，避免出现大量相同的字符串
        return sql.toString().intern();
    }

    public String getDeleteSql(String schemaName, String tableName, String[] pkNames) {
        StringBuilder sql = new StringBuilder("delete from " + getFullName(schemaName, tableName) + " where ");
        appendColumnEquals(sql, pkNames, "and");
        // intern优化，避免出现大量相同的字符串
        return sql.toString().intern();
    }

    protected String getFullName(String schemaName, String tableName) {
        StringBuilder sb = new StringBuilder();
        if (schemaName != null) {
            sb.append(appendEscape(schemaName)).append(DOT);
        }
        sb.append(appendEscape(tableName));
        return sb.toString().intern();
    }

    @Override
    public String getTruncateSql(String schemaName, String tableName) {
        StringBuilder sql = new StringBuilder("truncate table  " + getFullName(schemaName, tableName));
        return sql.toString().intern();
    }

    @Override
    public String getDataChangedCountSql(String schemaName, String tableName, String autoUpdateColumn) {
        StringBuilder sql = new StringBuilder("select count(1) from ").append(getFullName(schemaName, tableName)).append(" where ");
        sql.append(autoUpdateColumn).append(" >= ? and ").append(autoUpdateColumn).append(" <= ? ");
        return sql.toString().intern();
    }

    // ================ helper method ============

    protected String appendEscape(String columnName) {
        return columnName;
    }

    protected void appendColumnQuestions(StringBuilder sql, String[] columns) {
        int size = columns.length;
        for (int i = 0; i < size; i++) {
            sql.append("?").append((i + 1 < size) ? " , " : "");
        }
    }

    protected void appendColumnEquals(StringBuilder sql, String[] columns, String separator) {
        int size = columns.length;
        for (int i = 0; i < size; i++) {
            sql.append(" ").append(appendEscape(columns[i])).append(" = ").append("? ");
            if (i != size - 1) {
                sql.append(separator);
            }
        }
    }
}
