package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.DicDataSourcePlugin;
import com.suixingpay.datas.manager.web.page.Page;

import java.util.List;

/**
 * 数据源信息字典表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
public interface DicDataSourcePluginService {

    Integer insert(DicDataSourcePlugin dicDataSourcePlugin);

    Integer update(Long id, DicDataSourcePlugin dicDataSourcePlugin);

    Integer delete(Long id);

    DicDataSourcePlugin selectById(Long id);

    Page<DicDataSourcePlugin> page(Page<DicDataSourcePlugin> page);

    List<DicDataSourcePlugin> findByType(String sourceType);
}
