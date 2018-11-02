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

package cn.vbill.middleware.porter.cluster.zookeeper;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.common.alert.AlertProviderFactory;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import cn.vbill.middleware.porter.common.config.AlertConfig;
import cn.vbill.middleware.porter.common.config.LogConfig;
import cn.vbill.middleware.porter.common.config.StatisticConfig;
import cn.vbill.middleware.porter.common.exception.ClientConnectionException;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.core.NodeContext;
import org.slf4j.LoggerFactory;

/**
 * 节点监听
 *
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
        logger.info("集群配置参数监听:{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
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
                    logger.warn("解析告警任务配置失败", e);
                } catch (ClientConnectionException e) {
                    logger.warn("告警客户端连接失败", e);
                }
            }
            //统计分析
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