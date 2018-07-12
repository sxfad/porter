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
 *
 */
public interface SqlTemplate {
    String getSelectSql(String schemaName, String tableName, String[] pkNames, String[] columnNames);
    String getUpdateSql(String schemaName, String tableName, String[] pkNames, String[] columnNames);
    String getUpdateSql(String schemaName, String tableName, String[] allColumnNames);
    String getDeleteSql(String schemaName, String tableName, String[] pkNames);

    String getInsertSql(String schemaName, String tableName, String[] columns);
    String getTruncateSql(String schemaName, String tableName);

    String getDataChangedCountSql(String schemaName, String tableName, String autoUpdateColumn);
}
