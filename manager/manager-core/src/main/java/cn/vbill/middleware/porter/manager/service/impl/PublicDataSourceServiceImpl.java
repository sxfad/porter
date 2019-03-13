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

import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;
import cn.vbill.middleware.porter.manager.core.mapper.PublicDataSourceMapper;
import cn.vbill.middleware.porter.manager.service.PublicDataSourceService;

 /**  
 * 公共数据源配置表 服务实现类
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
@Service
public class PublicDataSourceServiceImpl implements PublicDataSourceService {
	
	@Autowired
	private PublicDataSourceMapper publicDataSourceMapper;
	
	@Override
	public Integer insert(PublicDataSource publicDataSource) {
		return publicDataSourceMapper.insert(publicDataSource);
	}

	@Override
	public Integer update(Long id, PublicDataSource publicDataSource) {
        return publicDataSourceMapper.update(id, publicDataSource);
	}

	@Override
	public Integer delete(Long id) {
        return publicDataSourceMapper.delete(id);
	}

	@Override
	public PublicDataSource selectById(Long id) {
        return publicDataSourceMapper.selectById(id);
	}

	@Override
	public Page<PublicDataSource> page(Page<PublicDataSource> page) {
        Integer total = publicDataSourceMapper.pageAll(1);
        if(total>0) {
			page.setTotalItems(total);
			page.setResult(publicDataSourceMapper.page(page, 1));
        }
        return page;
	}
}
