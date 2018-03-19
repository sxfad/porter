/**
 * 
 */
package com.suixingpay.datas.manager.core.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class DataSourceUtil {

    /**
     * 
     * 创建数据源
     * 
     * @param driverName
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static Connection getConn(String driverName, String url, String username, String password) {
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 
     * 创建数据源
     * 
     * @param driverName
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static Connection getConnection(String driverName, String url, String username, String password)
            throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    /**
     * 
     * 关闭数据源
     * 
     * @param conn
     * @param stmt
     * @param rs
     */
    public static void closed(Connection conn, Statement stmt, ResultSet rs) {
        // 关闭结果集
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 关闭数据库操作对象
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 关闭数据库连接
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
