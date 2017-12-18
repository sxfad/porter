package com.suixingpay.datas.manager.web;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年11月13日 11:07
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * TODO
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年11月13日 11:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年11月13日 11:07
 */
@EnableSwagger2
@Configuration
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.suixingpay.datas.manager.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("sxpDS-UI接口")
                .contact(new Contact("张科伟", "", "zhang_kw@suixingpay.com"))
                .version("1.0")
                .description("sxpDS-UI的接口描述")
                .build();
    }
}
