/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
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
