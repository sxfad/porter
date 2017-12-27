/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.dialect;

import org.apache.ddlutils.model.Table;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.support.TransactionTemplate;


public interface DbDialect {

    public String getName();

    public String getVersion();

    public int getMajorVersion();

    public int getMinorVersion();

    public String getDefaultSchema();

    public String getDefaultCatalog();

    public boolean isCharSpacePadded();

    public boolean isCharSpaceTrimmed();

    public boolean isEmptyStringNulled();

    public boolean isSupportMergeSql();

    public boolean isDRDS();

    public LobHandler getLobHandler();

    public JdbcTemplate getJdbcTemplate();

    public TransactionTemplate getTransactionTemplate();

    public SqlTemplate getSqlTemplate();

    public Table findTable(String schema, String table);

    public Table findTable(String schema, String table, boolean useCache);

    public String getShardColumns(String schema, String table);

    public void reloadTable(String schema, String table);

    public void destory();
}
