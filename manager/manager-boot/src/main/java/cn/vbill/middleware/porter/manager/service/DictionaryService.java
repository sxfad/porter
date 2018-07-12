package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.Dictionary;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.Map;

/**
 * 数据字典表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DictionaryService {

    Integer insert(Dictionary dictionary);

    Integer update(Long id, Dictionary dictionary);

    Integer delete(Long id);

    Dictionary selectById(Long id);

    Page<Dictionary> page(Page<Dictionary> page);

    Map<String, Map<String, Object>> selectMap();
}
