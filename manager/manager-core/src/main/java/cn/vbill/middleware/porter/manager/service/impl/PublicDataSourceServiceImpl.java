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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.config.DataLoaderConfig;
import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;
import cn.vbill.middleware.porter.manager.core.mapper.PublicDataSourceMapper;
import cn.vbill.middleware.porter.manager.service.PublicDataSourceService;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 公共数据源配置表 服务实现类
 * 
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
@Service
public class PublicDataSourceServiceImpl implements PublicDataSourceService {

    private Logger logger = LoggerFactory.getLogger(PublicDataSourceServiceImpl.class);

    @Autowired
    private PublicDataSourceMapper publicDataSourceMapper;

    @Override
    public Integer insert(PublicDataSource publicDataSource) {
        //等权限类代码
        publicDataSource.setCreator(-1L);
        DataLoaderConfig config = JSONObject.parseObject(publicDataSource.getJsonText(), DataLoaderConfig.class);
        publicDataSource.setCode(config.getLoaderName());
        return publicDataSourceMapper.insert(publicDataSource);
    }

    @Override
    public Integer update(Long id, PublicDataSource publicDataSource) {
        //等权限类代码
        publicDataSource.setCreator(-1L);
        DataLoaderConfig config = JSONObject.parseObject(publicDataSource.getJsonText(), DataLoaderConfig.class);
        publicDataSource.setCode(config.getLoaderName());
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
    public Page<PublicDataSource> page(Page<PublicDataSource> page, Long id, String code, String name) {
        Integer total = publicDataSourceMapper.pageAll(id, code, name);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(publicDataSourceMapper.page(page, id, code, name));
        }
        return page;
    }

    @Override
    public Integer updateCancel(Long id) {
        return publicDataSourceMapper.updateCancel(id);
    }

    @Override
    public DataLoaderConfig dealxml(String xmlTextStr) {
        DataLoaderConfig config = new DataLoaderConfig();
        try {
            Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(xmlTextStr.getBytes()));
            PropertiesConfigurationFactory<DataLoaderConfig> factory = new PropertiesConfigurationFactory<>(config);
            MutablePropertySources sources = new MutablePropertySources();
            sources.addFirst(new PropertiesPropertySource(UUID.randomUUID().toString(), properties));
            factory.setPropertySources(sources);
            // factory.setTargetName("porter.task[0]");
            factory.bindPropertiesToTarget();
        } catch (IOException e) {
            logger.error("解析jobXmlText失败IOException，请注意！！", e);
        } catch (BindException e) {
            logger.error("解析jobXmlText失败BindException，请注意！！", e);
        }
        return config;
    }

}
