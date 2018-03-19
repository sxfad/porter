package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 数据源信息表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataSourceService {

    Integer insert(DataSource dataSource);

    Integer update(Long id, DataSource dataSource);

    Integer delete(Long id);

    DataSource selectById(Long id);

    Page<DataSource> page(Page<DataSource> page, String name, String beginTime, String endTime);

}
