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

import cn.vbill.middleware.porter.manager.core.entity.DataAuthority;
import cn.vbill.middleware.porter.manager.core.mapper.DataAuthorityMapper;
import cn.vbill.middleware.porter.manager.service.DataAuthorityService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据权限控制表 服务实现类
 *
 * @author: FairyHood
 * @date: 2019-03-28 15:21:58
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-28 15:21:58
 */
@Service
public class DataAuthorityServiceImpl implements DataAuthorityService {

    @Autowired
    private DataAuthorityMapper dataAuthorityMapper;

    @Override
    public Integer insert(DataAuthority dataAuthority) {
        return dataAuthorityMapper.insert(dataAuthority);
    }

    @Override
    public Integer update(Long id, DataAuthority dataAuthority) {
        return dataAuthorityMapper.update(id, dataAuthority);
    }

    @Override
    public Integer delete(Long id) {
        return dataAuthorityMapper.delete(id);
    }

    @Override
    public DataAuthority selectById(Long id) {
        return dataAuthorityMapper.selectById(id);
    }

    @Override
    public Page<DataAuthority> page(Page<DataAuthority> page) {
        Integer total = dataAuthorityMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataAuthorityMapper.page(page, 1));
        }
        return page;
    }

}
