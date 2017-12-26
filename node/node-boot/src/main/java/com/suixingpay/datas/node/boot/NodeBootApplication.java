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
import com.suixingpay.datas.common.cluster.command.NodeRegisterCommand;
import com.suixingpay.datas.common.util.ApplicationContextUtils;
import com.suixingpay.datas.common.util.ProcessUtils;
import com.suixingpay.datas.node.boot.config.DataDriverConfig;
import com.suixingpay.datas.node.boot.config.NodeConfig;
import com.suixingpay.datas.node.boot.config.TaskConfig;
import com.suixingpay.datas.node.core.connector.ConnectorContext;
import com.suixingpay.datas.node.task.TaskController;
import com.suixingpay.datas.node.task.extract.extractor.Extractor;
import com.suixingpay.datas.node.task.extract.extractor.ExtractorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * node launcher
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 16:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月13日 16:24
 */
@ComponentScan({"com.suixingpay"})
@SpringBootApplication
public class NodeBootApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeBootApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(NodeBootApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setWebEnvironment(false);
        ConfigurableApplicationContext context = app.run(args);
        //注入spring工具类
        ApplicationContextUtils.INSTANCE.init(context);
        //获取公用数据库连接池
        DataDriverConfig dataDriverConfig = context.getBean(DataDriverConfig.class);
        if (DataDriverConfig.error(dataDriverConfig)) {
            LOGGER.error("公用数据库连接池初始化失败", new Exception("公用数据库连接池仅支持Mysql、Oracle, 数据同步节点退出!"));
            System.exit(-1);
        }
        //初始化连接池
        ConnectorContext.INSTANCE.initialize(dataDriverConfig.getDrivers());

        //获取集群配置信息
        ClusterDriver driver = context.getBean(ClusterDriver.class);
        if (ClusterDriver.error(driver)) {
            LOGGER.error("集群参数初始化失败", new Exception("集群配置参数ClusterDriver初始化失败, 数据同步节点退出!"));
            System.exit(-1);
        }
        //初始化集群提供者中间件,spring spi插件
        ClusterProvider.load(driver);
        LOGGER.info("建群.......");
        //节点初始化
        NodeConfig nodeConfig = context.getBean(NodeConfig.class);
        //节点注册
        try {
            //注册节点，注册失败退出进程
            ClusterProvider.sendCommand(new NodeRegisterCommand(nodeConfig.getId()));
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage() + "," + "数据同步节点退出!", e);
            System.exit(-1);
        }
        LOGGER.info("加入群聊.......");
        //获取任务配置
        TaskConfig taskConfig = context.getBean(TaskConfig.class);
        //监工上线
        LOGGER.info("监工上线.......");
        TaskController controller = context.getBean(TaskController.class);
        controller.start(taskConfig.getItems());
        LOGGER.info("NodeBootApplication started");

        //保持进程持续运行不退出
        ProcessUtils.keepRunning();
    }
}