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

import cn.vbill.middleware.porter.manager.service.DataFieldService;
import cn.vbill.middleware.porter.manager.core.entity.DataField;
import cn.vbill.middleware.porter.manager.core.mapper.DataFieldMapper;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据字段对应表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class DataFieldServiceImpl implements DataFieldService {

    @Autowired
    private DataFieldMapper dataFieldMapper;

    @Override
    public Integer insert(DataField dataField) {
        return dataFieldMapper.insert(dataField);
    }

    @Override
    public Integer update(Long id, DataField dataField) {
        return dataFieldMapper.update(id, dataField);
    }

    @Override
    public Integer delete(Long id) {
        return dataFieldMapper.delete(id);
    }

    @Override
    public DataField selectById(Long id) {
        return dataFieldMapper.selectById(id);
    }

    @Override
    public Page<DataField> page(Page<DataField> page) {
        Integer total = dataFieldMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataFieldMapper.page(page, 1));
        }
        return page;
    }

}
