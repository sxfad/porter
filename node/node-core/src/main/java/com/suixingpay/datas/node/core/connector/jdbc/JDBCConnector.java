package com.suixingpay.datas.node.core.connector.jdbc;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 14:10
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.HexBin;
import com.suixingpay.datas.node.core.connector.AbstractConnector;
import com.suixingpay.datas.node.core.connector.DataDriver;
import com.suixingpay.datas.node.core.connector.DataDriverType;
import com.suixingpay.datas.node.core.event.SQLExecutor;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 14:10
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 14:10
 */
public class JDBCConnector extends AbstractConnector implements SQLExecutor {
    private DruidDataSource dataSource;
    private final Long maxWait;
    public JDBCConnector(DataDriver driver) {
        super(driver);
        JDBCDriverMeta meta = (JDBCDriverMeta) driver.getType().getMeta();
        String maxWaitStr = driver.getExtendAttr().getOrDefault(meta.MAX_WAIT,"10000");
        maxWait = Long.parseLong(maxWaitStr);
    }

    @Override
    public boolean doIsConnected() {
        return this.dataSource != null && this.dataSource.isEnable();
    }

    @Override
    protected void doDisconnect() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    protected void doConnect() {
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver.getDriverClassName());
        dataSource.setUrl(driver.getUrl());
        dataSource.setUsername(driver.getUserName());
        dataSource.setPassword(driver.getPassword());
        dataSource.setMaxWait(maxWait);
        if (driver.getType().getValue() == DataDriverType.MYSQL_VALUE) {
            dataSource.setValidationQuery("select 1");
        } else if (driver.getType().getValue() == DataDriverType.ORACLE_VALUE) {
            dataSource.setValidationQuery("select 1 from dual");
        }
        dataSource.setDefaultAutoCommit(false);
    }

    @Override
    public int update(String updateSQL) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        int affectRow = 0;
        try{
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(updateSQL);
            affectRow = ps.executeUpdate();
            connection.commit();
        } finally {
          if (null != ps)ps.close();
          if (null != connection)connection.close();
        }
        return affectRow;
    }

    @Override
    public List<Map<String, Triple<String, Class, Object>>> query(String querySQL) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        List<Map<String, Triple<String, Class, Object>>> rows = new ArrayList<>();
        try{
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(querySQL);
            ResultSet rs = ps.executeQuery();
            //获取列数
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();
            while (rs.next()) {
                Map<String, Triple<String, Class, Object>> rowData = new LinkedHashMap<>();
                for (int columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
                    //字段名
                    String columnName = metadata.getColumnName(columnIndex).toUpperCase();
                    Class columnType = null;
                    Object columnValue = null;
                    int type = metadata.getColumnType(columnIndex);
                    if (type == Types.VARCHAR || type == Types.CHAR || type == Types.NVARCHAR || type == Types.NCHAR) {
                        columnValue = rs.getString(columnIndex);
                        columnType = String.class;
                    } else if (type == Types.DATE) {
                        Date date = rs.getDate(columnIndex);
                        columnValue = new java.util.Date(date.getTime());
                        columnType = java.util.Date.class;
                    } else if (type == Types.BIT) {
                        columnValue = rs.getBoolean(columnIndex);
                        columnType = Boolean.class;
                    } else if (type == Types.BOOLEAN) {
                        columnValue = rs.getBoolean(columnIndex);
                        columnType = Boolean.class;
                    } else if (type == Types.TINYINT) {
                        columnValue = (int)rs.getByte(columnIndex);
                        columnType = Integer.class;
                    } else if (type == Types.SMALLINT) {
                        columnValue = (int)rs.getShort(columnIndex);
                        columnType = Integer.class;
                    } else if (type == Types.INTEGER) {
                        columnValue = rs.getInt(columnIndex);
                        columnType = Integer.class;
                    } else if (type == Types.BIGINT) {
                        columnValue = rs.getLong(columnIndex);
                        columnType = Long.class;
                    } else if (type == Types.TIMESTAMP) {
                        columnValue = new java.util.Date(rs.getTimestamp(columnIndex).getTime());
                        columnType = java.util.Date.class;
                    } else if (type == Types.DECIMAL) {
                        columnValue = rs.getBigDecimal(columnIndex);
                        columnType = BigDecimal.class;
                    } else if (type == Types.CLOB) {
                        columnValue = rs.getString(columnIndex);
                        columnType = String.class;
                    } else if (type == Types.JAVA_OBJECT) {
                        columnValue = rs.getObject(columnIndex);
                        columnType = Object.class;
                    } else if (type == Types.LONGVARCHAR) {
                        columnValue = rs.getString(columnIndex);
                        columnType = String.class;
                    } else if (type == Types.NULL) {
                        columnType = String.class;
                    } else {
                        columnType = String.class;
                        Object object = rs.getObject(columnIndex);
                        if (! rs.wasNull()) {
                            if (object instanceof byte[]) {
                                byte[] bytes = (byte[]) object;
                                columnValue = HexBin.encode(bytes);
                            } else {
                                columnValue = String.valueOf(object);
                            }
                        }
                    }
                    //插入一列
                    rowData.put(columnName, new ImmutableTriple<>(columnName, columnType, columnValue));
                }
                rows.add(rowData);
            }
            connection.commit();
        } finally {
            if (null != ps)ps.close();
            if (null != connection)connection.close();
        }
        return  rows;
    }
}
