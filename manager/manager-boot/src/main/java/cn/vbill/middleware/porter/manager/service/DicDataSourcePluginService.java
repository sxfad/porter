package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.DicDataSourcePlugin;
import cn.vbill.middleware.porter.manager.web.page.Page;

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
