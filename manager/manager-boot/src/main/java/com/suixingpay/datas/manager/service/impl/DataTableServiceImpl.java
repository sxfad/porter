/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.dto.JDBCVo;
import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.core.entity.DataSourcePlugin;
import com.suixingpay.datas.manager.core.entity.DataTable;
import com.suixingpay.datas.manager.core.enums.QuerySQL;
import com.suixingpay.datas.manager.core.mapper.DataTableMapper;
import com.suixingpay.datas.manager.core.util.ApplicationContextUtil;
import com.suixingpay.datas.manager.service.DataSourceService;
import com.suixingpay.datas.manager.service.DataTableService;
import com.suixingpay.datas.manager.service.DbSelectService;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 数据表信息表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class DataTableServiceImpl implements DataTableService {

    @Autowired
    private DataTableMapper dataTableMapper;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public Integer insert(DataTable dataTable) {
        return dataTableMapper.insertSelective(dataTable);
    }

    @Override
    public DataTable selectById(Long id) {
        return dataTableMapper.selectById(id);
    }

    @Override
    public Page<DataTable> page(Page<DataTable> page, String bankName, String beginTime, String endTime) {
        Integer total = dataTableMapper.pageAll(1, bankName, beginTime, endTime);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataTableMapper.page(page, 1, bankName, beginTime, endTime));
        }
        return page;
    }

    @Override
    public Integer delete(Long id) {
        return dataTableMapper.delete(id);
    }

    @Override
    public List<String> prefixList(Long sourceId) {
        DataSource dataSource = dataSourceService.selectById(sourceId);
        List<DataSourcePlugin> list = dataSource.getPlugins();
        String url = null;
        String username = null;
        String password = null;
        QuerySQL query = null;
        //根据数据源id获取数据源信息
        for (DataSourcePlugin dataSourcePlugin : list) {
            if (dataSourcePlugin.getFieldCode().toLowerCase().equals("dbtype")) {
                if (dataSourcePlugin.getFieldValue().toLowerCase().equals("mysql")) {
                    query = QuerySQL.MYSQL;
                }
                if (dataSourcePlugin.getFieldValue().toLowerCase().equals("oracle")) {
                    query = QuerySQL.ORACLE;
                }
            }
            if (dataSourcePlugin.getFieldCode().toLowerCase().equals("url")) {
                url = dataSourcePlugin.getFieldValue();
            }
            if (dataSourcePlugin.getFieldCode().toLowerCase().equals("username")) {
                username = dataSourcePlugin.getFieldValue();
            }
            if (dataSourcePlugin.getFieldCode().toLowerCase().equals("password")) {
                password = dataSourcePlugin.getFieldValue();
            }
        }

        String sql = query.getPrefixSql();
        //根据DbType获取相应的实现类
        DbSelectService dbSelectService = ApplicationContextUtil.getBean("db" + query.getDbType() + "SelectService");
        List<String> lists = dbSelectService.list(new JDBCVo(query.getDriverName(), url, username, password), sql, null);
        return lists;
    }

    @Override
    public Page<Object> tableList(Page<Object> page, Long sourceId, String prefix, String tableName) {
        DataSource dataSource = dataSourceService.selectById(sourceId);
        List<DataSourcePlugin> list = dataSource.getPlugins();
        String url = null;
        String username = null;
        String password = null;
        QuerySQL query = null;
        for (DataSourcePlugin dataSourcePlugin : list) {
            if (dataSourcePlugin.getFieldCode().toLowerCase().equals("dbtype")) {
                if (dataSourcePlugin.getFieldValue().toLowerCase().equals("mysql")) {
                    query = QuerySQL.MYSQL;
                }
                if (dataSourcePlugin.getFieldValue().toLowerCase().equals("oracle")) {
                    query = QuerySQL.ORACLE;
                }
            }
            if (dataSourcePlugin.getFieldCode().toLowerCase().equals("url")) {
                url = dataSourcePlugin.getFieldValue();
            }
            if (dataSourcePlugin.getFieldCode().toLowerCase().equals("username")) {
                username = dataSourcePlugin.getFieldValue();
            }
            if (dataSourcePlugin.getFieldCode().toLowerCase().equals("password")) {
                password = dataSourcePlugin.getFieldValue();
            }
        }
        String sql = query.getTablesSql();
        DbSelectService dbSelectService = ApplicationContextUtil.getBean("db" + query.getDbType() + "SelectService");
        Long total = dbSelectService.pageTotal(new JDBCVo(query.getDriverName(), url, username, password), sql, prefix, tableName);
        if (total > 0) {
            page.setTotalItems(total);
            List<Object> lists = dbSelectService.page(new JDBCVo(query.getDriverName(), url, username, password), page, sql, prefix, tableName);
            page.setResult(lists);
        }
        return page;
    }
}
