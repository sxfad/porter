/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.entity.Dictionary;
import cn.vbill.middleware.porter.manager.core.mapper.DictionaryMapper;
import cn.vbill.middleware.porter.manager.service.DictionaryService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if (iskey) {
                Map<String, Object> childMap = map.get(dictionary.getDictype());
                childMap.put(dictionary.getCode(), dictionary.getName());
                map.put(dictionary.getDictype(), childMap);
            } else {
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
