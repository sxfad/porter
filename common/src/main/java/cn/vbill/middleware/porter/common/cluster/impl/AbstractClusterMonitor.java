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

package cn.vbill.middleware.porter.common.cluster.impl;

import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.ClusterListener;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.ClusterMonitor;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.command.*;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 14:47
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 14:47
 */
public abstract class AbstractClusterMonitor implements ClusterMonitor {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final Map<String, ClusterListener> listeners = new LinkedHashMap<>();
    protected final Map<ClusterListenerEventType, List<ClusterListenerEventExecutor>> eventWatchdog = new ConcurrentHashMap<>();
    private ClusterClient client;

    @Override
    public void stop() {
        try {
            //最后的清除任务
            ClusterProviderProxy.INSTANCE.broadcastEvent(new ShutdownCommand());
        } catch (Exception e) {
            logger.warn("停止集群监听失败", e);
        }
    }

    @Override
    public void start() throws InterruptedException {
        initiate();
        //监听器初始化
        listeners.forEach((k, v) -> {
            try {
                v.start();
            } catch (Exception e) {
                logger.warn("集群监听器启动失败", e);
            }
        });
    }


    protected void initiate() throws InterruptedException {
        try {
            if (!getClient().isExists(AbstractClusterListener.PREFIX_ATALOG, false)) {
                getClient().createRoot(AbstractClusterListener.PREFIX_ATALOG, false);
            }
            if (!getClient().isExists(AbstractClusterListener.BASE_CATALOG, false)) {
                getClient().createRoot(AbstractClusterListener.BASE_CATALOG, false);
            }
            for (ClusterListener listener : listeners.values()) {
                try {
                    logger.info("init:{},watch:{}", listener.listenPath(), listener.watchListenPath());
                    getClient().createRoot(listener.listenPath(), false);
                    logger.info("attempted create node:{}", listener.listenPath());
                    //只有该ClusterListener监听path变化时才会触发triggerTreeEvent
                    if (listener.watchListenPath()) {
                        logger.info("trigger children watch event:{}", listener.listenPath());
                        //watch children changed
                        triggerWatch(listener.listenPath());
                    }
                } catch (Throwable e) {
                    logger.warn("初始化节点{}监听失败", (null != listener ? listener.listenPath() : "---"), e);
                }
            }
        } catch (Throwable e) {
            logger.error("initiate ClusterMonitor error", e);
        }
    }



    /**
     * doStart
     */
    protected abstract void triggerWatch(String treeNodePath);

    @Override
    public void onEvent(ClusterTreeNodeEvent e) {
        if (null != e && null != listeners && !listeners.isEmpty()) {
            for (ClusterListener listener : listeners.values()) {
                ClusterListenerFilter filter = listener.filter();
                if (null == filter || filter.onFilter(e)) {
                    listener.onEvent(e);
                }
            }
        }
    }

    @Override
    public Map<String, ClusterListener> getListener() {
        return Collections.unmodifiableMap(listeners);
    }

    @Override
    public void setClient(ClusterClient client) {
        this.client = client;
    }

    public ClusterClient getClient() {
        return client;
    }

    public void registerListener(List<ClusterListener> target) {
        //添加SPI到监听器
        target.forEach(listener -> {
            listener.setClient(client);
            listeners.put(listener.getName(), listener);
            List<ClusterListenerEventExecutor> executors = listener.watchedEvents();
            if (null != executors) {
                executors.forEach(e -> registerClusterEvent(e));
            }
        });
    }

    public void registerClusterEvent(ClusterListenerEventExecutor eventExecutor) {
        if (null != eventExecutor && null == eventExecutor.getClient()) eventExecutor.bind(client);
        eventWatchdog.computeIfAbsent(eventExecutor.getBindEvent(), clusterListenerEventType -> new ArrayList<>());
        eventWatchdog.get(eventExecutor.getBindEvent()).add(eventExecutor);
    }

    @Override
    public void noticeClusterListenerEvent(ClusterCommand command) {
        eventWatchdog.getOrDefault(command.getClusterListenerEventType(), Collections.emptyList())
                .forEach(l -> l.execute(command));
    }
}
