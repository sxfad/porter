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

package cn.vbill.middleware.porter.manager;

import cn.vbill.middleware.porter.manager.config.ManagerConfig;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.manager.helper.ErrorTaskRestartHelper;
import cn.vbill.middleware.porter.manager.helper.GCHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 14:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 14:09
 */
@EnableScheduling
@EnableTransactionManagement
@ServletComponentScan
@SpringBootApplication(scanBasePackages = { "cn.vbill" })
@MapperScan("cn.vbill.middleware.porter.manager.core.mapper")
public class ManagerBootApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerBootApplication.class);

    /**
     * 入口
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ManagerBootApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext context = app.run(args);
        // 注入spring工具类
        ManagerContext.INSTANCE.setApplicationContext(context);
        // 获取配置
        ManagerConfig config = context.getBean(ManagerConfig.class);
        try {
            ClusterProviderProxy.INSTANCE.initialize(config.getCluster());
        } catch (Exception e) {
            ClusterProviderProxy.INSTANCE.stop();
            LOGGER.error("集群模块初始化失败, 数据同步管理后台退出!error:" + e.getMessage());
            throw new RuntimeException("集群模块初始化失败, 数据同步管理后台退出!error:" + e.getMessage());
        }
        if (config.isGc()) {
            GCHelper.run(config.getGcDelayOfMinutes());
        }

        if (config.isTaskRestart()) {
            ErrorTaskRestartHelper.run();
        }

        LOGGER.info("ManagerApplication is success!");
    }
}
