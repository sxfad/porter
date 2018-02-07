/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.db;

public interface SqlTemplate {
    public String getSelectSql(String schemaName, String tableName, String[] pkNames, String[] columnNames);
    public String getUpdateSql(String schemaName, String tableName, String[] pkNames, String[] columnNames);
    public String getUpdateSql(String schemaName, String tableName, String[] allColumnNames);
    public String getDeleteSql(String schemaName, String tableName, String[] pkNames);

    public String getInsertSql(String schemaName, String tableName, String[] columns);
    public String getTruncateSql(String schemaName, String tableName);

    public String getDataChangedCountSql(String schemaName, String tableName, String autoUpdateColumn);
}
