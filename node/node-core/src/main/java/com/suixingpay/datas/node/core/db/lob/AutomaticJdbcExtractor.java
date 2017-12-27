/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.lob;

import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor;

import java.sql.*;
import java.util.Iterator;
import java.util.Map;

/**
 * copy from otter3.0，根据不同的数据源自动选择对应的NativeJdbcExtractor
 * 
 * @author jianghang 2011-10-27 下午03:35:17
 * @version 4.0.0
 */
public class AutomaticJdbcExtractor implements NativeJdbcExtractor {

    private NativeJdbcExtractor defaultJdbcExtractor;
    private Map<String, NativeJdbcExtractor> extractors;
    private NativeJdbcExtractor jdbcExtractor;

    public AutomaticJdbcExtractor(){
        defaultJdbcExtractor =  new SimpleNativeJdbcExtractor();
        jdbcExtractor = defaultJdbcExtractor;
    }

    public boolean isNativeConnectionNecessaryForNativeStatements() {
        return true;
    }

    public boolean isNativeConnectionNecessaryForNativePreparedStatements() {
        return true;
    }

    public boolean isNativeConnectionNecessaryForNativeCallableStatements() {
        return true;
    }

    public Connection getNativeConnection(Connection con) throws SQLException {
        return getJdbcExtractor(con).getNativeConnection(con);
    }

    private synchronized NativeJdbcExtractor getJdbcExtractor(Object o) {
        if (jdbcExtractor == null) {
            String objClass = o.getClass().getName();
            Iterator<String> iterator = extractors.keySet().iterator();

            while (iterator.hasNext()) {
                String classPrefix = iterator.next();

                if (objClass.indexOf(classPrefix) != -1) {
                    jdbcExtractor = (NativeJdbcExtractor) extractors.get(classPrefix);

                    break;
                }
            }

            if (jdbcExtractor == null) {
                jdbcExtractor = defaultJdbcExtractor;
            }
        }

        return jdbcExtractor;
    }

    public Connection getNativeConnectionFromStatement(Statement stmt) throws SQLException {
        return getJdbcExtractor(stmt).getNativeConnectionFromStatement(stmt);
    }

    public Statement getNativeStatement(Statement stmt) throws SQLException {
        return getJdbcExtractor(stmt).getNativeStatement(stmt);
    }

    public PreparedStatement getNativePreparedStatement(PreparedStatement ps) throws SQLException {
        return getJdbcExtractor(ps).getNativePreparedStatement(ps);
    }

    public CallableStatement getNativeCallableStatement(CallableStatement cs) throws SQLException {
        return getJdbcExtractor(cs).getNativeCallableStatement(cs);
    }

    public ResultSet getNativeResultSet(ResultSet rs) throws SQLException {
        return getJdbcExtractor(rs).getNativeResultSet(rs);
    }

    public Map<String, NativeJdbcExtractor> getExtractors() {
        return extractors;
    }

    public void setExtractors(Map<String, NativeJdbcExtractor> extractors) {
        this.extractors = extractors;
    }

    public NativeJdbcExtractor getDefaultJdbcExtractor() {
        return defaultJdbcExtractor;
    }

    public void setDefaultJdbcExtractor(NativeJdbcExtractor defaultJdbcExtractor) {
        this.defaultJdbcExtractor = defaultJdbcExtractor;
    }
}
