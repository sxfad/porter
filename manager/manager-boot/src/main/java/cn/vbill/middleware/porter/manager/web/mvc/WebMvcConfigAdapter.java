/**
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

package cn.vbill.middleware.porter.manager.web.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@Order(10)
@Configuration
public class WebMvcConfigAdapter extends WebMvcConfigurerAdapter {

    /**
     * SWAGER_URL_PATTERNS
     */
    public static final String SWAGER_URL_PATTERNS = "/swagger*/**,/v2/api-docs";

    /**
     * LOGIN_URL_PATTERNS
     */
    public static final String LOGIN_URL_PATTERNS = "/manager/login,/manager/register,/manager/checkLoginName";

    /**
     * ALARM_URL_PATTERNS
     */
    public static final String ALARM_URL_PATTERNS = "/alarm/task/check,/manager/ogg/tables";


    /**
     * porter-ui
     */
    public static final String PORTER_UI_URL_PATTERNS = "/static,/login.html,/favicon.ico";

    @Autowired
    private XTokenInterceptor xtokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(xtokenInterceptor).addPathPatterns("/**")
                .excludePathPatterns(ALARM_URL_PATTERNS.split(","))
                .excludePathPatterns(SWAGER_URL_PATTERNS.split(","))
                .excludePathPatterns(LOGIN_URL_PATTERNS.split(","))
                .excludePathPatterns(PORTER_UI_URL_PATTERNS.split(","));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
        super.configureMessageConverters(converters);
    }
}
