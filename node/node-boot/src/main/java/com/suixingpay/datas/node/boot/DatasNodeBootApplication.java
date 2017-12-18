package com.suixingpay.datas.node.boot;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 16:24
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.cluster.ClusterDriver;
import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.ClusterType;
import com.suixingpay.datas.common.util.ProcessUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * node launcher
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 16:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月13日 16:24
 */
@ComponentScan({"com.suixingpay"})
@SpringBootApplication
public class DatasNodeBootApplication implements CommandLineRunner{

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasNodeBootApplication.class);

    public static void main(String[] args) throws Exception {

        SpringApplication app = new SpringApplication(DatasNodeBootApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setWebEnvironment(false);
        ConfigurableApplicationContext context = app.run(args);
        //获取driver配置
        ClusterDriver driver = context.getBean(ClusterDriver.class);
        if (null == driver || driver.getType() == null || driver.getType() == ClusterType.UNKNOWN) {
            LOGGER.error("ClusterDriver初始化失败", new Exception("集群配置参数ClusterDriver初始化失败,manager节点退出!"));
            System.exit(-1);
        }
        //初始化集群提供者中间件
        ClusterProvider.load(driver);

    }

    @Override
    public void run(String... args) throws Exception {
        //保持进程持续运行不退出
        ProcessUtils.keepRunning();
    }
}