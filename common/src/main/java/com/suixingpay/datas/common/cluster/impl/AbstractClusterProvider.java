/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.impl;

import com.suixingpay.datas.common.client.Client;
import com.suixingpay.datas.common.client.ClusterClient;
import com.suixingpay.datas.common.cluster.ClusterListener;
import com.suixingpay.datas.common.cluster.ClusterMonitor;
import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.dic.ClusterPlugin;
import com.suixingpay.datas.common.cluster.command.NodeOrderPushCommand;
import com.suixingpay.datas.common.cluster.command.TaskPositionQueryCommand;
import com.suixingpay.datas.common.cluster.command.TaskPositionUploadCommand;
import com.suixingpay.datas.common.cluster.command.TaskStoppedByErrorCommand;
import com.suixingpay.datas.common.cluster.command.ClusterCommand;
import com.suixingpay.datas.common.cluster.command.TaskStatCommand;
import com.suixingpay.datas.common.cluster.command.TaskStopCommand;
import com.suixingpay.datas.common.cluster.command.StatisticUploadCommand;
import com.suixingpay.datas.common.cluster.command.TaskPushCommand;
import com.suixingpay.datas.common.cluster.command.TaskStatQueryCommand;
import com.suixingpay.datas.common.cluster.command.NodeRegisterCommand;
import com.suixingpay.datas.common.cluster.command.ShutdownCommand;
import com.suixingpay.datas.common.cluster.command.TaskAssignedCommand;
import com.suixingpay.datas.common.cluster.command.TaskRegisterCommand;
import com.suixingpay.datas.common.cluster.command.broadcast.NodeOrderPush;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskPush;
import com.suixingpay.datas.common.cluster.command.broadcast.StatisticUpload;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskStatQuery;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskStatUpload;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskStop;
import com.suixingpay.datas.common.cluster.command.broadcast.NodeRegister;
import com.suixingpay.datas.common.cluster.command.broadcast.Shutdown;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskAssigned;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskRegister;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskPosition;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskStoppedByError;
import com.suixingpay.datas.common.config.ClusterConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ClientMatchException;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.common.task.TaskEventListener;
import com.suixingpay.datas.common.task.TaskEventProvider;
import com.suixingpay.datas.common.util.compile.JavaFileCompiler;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 集群提供者
 * @param <C>
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:32
 */
public abstract class AbstractClusterProvider<C extends Client> implements ClusterProvider {
    private final AtomicBoolean status = new AtomicBoolean(false);
    protected abstract ClusterPlugin getMatchType();
    protected abstract ClusterMonitor newMonitor();
    protected abstract Class getClusterListenerClass();
    protected abstract C initClient(ClusterConfig clusterConfig) throws ConfigParseException;
    private C client;
    private ClusterMonitor monitor;

    @Override
    public boolean matches(ClusterPlugin type) {
        return ClusterPlugin.ZOOKEEPER == getMatchType();
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
                        e.printStackTrace();
                    }
                }
            }
        }
    }


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
    }

    @Override
    public boolean available() {
        return null != client && client.isAlive();
    }
}