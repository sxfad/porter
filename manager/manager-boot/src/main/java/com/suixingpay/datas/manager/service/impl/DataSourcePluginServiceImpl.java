package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.DataSourcePlugin;
import com.suixingpay.datas.manager.core.mapper.DataSourcePluginMapper;
import com.suixingpay.datas.manager.service.DataSourcePluginService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据源信息关联表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
@Service
public class DataSourcePluginServiceImpl implements DataSourcePluginService {

    @Autowired
    private DataSourcePluginMapper dataSourcePluginMapper;

    @Override
    public Integer insert(DataSourcePlugin dataSourcePlugin) {
        return dataSourcePluginMapper.insert(dataSourcePlugin);
    }

    @Override
    public Integer update(Long id, DataSourcePlugin dataSourcePlugin) {
        return dataSourcePluginMapper.update(id, dataSourcePlugin);
    }

    @Override
    public Integer delete(Long id) {
        return dataSourcePluginMapper.delete(id);
    }

    @Override
    public DataSourcePlugin selectById(Long id) {
        return dataSourcePluginMapper.selectById(id);
    }

    @Override
    public Page<DataSourcePlugin> page(Page<DataSourcePlugin> page) {
        Integer total = dataSourcePluginMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataSourcePluginMapper.page(page, 1));
        }
        return page;
    }
}
