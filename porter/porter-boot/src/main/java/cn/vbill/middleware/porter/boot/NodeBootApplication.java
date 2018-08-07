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

import cn.vbill.middleware.porter.common.client.PublicClientContext;
import cn.vbill.middleware.porter.common.dic.AlertPlugin;
import cn.vbill.middleware.porter.common.util.ProcessUtils;
import cn.vbill.middleware.porter.boot.config.SourcesConfig;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.task.TaskController;
import cn.vbill.middleware.porter.common.alert.AlertProviderFactory;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.command.NodeRegisterCommand;
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

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NodeBootApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext context = app.run(args);

        //注入spring工具类
        NodeContext.INSTANCE.setApplicationContext(context);

        //扫描plugins目录，加载插件
        try {
            JavaFileCompiler.getInstance().loadPlugin();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("初始化插件失败:" + e.getMessage());
        }


        //获取配置类
        NodeConfig config = context.getBean(NodeConfig.class);

        //从本地初始化告警配置
        if (null != config.getAlert() && AlertPlugin.NONE != config.getAlert().getStrategy()) {
            try {
                AlertProviderFactory.INSTANCE.initialize(config.getAlert());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("告警配置初始化失败, 数据同步节点退出!error:" + e.getMessage());
            }
        }

        //初始化默认工作任务数
        NodeContext.INSTANCE.updateWorkLimit(config.getWorkLimit());

        //从本地初始化公用数据库连接池
        SourcesConfig datasourceConfigBean = context.getBean(SourcesConfig.class);
        try {
            PublicClientContext.INSTANCE.initialize(datasourceConfigBean.getConfig());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("公用资源连接SourcesConfig初始化失败, 数据同步节点退出!error:" + e.getMessage());
        }




        //初始化集群提供者中间件,spring spi插件
        try {
            //获取集群配置信息
            ClusterProviderProxy.INSTANCE.initialize(config.getCluster());
        } catch (Exception e) {
            ClusterProviderProxy.INSTANCE.stop();
            e.printStackTrace();
            throw new RuntimeException("集群配置参数ClusterConfig初始化失败, 数据同步节点退出!error:" + e.getMessage());
        }

        //节点注册
        LOGGER.info("建群.......");
        try {
            //注册节点，注册失败退出进程
            ClusterProviderProxy.INSTANCE.broadcast(new NodeRegisterCommand(config.getId(), config.getStatistic().isUpload()));
        } catch (Exception e) {
            throw  new RuntimeException(e.getMessage() + "数据同步节点退出!error:" + e.getMessage());
        }


        LOGGER.info("加入群聊.......");
        //获取任务配置
        //监工上线
        LOGGER.info("监工上线.......");
        TaskController controller = context.getBean(TaskController.class);
        //启动节点任务执行容器，并尝试执行本地配置文件任务
        controller.start(null != config.getTask() && !config.getTask().isEmpty() ? config.getTask() : null);
        LOGGER.info("NodeBootApplication started");
        //保持进程持续运行不退出
        ProcessUtils.keepRunning();
    }
}