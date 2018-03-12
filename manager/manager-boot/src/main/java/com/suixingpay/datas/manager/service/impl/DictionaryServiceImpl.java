/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.Dictionary;
import com.suixingpay.datas.manager.core.mapper.DictionaryMapper;
import com.suixingpay.datas.manager.service.DictionaryService;
import com.suixingpay.datas.manager.web.page.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据字典表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public Integer insert(Dictionary dictionary) {
        return dictionaryMapper.insert(dictionary);
    }

    @Override
    public Integer update(Long id, Dictionary dictionary) {
        return dictionaryMapper.update(id, dictionary);
    }

    @Override
    public Integer delete(Long id) {
        return dictionaryMapper.delete(id);
    }

    @Override
    public Dictionary selectById(Long id) {
        return dictionaryMapper.selectById(id);
    }

    @Override
    public Page<Dictionary> page(Page<Dictionary> page) {
        Integer total = dictionaryMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dictionaryMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public Map<String, Map<String, Object>> selectMap() {
        Map<String, Map<String, Object>> map = new HashMap<>();
        List<Dictionary> list = dictionaryMapper.selectAll(1);
        for (Dictionary dictionary : list) {
            Boolean iskey = map.containsKey(dictionary.getDictype());
            if(iskey) {
                Map<String, Object> childMap = map.get(dictionary.getDictype());
                childMap.put(dictionary.getCode(), dictionary.getName());
                map.put(dictionary.getDictype(), childMap);
            }else {
                @SuppressWarnings("serial")
                Map<String, Object> childMap = new HashMap<String, Object>() {
                    {
                        put(dictionary.getCode(), dictionary.getName());
                    }
                };
                map.put(dictionary.getDictype(), childMap);
            }
        }
        return map;
    }
}
