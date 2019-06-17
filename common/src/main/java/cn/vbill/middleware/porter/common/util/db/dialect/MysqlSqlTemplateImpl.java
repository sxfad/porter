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

package cn.vbill.middleware.porter.common.util.db.dialect;

/**
 * 
 * @author zhangkewei[zhang_kw@suixingpay.com]
 *
 */
public class MysqlSqlTemplateImpl extends GeneralSqlTemplateImpl {


    /**
     * getInsertSql
     * @param schemaName
     * @param tableName
     * @param allColumns
     * @return
     */
    public String getInsertSql(String schemaName, String tableName, String[] allColumns) {
        return getInsertSql("insert ignore", schemaName, tableName, allColumns);
    }

    public String getReplaceSql(String schemaName, String tableName, String[] allColumns) {
        return getInsertSql("replace ", schemaName, tableName, allColumns);
    }

    private String getInsertSql(String insertkey, String schemaName, String tableName, String[] allColumns) {
        StringBuilder sql = new StringBuilder(insertkey + " into " + getFullName(schemaName, tableName) + "(");
        for (int i = 0; i < allColumns.length; i++) {
            sql.append(appendEscape(allColumns[i])).append((i + 1 < allColumns.length) ? "," : "");
        }
        sql.append(") values (");
        appendColumnQuestions(sql, allColumns);
        sql.append(")");
        // intern优化，避免出现大量相同的字符串
        return sql.toString().intern();
    }

}
