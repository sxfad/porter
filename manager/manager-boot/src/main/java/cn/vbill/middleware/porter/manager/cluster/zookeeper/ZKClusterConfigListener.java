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

package cn.vbill.middleware.porter.manager.cluster.zookeeper;

import cn.vbill.middleware.porter.common.alert.AlertProviderFactory;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.executor.TaskPushEventExecutor;
import cn.vbill.middleware.porter.common.config.AlertConfig;
import cn.vbill.middleware.porter.common.cluster.event.command.ConfigPushCommand;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台配置推送
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

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent zkEvent) {
        logger.info("集群配置参数监听:{},{},{}", zkEvent.getId(), zkEvent.getData(), zkEvent.getEventType());
        if (zkEvent.isDataChanged() || zkEvent.isOnline()) {
            if (zkEvent.getId().equals(ALERT_CONFIG_PATH)) {
                AlertConfig config = JSONObject.parseObject(zkEvent.getData(), AlertConfig.class);
                try {
                    AlertProviderFactory.INSTANCE.initialize(config);
                } catch (Throwable e) {
                    logger.warn("告警客户端初始化失败", e);
                }
            }
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ClusterListenerFilter() {
            @Override
            public String getPath() {
                return listenPath();
            }

            @Override
            public boolean doFilter(ClusterTreeNodeEvent event) {
                return true;
            }
        };
    }

    @Override
    public List<ClusterListenerEventExecutor> watchedEvents() {
        List<ClusterListenerEventExecutor> executors = new ArrayList<>();
        //任务上传事件
        executors.add(new TaskPushEventExecutor(this.getClass(), true, true, listenPath()));
        //任务注册
        executors.add(new ClusterListenerEventExecutor(this.getClass(), ClusterListenerEventType.ConfigPush).bind((clusterCommand, client) -> {
            ConfigPushCommand command = (ConfigPushCommand) clusterCommand;
            String configPath = "";
            if (command.getType().isLog()) {
                configPath = LOG_CONFIG_PATH;
            } else if (command.getType().isAlert()) {
                configPath = ALERT_CONFIG_PATH;
            }

            if (!StringUtils.isBlank(configPath)) {
                client.changeData(configPath, false, false, command.render());
            }
        }, client));
        return executors;
    }
}