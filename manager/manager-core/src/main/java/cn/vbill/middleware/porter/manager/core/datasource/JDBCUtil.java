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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class JDBCUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JDBCUtil.class);

    /**
     * jdbcPage
     */
    public static void jdbcPage() {
        // String driverName = "com.mysql.cj.jdbc.Driver";
        // String url =
        // "jdbc:mysql://172.16.60.247:3306/ds_data?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        // String username = "fd";
        // String password = "123456";

        String driverName = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@172.16.135.252:1521/BAPDB";
        String username = "rcsl";
        String password = "rcsl_sit";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet results = null;
        try {
            connection = DataSourceUtil.getConnection(driverName, url, username, password);
            preparedStatement = connection.prepareStatement("SELECT 1 AS id FROM DUAL");
            results = preparedStatement.executeQuery();
            while (results.next()) {
                String id = results.getString(1);
//                System.out.println(id);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtil.closed(connection, preparedStatement, results);
        }

    }

//    public static void main(String[] args) {
//        JDBCUtil.jdbcPage();
//    }
}
