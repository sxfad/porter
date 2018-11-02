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

import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.client.DistributedLock;
import cn.vbill.middleware.porter.common.client.SupportDistributedLock;
import cn.vbill.middleware.porter.common.cluster.ClusterListener;
import cn.vbill.middleware.porter.common.cluster.ClusterMonitor;
import cn.vbill.middleware.porter.common.cluster.ClusterProvider;
import cn.vbill.middleware.porter.common.cluster.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.command.ConfigPushCommand;
import cn.vbill.middleware.porter.common.cluster.command.NodeOrderPushCommand;
import cn.vbill.middleware.porter.common.cluster.command.NodeRegisterCommand;
import cn.vbill.middleware.porter.common.cluster.command.ShutdownCommand;
import cn.vbill.middleware.porter.common.cluster.command.StatisticUploadCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskAssignedCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskPositionQueryCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskPositionUploadCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskPushCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskRegisterCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskStatCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskStatQueryCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskStopCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskStoppedByErrorCommand;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.ConfigPush;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.NodeOrderPush;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.NodeRegister;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.Shutdown;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.StatisticUpload;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskAssigned;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskPosition;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskPush;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskRegister;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskStatQuery;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskStatUpload;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskStop;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskStoppedByError;
import cn.vbill.middleware.porter.common.config.ClusterConfig;
import cn.vbill.middleware.porter.common.dic.ClusterPlugin;
import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.exception.ClientMatchException;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.task.TaskEventListener;
import cn.vbill.middleware.porter.common.task.TaskEventProvider;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 集群提供者
 *
 * @param <C>
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:32
 */
public abstract class AbstractClusterProvider<C extends Client> implements ClusterProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClusterProvider.class);
    private final AtomicBoolean status = new AtomicBoolean(false);

    /**
     * getMatchType
     * @return
     */
    protected abstract ClusterPlugin getMatchType();

    /**
     * newMonitor
     * @return
     */
    protected abstract ClusterMonitor newMonitor();

    /**
     * getClusterListenerClass
     * @return
     */
    protected abstract Class getClusterListenerClass();

    /**
     * initClient
     * @param clusterConfig
     * @return
     * @throws ConfigParseException
     */
    protected abstract C initClient(ClusterConfig clusterConfig) throws ConfigParseException, ClientException;

    private C client;
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
    public void broadcastCommand(ClusterCommand command) throws Exception {
        for (ClusterListener listener : monitor.getListener().values()) {
            if (listener instanceof NodeRegister && command instanceof NodeRegisterCommand) {
                ((NodeRegister) listener).nodeRegister((NodeRegisterCommand) command);
            }

            if (listener instanceof TaskRegister && command instanceof TaskRegisterCommand) {
                ((TaskRegister) listener).taskRegister((TaskRegisterCommand) command);
            }

            if (listener instanceof Shutdown && command instanceof ShutdownCommand) {
                ((Shutdown) listener).shutdown((ShutdownCommand) command);
            }

            if (listener instanceof TaskAssigned && command instanceof TaskAssignedCommand) {
                ((TaskAssigned) listener).taskAssigned((TaskAssignedCommand) command);
            }

            if (listener instanceof TaskStatUpload && command instanceof TaskStatCommand) {
                ((TaskStatUpload) listener).uploadStat((TaskStatCommand) command);
            }

            if (listener instanceof TaskStop && command instanceof TaskStopCommand) {
                ((TaskStop) listener).stopTask((TaskStopCommand) command);
            }

            if (listener instanceof TaskStatQuery && command instanceof TaskStatQueryCommand) {
                ((TaskStatQuery) listener).queryTaskStat((TaskStatQueryCommand) command);
            }

            if (listener instanceof StatisticUpload && command instanceof StatisticUploadCommand) {
                ((StatisticUpload) listener).upload((StatisticUploadCommand) command);
            }

            if (listener instanceof TaskPush && command instanceof TaskPushCommand) {
                ((TaskPush) listener).push((TaskPushCommand) command);
            }

            if (listener instanceof NodeOrderPush && command instanceof NodeOrderPushCommand) {
                ((NodeOrderPush) listener).push((NodeOrderPushCommand) command);
            }

            if (listener instanceof TaskStoppedByError && command instanceof TaskStoppedByErrorCommand) {
                ((TaskStoppedByError) listener).tagError((TaskStoppedByErrorCommand) command);
            }

            if (listener instanceof TaskPosition && command instanceof TaskPositionQueryCommand) {
                ((TaskPosition) listener).query((TaskPositionQueryCommand) command);
            }

            if (listener instanceof TaskPosition && command instanceof TaskPositionUploadCommand) {
                ((TaskPosition) listener).upload((TaskPositionUploadCommand) command);
            }

            if (listener instanceof ConfigPush && command instanceof ConfigPushCommand) {
                ((ConfigPush) listener).push((ConfigPushCommand) command);
            }
        }
    }

    @Override
    public void start(ClusterConfig config) throws Exception, ClientException, ConfigParseException {
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
    private void initialize(ClusterConfig config) throws ClientException, IOException, ConfigParseException {
        client = initClient(config);
        if (null == client || !(client instanceof ClusterClient)) {
            throw new ClientMatchException();
        }
        monitor = newMonitor();
        monitor.setClient(client);
        //通过spring框架的SPI loader加载服务
        List<ClusterListener> listeners = SpringFactoriesLoader.loadFactories(getClusterListenerClass(), JavaFileCompiler.getInstance());
        //添加SPI到监听器
        listeners.forEach(listener -> {
            listener.setClient(client);
            monitor.addListener(listener);
        });
        //初始化集群分布式锁功能
        if (lock instanceof SupportDistributedLock) {
            lock = initiateLock((ClusterClient) client);
        }
        if (null != lock && lock instanceof ClusterListener) {
            monitor.addListener((ClusterListener) lock);
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