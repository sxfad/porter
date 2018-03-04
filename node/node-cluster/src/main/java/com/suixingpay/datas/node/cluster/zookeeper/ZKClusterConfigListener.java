/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.cluster.zookeeper;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.alert.AlertProviderFactory;
import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import com.suixingpay.datas.common.config.AlertConfig;
import com.suixingpay.datas.common.config.LogConfig;
import com.suixingpay.datas.common.config.StatisticConfig;
import com.suixingpay.datas.common.exception.ClientConnectionException;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.node.core.NodeContext;
import org.slf4j.LoggerFactory;

/**
 * 节点监听
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterConfigListener extends ZookeeperClusterListener {
    private static final String ZK_PATH = BASE_CATALOG + "/config";
    private static final String LOG_CONFIG_PATH = ZK_PATH + "/log";
    private static final String ALERT_CONFIG_PATH = ZK_PATH + "/alert";
    private static final String STATISTIC_CONFIG_PATH = ZK_PATH + "/statistic";

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        LOGGER.info("集群配置参数监听:{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        if (zkEvent.isDataChanged() || zkEvent.isOnline()) {
            //日志
            if (zkEvent.getPath().equals(LOG_CONFIG_PATH)) {
                //转换配置参数问java对象
                LogConfig config = JSONObject.parseObject(event.getData(), LogConfig.class);
                //获取Logger上下文
                LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                //设置日志级别
                loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.toLevel(config.getLevel(), Level.INFO));
            }
            //告警
            if (zkEvent.getPath().equals(ALERT_CONFIG_PATH)) {
                AlertConfig config = JSONObject.parseObject(event.getData(), AlertConfig.class);
                try {
                    AlertProviderFactory.INSTANCE.initialize(config);
                } catch (ConfigParseException e) {
                    e.printStackTrace();
                } catch (ClientConnectionException e) {
                    e.printStackTrace();
                }
            }
            if (zkEvent.getPath().equals(STATISTIC_CONFIG_PATH)) {
                StatisticConfig config = JSONObject.parseObject(event.getData(), StatisticConfig.class);
                NodeContext.INSTANCE.syncUploadStatistic(config.isUpload());
            }
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ZookeeperClusterListenerFilter() {
            @Override
            protected String getPath() {
                return listenPath();
            }
            @Override
            protected boolean doFilter(ZookeeperClusterEvent event) {
                //应用自身，跳过
                return !event.getPath().equals(getPath() + "/" + NodeContext.INSTANCE.getNodeId());
            }
        };
    }
}