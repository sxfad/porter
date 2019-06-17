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

package cn.vbill.middleware.porter.common.util.db;

/**
 *
 */
public interface SqlTemplate {

    /**
     * getSelectSql
     * @param schemaName
     * @param tableName
     * @param pkNames
     * @param columnNames
     * @return
     */
    String getSelectSql(String schemaName, String tableName, String[] pkNames, String[] columnNames);

    /**
     * getUpdateSql
     * @param schemaName
     * @param tableName
     * @param pkNames
     * @param columnNames
     * @return
     */
    String getUpdateSql(String schemaName, String tableName, String[] pkNames, String[] columnNames);

    /**
     * getUpdateSql
     * @param schemaName
     * @param tableName
     * @param allColumnNames
     * @return
     */
    String getUpdateSql(String schemaName, String tableName, String[] allColumnNames);

    /**
     * getDeleteSql
     * @param schemaName
     * @param tableName
     * @param pkNames
     * @return
     */
    String getDeleteSql(String schemaName, String tableName, String[] pkNames);

    /**
     * getInsertSql
     * @param schemaName
     * @param tableName
     * @param columns
     * @return
     */
    String getInsertSql(String schemaName, String tableName, String[] columns);

    /**
     * getTruncateSql
     * @param schemaName
     * @param tableName
     * @return
     */
    String getTruncateSql(String schemaName, String tableName);

    /**
     * getDataChangedCountSql
     * @param schemaName
     * @param tableName
     * @param autoUpdateColumn
     * @return
     */
    String getDataChangedCountSql(String schemaName, String tableName, String autoUpdateColumn);

    default String getReplaceSql(String schemaName, String tableName, String[] allColumns) {
        throw new UnsupportedOperationException("不支持的方法调用");
    }
}
