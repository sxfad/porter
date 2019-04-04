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

import cn.vbill.middleware.porter.common.task.config.PublicSourceConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.plugin.config.PluginServiceConfig;
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
        // 等权限类代码
        publicDataSource.setCreator(-1L);
        if (StringUtils.isBlank(publicDataSource.getCode())) {
            PublicSourceConfig config = JSONObject.parseObject(publicDataSource.getJsonText(),
                    PublicSourceConfig.class);
            publicDataSource.setCode(config.getCode());
        }
        return publicDataSourceMapper.insert(publicDataSource);
    }

    @Override
    public Integer update(Long id, PublicDataSource publicDataSource) {
        // 等权限类代码
        publicDataSource.setCreator(-1L);
        if (StringUtils.isBlank(publicDataSource.getCode())) {
            PublicSourceConfig config = JSONObject.parseObject(publicDataSource.getJsonText(),
                    PublicSourceConfig.class);
            publicDataSource.setCode(config.getCode());
        }
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
    public Page<PublicDataSource> page(Page<PublicDataSource> page, Long id, String code, String name, String ipsite) {
        Integer total = publicDataSourceMapper.pageAll(id, code, name, ipsite);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(publicDataSourceMapper.page(page, id, code, name, ipsite));
        }
        return page;
    }

    @Override
    public Integer updateCancel(Long id) {
        return publicDataSourceMapper.updateCancel(id);
    }

    @Override
    public Integer updatePush(Long id, Integer ispush) {
        return publicDataSourceMapper.updatePush(id, ispush);
    }

    @Override
    public PublicSourceConfig dealxml(String xmlTextStr) {
        PublicSourceConfig config = new PublicSourceConfig();
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(xmlTextStr.getBytes()));
            ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
            Binder binder = new Binder(source);
            config = binder.bind("", PublicSourceConfig.class).get();
        } catch (IOException e) {
            logger.error("解析xmlTextStr失败，请注意！！", e);
        }
        return config;
    }

}
