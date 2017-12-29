/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:15
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.zookeeper;

import com.suixingpay.datas.common.cluster.*;
import com.suixingpay.datas.common.cluster.command.ClusterCommand;
import com.suixingpay.datas.common.task.TaskEventListener;
import com.suixingpay.datas.common.task.TaskEventProvider;

import java.util.concurrent.CyclicBarrier;

/**
 * zookeeper集群提供者
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:15
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 18:15
 */
public class ZookeeperClusterProvider extends ClusterProvider{
    private ZookeeperClient client;
    private ZookeeperClusterMonitor zkMonitor;
    public ZookeeperClusterProvider() {

    }

    @Override
    protected void addListener(ClusterListener listener) {
        listener.setClient(client);
        zkMonitor.addListener(listener);
    }

    @Override
    public void addTaskEventListener(TaskEventListener listener) {
        for (ClusterListener clusterListener : zkMonitor.getListener().values()) {
            if (clusterListener instanceof TaskEventProvider) {
                TaskEventProvider teProvider = (TaskEventProvider) clusterListener;
                teProvider.addTaskEventListener(listener);
            }
        }
    }

    @Override
    public void doInitialize(ClusterDriver driver) {
        client = new ZookeeperClient(driver);
        zkMonitor = new ZookeeperClusterMonitor();
        zkMonitor.setClient(client);
    }

    @Override
    public void start() {
        if( null != client) {
            client.connect();
        }
        zkMonitor.start();
    }

    @Override
    public void stop() {
        try {
            zkMonitor.stop();
        } finally {
            if( null != client) client.disconnect();
        }
    }

    @Override
    protected void distributeCommand(ClusterCommand command) throws Exception {
        for (ClusterListener listener : zkMonitor.getListener().values()) {
            listener.hobby(command);
        }
    }
}