/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 16:24
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.boot;

import com.suixingpay.datas.common.alert.AlertProviderFactory;
import com.suixingpay.datas.common.client.PublicClientContext;
import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.NodeRegisterCommand;
import com.suixingpay.datas.common.util.compile.JavaFileCompiler;
import com.suixingpay.datas.node.boot.config.NodeConfig;
import com.suixingpay.datas.node.boot.config.SourcesConfig;
import com.suixingpay.datas.node.core.NodeContext;
import com.suixingpay.datas.common.util.ProcessUtils;
import com.suixingpay.datas.node.task.TaskController;
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
@ComponentScan({"com.suixingpay"})
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
        try {
            AlertProviderFactory.INSTANCE.initialize(config.getAlert());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("告警配置初始化失败, 数据同步节点退出!error:" + e.getMessage());
        }


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