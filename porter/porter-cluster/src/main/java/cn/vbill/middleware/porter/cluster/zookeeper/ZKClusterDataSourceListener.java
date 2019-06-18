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

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.node.statistics.NodeLog;
import cn.vbill.middleware.porter.common.task.config.PublicSourceConfig;
import cn.vbill.middleware.porter.common.task.loader.PublicClientContext;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.task.TaskContext;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 公共数据源监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年06月18日 10:02
 */
public class ZKClusterDataSourceListener extends ZookeeperClusterListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZKClusterDataSourceListener.class);

    private static final String ZK_PATH = BASE_CATALOG + "/datesource";
    private static final Pattern DS_CONFIG_PATH = Pattern.compile(ZK_PATH + "/.*");

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public List<ClusterListenerEventExecutor> watchedEvents() {
        return null;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent event) {
        logger.info("集群配置参数监听:{},{},{}", event.getId(), event.getData(), event.getEventType());
        if (DS_CONFIG_PATH.matcher(event.getId()).matches()) {
            PublicSourceConfig config = event.isOnline() || event.isDataChanged() ? JSONObject.parseObject(event.getData(), PublicSourceConfig.class) : null;
            if (null == config || !config.isUsing()) {
                String code = null != config ? config.getCode() : event.getId().replace(listenPath() + "/", "");
                LOGGER.info("禁用公共数据源:{}", null != event.getData() ? event.getData() : code);
                PublicClientContext.INSTANCE.remove(code);
            } else {
                if (StringUtils.isBlank(config.getNodeId()) || ("," + config.getNodeId() + ",").contains("," + NodeContext.INSTANCE.getNodeId() + ",")) {
                    LOGGER.info("初始化公共数据源开始:{}", event.getData());
                    try {
                        PublicClientContext.INSTANCE.initialize(config.getCode(), SourceConfig.getConfig(config.getSource()));
                    } catch (Throwable e) {
                        LOGGER.error("初始化公共数据源出错:{}", e.getMessage(), e);
                        TaskContext.warning(new NodeLog(NodeLog.LogType.ERROR, e.getMessage()).upload());
                    }
                    LOGGER.info("初始化公共数据源结束:{}", event.getData());
                }
            }
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ClusterListenerFilter() {
            @Override
            public boolean doFilter(ClusterTreeNodeEvent event) {
                return true;
            }

            @Override
            public String getPath() {
                return listenPath();
            }
        };
    }
}