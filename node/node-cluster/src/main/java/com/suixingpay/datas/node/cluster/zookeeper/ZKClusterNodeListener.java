/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.cluster.zookeeper;

import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.command.ClusterCommand;
import com.suixingpay.datas.common.cluster.command.NodeRegisterCommand;
import com.suixingpay.datas.common.cluster.command.ShutdownCommand;
import com.suixingpay.datas.common.cluster.command.TaskAssignedCommand;
import com.suixingpay.datas.common.cluster.command.broadcast.NodeRegister;
import com.suixingpay.datas.common.cluster.command.broadcast.Shutdown;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskAssigned;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import com.suixingpay.datas.common.cluster.data.DNode;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 节点监听
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterNodeListener extends ZookeeperClusterListener implements NodeRegister, Shutdown, TaskAssigned {
    private final ScheduledExecutorService heartbeatWorker;
    private static final String ZK_PATH = BASE_CATALOG + "/node";
    private final ReentrantLock lock =  new ReentrantLock();
    private String nodeId;
    public ZKClusterNodeListener() {
        heartbeatWorker = Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("node-heartbeat"));
    }

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        LOGGER.info("集群节点监听->{}:{}",event.getEventType(), event.getData());
    }

    @Override
    public ClusterListenerFilter filter() {

        return new ZookeeperClusterListenerFilter(){
            @Override
            protected String getPath() {
                return listenPath();
            }
            @Override
            protected boolean doFilter(ZookeeperClusterEvent event) {
                //应用自身，跳过
                return ! event.getPath().equals(getPath() + "/" + nodeId);
            }
        };
    }

    @Override
    public void register(NodeRegisterCommand nrCommend) throws Exception {
        nodeId = nrCommend.getId();
        String path = listenPath() + "/" + nrCommend.getId();
        Stat stat = client.exists(path, false);
        if (null == stat) {
            client.create(path,false, new DNode(nodeId).toString());
        } else {
            Pair<String, Stat> dataPair = client.getData(path);
            DNode nodeData = DNode.fromString(dataPair.getLeft(), DNode.class);
            if (DateUtils.addSeconds(nodeData.getHeartbeat(),60*5).before(new Date())) {
                client.setData(path,new DNode(nodeId).toString(), dataPair.getRight().getVersion());
            } else {
                throw  new Exception(path + ",5分钟前已注册:" +  nodeData.toString());
            }
        }
        //心跳定时任务
        heartbeatWorker.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    Stat statInJob = client.exists(path, false);
                    if (null != statInJob) {
                        Pair<String, Stat> dataPair = client.getData(path);
                        DNode nodeData = DNode.fromString(dataPair.getLeft(), DNode.class);
                        nodeData.setHeartbeat(new Date());
                        client.setData(path,nodeData.toString(), dataPair.getRight().getVersion());
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        },10000,10000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void shutdown(ShutdownCommand command) throws KeeperException, InterruptedException {
        client.delete(listenPath() + "/" + nodeId);
        heartbeatWorker.shutdownNow();
    }

    @Override
    public void taskAssigned(TaskAssignedCommand command) throws Exception {
        TaskAssignedCommand assignedCommand =  (TaskAssignedCommand)command;
        String path = listenPath() + "/" + nodeId;
        lock.lock();
        DNode nodeData = DNode.fromString(client.getData(path).getLeft(), DNode.class);
        List<String> resources = nodeData.getTasks().getOrDefault(assignedCommand.getTaskId(), new ArrayList<>());
        resources.add(assignedCommand.getResourceId());
        nodeData.getTasks().put(assignedCommand.getTaskId(), resources);
        Stat nowStat = client.exists(path, true);
        client.setData(path, nodeData.toString(), nowStat.getVersion());
        lock.unlock();
    }
}