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

package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.datasource.DataSourceUtil;
import cn.vbill.middleware.porter.manager.core.dto.JDBCVo;
import cn.vbill.middleware.porter.manager.core.entity.DataSource;
import cn.vbill.middleware.porter.manager.exception.BaseException;
import cn.vbill.middleware.porter.manager.exception.ExceptionCode;
import cn.vbill.middleware.porter.manager.service.DbSelectService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@Service("dbJDBCORACLESelectService")
public class DbOracleSelectService implements DbSelectService {

    @Override
    public List<String> list(DataSource dataSource, JDBCVo jvo, String sql, Map<String, Object> map) {
        List<String> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet results = null;
        try {
            connection = DataSourceUtil.getConnection(jvo.getDriverName(), jvo.getUrl(), jvo.getUsername(),
                    jvo.getPassword());
            preparedStatement = connection.prepareStatement(sql);
            results = preparedStatement.executeQuery();
            while (results.next()) {
                String prefixName = results.getString("prefixName");
                list.add(prefixName);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new BaseException(String.valueOf(ExceptionCode.EXCEPTION_DATASOURCE), "数据源连接错误,请获取数据库权限、网络权限", e);
        } finally {
            DataSourceUtil.closed(connection, preparedStatement, results);
        }
        return list;
    }

    @Override
    public Long pageTotal(DataSource dataSource, JDBCVo jvo, String sql, String prefix, String tableName1) {
        Long total = 0L;
        String executeSql = "select count(*) as tatal from (" + sql + ") t where %term";
        StringBuffer termSql = new StringBuffer("1=1");
        if (prefix != null && !prefix.equals("")) {
            termSql.append(" and lower(prefixName) = '" + prefix.toLowerCase() + "' ");
        }
        if (tableName1 != null && !tableName1.equals("")) {
            termSql.append(" and lower(tableName) like '%" + tableName1.toLowerCase() + "%' ");
        }
        executeSql = executeSql.replace("%term", termSql);
        // System.out.println(executeSql);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet results = null;
        try {
            connection = DataSourceUtil.getConnection(jvo.getDriverName(), jvo.getUrl(), jvo.getUsername(),
                    jvo.getPassword());
            preparedStatement = connection.prepareStatement(executeSql);
            results = preparedStatement.executeQuery();
            while (results.next()) {
                total = results.getLong(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new BaseException(String.valueOf(ExceptionCode.EXCEPTION_DATASOURCE), "数据源连接错误,请获取数据库权限、网络权限", e);
        } finally {
            DataSourceUtil.closed(connection, preparedStatement, results);
        }
        return total;
    }

    @Override
    public List<Object> page(DataSource dataSource, JDBCVo jvo, Page<Object> page, String sql, String prefix,
                             String tableName1) {
        int begin = (page.getPageNo() - 1) * page.getPageSize();
        int end = page.getPageNo() * page.getPageSize();
        List<Object> list = new ArrayList<Object>();
        String executeSql = "select * from (select t.*,rownum rn from (" + sql
                + ") t where %term) where rn>? and  rn<=?";
        StringBuffer termSql = new StringBuffer("1=1");
        if (prefix != null && !prefix.equals("")) {
            termSql.append(" and lower(prefixName) = '" + prefix.toLowerCase() + "' ");
        }
        if (tableName1 != null && !tableName1.equals("")) {
            termSql.append(" and lower(tableName) like '%" + tableName1.toLowerCase() + "%' ");
        }
        executeSql = executeSql.replace("%term", termSql);
        // System.out.println(executeSql);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet results = null;
        try {
            connection = DataSourceUtil.getConnection(jvo.getDriverName(), jvo.getUrl(), jvo.getUsername(),
                    jvo.getPassword());
            preparedStatement = connection.prepareStatement(executeSql);
            preparedStatement.setInt(1, begin);
            preparedStatement.setInt(2, end);
            results = preparedStatement.executeQuery();
            while (results.next()) {
                String prefixName = results.getString("prefixName");
                String tableName = results.getString("tableName");
                String tableAllName = results.getString("tableAllName");
                String[] str = {prefixName, tableName, tableAllName};
                list.add(str);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new BaseException(String.valueOf(ExceptionCode.EXCEPTION_DATASOURCE), "数据源连接错误,请获取数据库权限、网络权限", e);
        } finally {
            DataSourceUtil.closed(connection, preparedStatement, results);
        }
        return list;
    }

    @Override
    public List<String> fieldList(DataSource dataSource, JDBCVo jvo, String sql, String tableAllName) {
        sql = sql.replace("%s", tableAllName);
        List<String> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet results = null;
        try {
            connection = DataSourceUtil.getConnection(jvo.getDriverName(), jvo.getUrl(), jvo.getUsername(),
                    jvo.getPassword());
            preparedStatement = connection.prepareStatement(sql);
            results = preparedStatement.executeQuery();
            while (results.next()) {
                String prefixName = results.getString("fieldName");
                list.add(prefixName);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new BaseException(String.valueOf(ExceptionCode.EXCEPTION_DATASOURCE), "数据源连接错误,请获取数据库权限、网络权限", e);
        } finally {
            DataSourceUtil.closed(connection, preparedStatement, results);
        }
        return list;
    }
}
