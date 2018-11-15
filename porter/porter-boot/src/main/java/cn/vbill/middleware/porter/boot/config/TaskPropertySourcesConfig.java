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

package cn.vbill.middleware.porter.boot.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 采用实现XXXXAware接口的方式是因为@Autowired时机晚于PropertySourcesPlaceholderConfigurer对象构造
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 14:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月27日 14:07
 */

@Configuration
public class TaskPropertySourcesConfig implements EnvironmentAware, ResourceLoaderAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPropertySourcesConfig.class);

    /**
     * placeHolderConfigurer
     *
     * @date 2018/8/9 下午3:11
     * @param: []
     * @return: org.springframework.context.support.PropertySourcesPlaceholderConfigurer
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setEnvironment(environment);
        String[] activesProfiles = environment.getActiveProfiles();
        for (String active : activesProfiles) {
            List<File> filesOfActive = configFiles(active);
            List<Resource> resources = new ArrayList<>();
            if (null != filesOfActive && !filesOfActive.isEmpty()) {
                for (File file : filesOfActive) {
                    resources.add(new FileSystemResource(file.getPath()));
                }
            }
            configurer.setLocations(resources.toArray(new Resource[]{}));
        }
        return configurer;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * configFiles
     *
     * @date 2018/8/9 下午3:11
     * @param: [active]
     * @return: java.util.List<java.io.File>
     */
    private List<File> configFiles(String active) {
        List<File> files = new ArrayList<>();
        String[] path = new String[]{"file:./tasks/" + active, "file:./config/tasks/" + active, "classpath:/tasks/" + active};
        for (String p : path) {
            Resource resource = null;
            File[] tasks = null;
            try {
                resource =  resourceLoader.getResource(p);
                File taskDir = resource.getFile();
                tasks = taskDir.listFiles(pathname -> pathname.isFile() && pathname.getName().endsWith(".properties"));
            } catch (IOException e) {
                LOGGER.warn("从以下目录{}找不到默认本地任务配置文件", StringUtils.join(path, ","));
            }
            if (null != tasks && tasks.length > 0) {
                files.addAll(Arrays.asList(tasks));
            }
        }
        return files;
    }
}
