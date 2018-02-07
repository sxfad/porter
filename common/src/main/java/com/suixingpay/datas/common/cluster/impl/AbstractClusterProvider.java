/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.impl;

import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.Client;
import com.suixingpay.datas.common.client.ClusterClient;
import com.suixingpay.datas.common.cluster.ClusterListener;
import com.suixingpay.datas.common.cluster.ClusterMonitor;
import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.command.*;
import com.suixingpay.datas.common.cluster.command.broadcast.*;
import com.suixingpay.datas.common.config.Config;
import com.suixingpay.datas.common.config.ConfigType;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ClientMatchException;
import com.suixingpay.datas.common.task.TaskEventListener;
import com.suixingpay.datas.common.task.TaskEventProvider;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * 集群提供者
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:32
 */
public abstract class AbstractClusterProvider<C extends Client> implements ClusterProvider {
    private final AtomicBoolean status = new AtomicBoolean(false);
    protected abstract ConfigType getMatchType();
    protected abstract ClusterMonitor newMonitor();
    protected abstract Class getClusterListenerClass();
    private C client;
    private ClusterMonitor monitor;

    @Override
    public boolean matches(ConfigType type) {
        return ConfigType.ZOOKEEPER == getMatchType();
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

            if (listener instanceof NodeRegister) {
                ((NodeRegister) listener).register((NodeRegisterCommand) command);
            }

            if (listener instanceof TaskRegister) {
                ((TaskRegister) listener).register((TaskRegisterCommand) command);
            }

            if (listener instanceof Shutdown) {
                ((Shutdown) listener).shutdown((ShutdownCommand) command);
            }

            if (listener instanceof TaskAssigned) {
                ((TaskAssigned) listener).taskAssigned((TaskAssignedCommand) command);
            }

            if (listener instanceof TaskStatUpload) {
                ((TaskStatUpload) listener).uploadStat((TaskStatCommand) command);
            }

            if (listener instanceof TaskStop) {
                ((TaskStop) listener).stopTask((TaskStopCommand) command);
            }

            if (listener instanceof TaskStatQuery) {
                ((TaskStatQuery) listener).queryTaskStat((TaskStatQueryCommand) command);
            }
        }
    }

    @Override
    public void start(Config config) throws IOException, ClientException {
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
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initialize(Config config) throws ClientException, IOException {
        client = (C) AbstractClient.getClient(config);
        if (null == client || ! (client instanceof ClusterClient)) {
            throw new ClientMatchException();
        }
        monitor = newMonitor();
        monitor.setClient(client);
        //通过spring框架的SPI loader加载服务
        List<ClusterListener> listeners = SpringFactoriesLoader.loadFactories(getClusterListenerClass(), null);
        //添加SPI到监听器
        listeners.forEach(new Consumer<ClusterListener>() {
            @Override
            public void accept(ClusterListener listener) {
                listener.setClient(client);
                monitor.addListener(listener);
            }
        });
    }
}
