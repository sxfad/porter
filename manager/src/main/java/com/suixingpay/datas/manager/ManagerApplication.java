package com.suixingpay.datas.manager;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 14:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.cluster.ClusterDriver;
import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.ClusterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class ManagerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerApplication.class);
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ManagerApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext ac = app.run(args);
        //获取driver配置
        ClusterDriver driver = ac.getBean(ClusterDriver.class);
        if (null == driver || driver.getType() == null || driver.getType() == ClusterType.UNKNOWN) {
            LOGGER.error("ClusterDriver初始化失败", new Exception("集群配置参数ClusterDriver初始化失败,manager节点退出!"));
            System.exit(-1);
        }
        //初始化集群提供者中间件
        ClusterProvider.load(driver);

    }
}
