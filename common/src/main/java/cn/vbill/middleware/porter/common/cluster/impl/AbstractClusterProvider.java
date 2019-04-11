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
import cn.vbill.middleware.porter.common.lock.DistributedLock;
import cn.vbill.middleware.porter.common.lock.SupportDistributedLock;
import cn.vbill.middleware.porter.common.cluster.ClusterListener;
import cn.vbill.middleware.porter.common.cluster.ClusterMonitor;
import cn.vbill.middleware.porter.common.cluster.ClusterProvider;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.config.ClusterConfig;
import cn.vbill.middleware.porter.common.cluster.dic.ClusterPlugin;
import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.exception.ClientMatchException;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.task.event.TaskEventListener;
import cn.vbill.middleware.porter.common.task.event.TaskEventProvider;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 集群提供者
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:32
 */
public abstract class AbstractClusterProvider implements ClusterProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClusterProvider.class);
    private final AtomicBoolean status = new AtomicBoolean(false);

    /**
     * getMatchType
     * @return
     */
    protected abstract ClusterPlugin getMatchType();

    /**
     * newMonitor
     *
     * @return
     */
    protected abstract ClusterMonitor newMonitor();

    /**
     * getClusterListenerClass
     *
     * @return
     */
    protected abstract Class getClusterListenerClass();

    /**
     * initClient
     * @param clusterConfig
     * @return
     * @throws ConfigParseException
     */
    protected abstract ClusterClient initClient(ClusterConfig clusterConfig) throws ConfigParseException, ClientException;

    private ClusterClient client;
    private ClusterMonitor monitor;
    private DistributedLock lock;

    @Override
    public boolean matches(ClusterPlugin type) {
        return type == getMatchType();
    }

    @Override
    public void addTaskEventListener(TaskEventListener listener) {
        for (ClusterListener clusterListener : monitor.getListener().values()) {
            if (clusterListener instanceof TaskEventProvider) {
                TaskEventProvider teProvider = (TaskEventProvider) clusterListener;
                teProvider.addTaskEventListener(listener);
            }
        }
    }

    @Override
    public void removeTaskEventListener(TaskEventListener listener) {
        for (ClusterListener clusterListener : monitor.getListener().values()) {
            if (clusterListener instanceof TaskEventProvider) {
                TaskEventProvider teProvider = (TaskEventProvider) clusterListener;
                teProvider.removeTaskEventListener(listener);
            }
        }
    }

    @Override
    public void broadcastEvent(ClusterCommand command) {
        monitor.noticeClusterListenerEvent(command);
    }

    @Override
    public void registerClusterEvent(ClusterListenerEventExecutor eventExecutor) {
        monitor.registerClusterEvent(eventExecutor);
    }

    @Override
    public void broadcastEvent(BiConsumer<ClusterCommand, ClusterClient> block, ClusterCommand command) {
        block.accept(command, client);
    }

    @Override
    public void broadcastEvent(Consumer<ClusterClient> block) {
        block.accept(client);
    }

    @Override
    public void start(ClusterConfig config) throws Exception {
        if (status.compareAndSet(false, true)) {
            initialize(config);
            client.start();
            monitor.start();
        }
    }

    @Override
    public void stop() {
        if (status.compareAndSet(true, false)) {
            try {
                monitor.stop();
            } finally {
                if (null != client) {
                    try {
                        client.shutdown();
                    } catch (Exception e) {
                        LOGGER.warn("停止集群Provider", e);
                    }
                }
            }
        }
    }

    /**
     * initialize
     *
     * @param config
     * @throws ClientException
     * @throws IOException
     * @throws ConfigParseException
     */
    private void initialize(ClusterConfig config) throws ClientException, ConfigParseException {
        client = initClient(config);
        if (null == client || !(client instanceof ClusterClient)) {
            throw new ClientMatchException();
        }
        monitor = newMonitor();
        monitor.setClient(client);
        //通过spring框架的SPI loader加载服务
        monitor.registerListener(SpringFactoriesLoader.loadFactories(getClusterListenerClass(), JavaFileCompiler.getInstance()));
        //初始化集群分布式锁功能
        if (client instanceof SupportDistributedLock) {
            lock = initiateLock(client);
        }
        if (null != lock && lock instanceof ClusterListener) {
            monitor.registerListener((Arrays.asList((ClusterListener) lock)));
        }
    }

    protected abstract DistributedLock initiateLock(ClusterClient client);

    @Override
    public boolean available() {
        return null != client && client.isAlive();
    }

    @Override
    public DistributedLock getLock() {
        if (null == lock) new UnsupportedOperationException("分布式锁功能未提供");
        return lock;
    }
}