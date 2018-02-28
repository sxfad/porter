/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 14:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager;

import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.manager.config.ManagerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 14:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 14:09
 */
@ComponentScan({"com.suixingpay"})
@ServletComponentScan
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ManagerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ManagerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext context = app.run(args);
        //注入spring工具类
        ManagerContext.INSTANCE.setApplicationContext(context);
        //获取配置
        ManagerConfig config = context.getBean(ManagerConfig.class);
        try {
            ClusterProviderProxy.INSTANCE.initialize(config.getCluster());
        } catch (Exception e) {
            ClusterProviderProxy.INSTANCE.stop();
            throw new RuntimeException("集群模块初始化失败, 数据同步管理后台退出!error:" + e.getMessage());
        }
    }
}
