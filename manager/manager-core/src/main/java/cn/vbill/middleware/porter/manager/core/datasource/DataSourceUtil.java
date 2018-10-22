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

package cn.vbill.middleware.porter.manager.core.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class DataSourceUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(DataSourceUtil.class);

    /**
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
