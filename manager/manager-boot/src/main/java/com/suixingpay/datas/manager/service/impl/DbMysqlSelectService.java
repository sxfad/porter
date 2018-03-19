/**
 * 
 */
package com.suixingpay.datas.manager.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.datasource.DataSourceUtil;
import com.suixingpay.datas.manager.core.dto.JDBCVo;
import com.suixingpay.datas.manager.service.DbSelectService;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@Service("dbMYSQLSelectService")
public class DbMysqlSelectService implements DbSelectService {

    @Override
    public List<String> list(JDBCVo jvo, String sql, Map<String, Object> map) {
//        String driverName = "oracle.jdbc.driver.OracleDriver";
//        String url = "jdbc:oracle:thin:@172.16.135.252:1521/BAPDB";
//        String username = "rcsl";
//        String password = "rcsl_sit";
//        
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet results = null;
//        try {
//            connection = DataSourceUtil.getConnection(driverName, url, username, password);
//            preparedStatement = connection.prepareStatement("SELECT 1 AS id FROM DUAL");
//            results = preparedStatement.executeQuery();
//            while (results.next()) {
//                String id = results.getString(1);
//                System.out.println(id);
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }finally {
//            DataSourceUtil.closed(connection, preparedStatement, results);
//        }
          return null;
    }

    @Override
    public Long pageTotal(JDBCVo jvo, String sql, String prefix, String tableName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Object> page(JDBCVo jvo, Page<Object> page, String sql, String prefix,
            String tableName) {
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
                System.out.println(id);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            DataSourceUtil.closed(connection, preparedStatement, results);
        }
        return null;
    }

}
