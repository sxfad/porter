/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.common.dic.SourceType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<String> lists = null;
        DataSource dataSource = dataSourceService.selectById(sourceId);
        if (dataSource.getDataType() == SourceType.JDBC) {
            List<DataSourcePlugin> dataSourcePlugins = dataSource.getPlugins();
            String url = null;
            String username = null;
            String password = null;
            QuerySQL query = null;
            //根据数据源id获取数据源信息
            for (DataSourcePlugin dataSourcePlugin : dataSourcePlugins) {
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
            DbSelectService dbSelectService = ApplicationContextUtil.getBean("dbJDBC" + query.getDbType() + "SelectService");
            lists = dbSelectService.list(dataSource, new JDBCVo(query.getDriverName(), url, username, password), sql, null);
            return lists;
        } else {
            DbSelectService dbSelectService = ApplicationContextUtil.getBean("db" + dataSource.getDataType() + "SelectService");
            lists = dbSelectService.list(dataSource, null, null, null);
            return lists;
        }
    }

    @Override
    public Page<Object> tableList(Page<Object> page, Long sourceId, String prefix, String tableName) {
        DataSource dataSource = dataSourceService.selectById(sourceId);
        if (dataSource.getDataType() == SourceType.JDBC) {
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
            DbSelectService dbSelectService = ApplicationContextUtil.getBean("dbJDBC" + query.getDbType() + "SelectService");
            Long total = dbSelectService.pageTotal(dataSource, new JDBCVo(query.getDriverName(), url, username, password), sql, prefix, tableName);
            if (total > 0) {
                page.setTotalItems(total);
                List<Object> lists = dbSelectService.page(dataSource, new JDBCVo(query.getDriverName(), url, username, password),
                        page, sql, prefix, tableName);
                page.setResult(lists);
            }
            return page;
        } else {
            DbSelectService dbSelectService = ApplicationContextUtil.getBean("db" + dataSource.getDataType() + "SelectService");
            Long total = dbSelectService.pageTotal(dataSource, null, null, prefix, tableName);
            if (total > 0) {
                page.setTotalItems(total);
                List<Object> lists = dbSelectService.page(dataSource, null, page, null, prefix, tableName);
                page.setResult(lists);
            }
            return page;
        }

    }

    @Override
    public Page<DataTable> dataTableList(Page<DataTable> page) {
        Integer total = dataTableMapper.dataTableAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataTableMapper.dataTablePage(page, 1));
        }
        return page;
    }
}
