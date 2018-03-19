package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.DicDataSourcePlugin;
import com.suixingpay.datas.manager.core.mapper.DicDataSourcePluginMapper;
import com.suixingpay.datas.manager.service.DicDataSourcePluginService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据源信息字典表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
@Service
public class DicDataSourcePluginServiceImpl implements DicDataSourcePluginService {

    @Autowired
    private DicDataSourcePluginMapper dicDataSourcePluginMapper;

    @Override
    public Integer insert(DicDataSourcePlugin dicDataSourcePlugin) {
        return dicDataSourcePluginMapper.insert(dicDataSourcePlugin);
    }

    @Override
    public Integer update(Long id, DicDataSourcePlugin dicDataSourcePlugin) {
        return dicDataSourcePluginMapper.update(id, dicDataSourcePlugin);
    }

    @Override
    public Integer delete(Long id) {
        return dicDataSourcePluginMapper.delete(id);
    }

    @Override
    public DicDataSourcePlugin selectById(Long id) {
        return dicDataSourcePluginMapper.selectById(id);
    }

    @Override
    public Page<DicDataSourcePlugin> page(Page<DicDataSourcePlugin> page) {
        Integer total = dicDataSourcePluginMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dicDataSourcePluginMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public List<DicDataSourcePlugin> findByType(String sourceType) {
        return dicDataSourcePluginMapper.findByType(sourceType);
    }
}
