/**
 *
 */
package com.suixingpay.datas.manager.web.mvc;

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

    public static final String API_PREFIX = "/api/manager";

    public static final String SWAGER_URL_PATTERNS = API_PREFIX+"/swagger*/**,"+API_PREFIX+"/v2/api-docs";

    public static final String LOGIN_URL_PATTERNS = API_PREFIX+"/login,"+API_PREFIX+"/register";

    @Autowired
    private XTokenInterceptor xtokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(xtokenInterceptor).addPathPatterns("/**")
//              .excludePathPatterns(SWAGER_URL_PATTERNS.split(",")).excludePathPatterns(LOGIN_URL_PATTERNS.split(","));
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
