/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2018-03-07 13:40:30  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.mapper.DataSourceMapper;
import com.suixingpay.datas.manager.service.DataSourceService;

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

    @Override
    public Integer insert(DataSource dataSource) {
        return dataSourceMapper.insert(dataSource);
    }

    @Override
    public Integer update(Long id, DataSource dataSource) {
        return dataSourceMapper.update(id, dataSource);
    }

    @Override
    public Integer delete(Long id) {
        return dataSourceMapper.delete(id);
    }

    @Override
    public DataSource selectById(Long id) {
        return dataSourceMapper.selectById(id);
    }

    @Override
    public Page<DataSource> page(Page<DataSource> page) {
        Integer total = dataSourceMapper.pageAll(1);
        if(total>0) {
            page.setTotalItems(total);
            page.setResult(dataSourceMapper.page(page, 1));
        }
        return page;
    }

}
