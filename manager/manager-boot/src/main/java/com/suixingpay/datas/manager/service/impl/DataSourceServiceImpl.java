/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.core.mapper.DataSourceMapper;
import com.suixingpay.datas.manager.service.DataSourcePluginService;
import com.suixingpay.datas.manager.service.DataSourceService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源信息表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private DataSourcePluginService dataSourcePluginService;

    @Override
    @Transactional
    public Integer insert(DataSource dataSource) {
        //新增数据源信息表
        Integer number = dataSourceMapper.insertSelective(dataSource);
        return number;
    }

    @Override
    public Integer update(Long id, DataSource dataSource) {
        return dataSourceMapper.updateSelective(id, dataSource);
    }

    @Override
    @Transactional
    public Integer delete(Long id) {
        //逻辑删除数据源信息表
        Integer number = dataSourceMapper.delete(id);
        //逻辑删除数据源信息关联表
        dataSourcePluginService.delete(id);

        return number;
    }

    @Override
    public DataSource selectById(Long id) {
        //根据主键查询数据源相信信息
        DataSource dataSource = dataSourceMapper.selectById(id);
        //根据数据源主键查询关联信息
        dataSource.setPlugins(dataSourcePluginService.findListBySourceID(id));

        return dataSource;
    }

    @Override
    public Page<DataSource> page(Page<DataSource> page, String name, String beginTime, String endTime) {
        Integer total = dataSourceMapper.pageAll(1, name, beginTime, endTime);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataSourceMapper.page(page, 1, name, beginTime, endTime));
        }
        return page;
    }
}
