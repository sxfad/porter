/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.core.mapper.DataSourceMapper;
import com.suixingpay.datas.manager.service.DataSourceService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        //创建人
        //dataSource.setCreater(1L);
        return dataSourceMapper.insertSelective(dataSource);
    }

    @Override
    public Integer update(Long id, DataSource dataSource) {
        return dataSourceMapper.updateSelective(id, dataSource);
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
    public Page<DataSource> page(Page<DataSource> page, String name, String beginTime, String endTime) {
        Integer total = dataSourceMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataSourceMapper.page(page, 1, name, beginTime, endTime));
        }
        return page;
    }
}
