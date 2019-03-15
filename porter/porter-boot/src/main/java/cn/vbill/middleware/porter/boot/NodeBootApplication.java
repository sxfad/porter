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

package cn.vbill.middleware.porter.boot;

import cn.vbill.middleware.porter.boot.helper.GCHelper;
import cn.vbill.middleware.porter.common.client.PublicClientContext;
import cn.vbill.middleware.porter.common.dic.AlertPlugin;
import cn.vbill.middleware.porter.boot.config.SourcesConfig;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.task.TaskController;
import cn.vbill.middleware.porter.common.alert.AlertProviderFactory;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.event.command.NodeRegisterCommand;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import cn.vbill.middleware.porter.boot.config.NodeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.net.MalformedURLException;

/**
 * node launcher
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 16:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月13日 16:24
 */
@ComponentScan({"cn.vbill"})
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
public class NodeBootApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeBootApplication.class);

    /**
     * node launcher
     *
     * @date 2018/8/9 下午3:10
     * @param: [args]
     * @return: void
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NodeBootApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext context = app.run(args);
        NodeContext.INSTANCE.startupArgs(args);
        //注入spring工具类
        NodeContext.INSTANCE.setApplicationContext(context);

        LOGGER.info("loading 3rd libraries.......");
        //扫描plugins目录，加载插件
        try {
            JavaFileCompiler.getInstance().loadPlugin();
        } catch (MalformedURLException e) {
            System.exit(-1);
            LOGGER.error("fail to load 3rd libraries", e);
            System.exit(-1);
        }

        LOGGER.info("initiating system properties.......");
        //获取配置类
        NodeConfig config = context.getBean(NodeConfig.class);
        //从本地初始化告警配置
        if (null != config.getAlert() && AlertPlugin.NONE != config.getAlert().getStrategy()) {
            try {
                AlertProviderFactory.INSTANCE.initialize(config.getAlert());
            } catch (Exception e) {
                LOGGER.error("fail to initiate alert properties", e.getMessage());
                System.exit(-1);
            }
        }

        //初始化默认工作任务数
        NodeContext.INSTANCE.updateWorkLimit(config.getWorkLimit());
        //从本地初始化公用数据库连接池
        SourcesConfig datasourceConfigBean = context.getBean(SourcesConfig.class);
        try {
            PublicClientContext.INSTANCE.initialize(datasourceConfigBean.getConfig());
        } catch (Exception e) {
            LOGGER.error("fail to initiate public jdbc properties", e);
            System.exit(-1);
        }

        LOGGER.info("initiating cluster properties........");
        //初始化集群提供者中间件,spring spi插件
        try {
            //获取集群配置信息
            ClusterProviderProxy.INSTANCE.initialize(config.getCluster());
            NodeContext.INSTANCE.workMode(config.getCluster().getStrategy());
        } catch (Throwable e) {
            try {
                ClusterProviderProxy.INSTANCE.stop();
            } catch (Throwable stopError) {
            }
            System.exit(-1);
            LOGGER.error("fail to initiate cluster properties", e);
        }

        //节点注册
        LOGGER.info("joining into cluster register.......");
        try {
            //注册节点，注册失败退出进程
            ClusterProviderProxy.INSTANCE.broadcastEvent(new NodeRegisterCommand(config.getId(), config.getStatistic().isUpload()));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("fail to join into cluster register", e);
            System.exit(-1);
        }
        LOGGER.info("joined into cluster register.......");

        //获取任务配置
        //监工上线
        LOGGER.info("initiating task container.......");
        TaskController controller = context.getBean(TaskController.class);
        //启动节点任务执行容器，并尝试执行本地配置文件任务
        controller.start(null != config.getTask() && !config.getTask().isEmpty() ? config.getTask() : null);

        if (config.isGc()) {
            LOGGER.info("running GC Thread.......");
            GCHelper.run(config.getGcDelayOfMinutes());
        }
        LOGGER.info("NodeBootApplication started");
    }
}