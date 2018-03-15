package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.core.entity.DataSourcePlugin;
import com.suixingpay.datas.manager.web.page.Page;

import java.util.List;

/**
 * 数据源信息关联表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
public interface DataSourcePluginService {

    Integer insert(DataSourcePlugin dataSourcePlugin);

    Integer update(Long id, DataSourcePlugin dataSourcePlugin);

    Integer delete(Long id);

    DataSourcePlugin selectById(Long id);

    Page<DataSourcePlugin> page(Page<DataSourcePlugin> page);

    void insertSelective(DataSource dataSource);

    List<DataSourcePlugin> findListBySourceID(Long sourceId);
}
