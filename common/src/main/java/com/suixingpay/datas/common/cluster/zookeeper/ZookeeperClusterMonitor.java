/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.zookeeper;

import com.suixingpay.datas.common.cluster.*;
import com.suixingpay.datas.common.cluster.command.ShutwdownCommand;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.event.EventType;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 18:22
 */
public class ZookeeperClusterMonitor implements ClusterMonitor, Watcher {
    private ZookeeperClient client;
    private Map<String, ClusterListener> listeners;
    private Map<String,List<String>> nodeChildren;
    private final CountDownLatch ready;
    public ZookeeperClusterMonitor(){
        listeners = new LinkedHashMap<>();
        nodeChildren = new HashMap<>();
        ready = new CountDownLatch(1);
    }

    @Override
    public void addListener(ClusterListener listener) {
        ZookeeperClusterListener zkListener = (ZookeeperClusterListener)listener;
        listeners.put(zkListener.path(),listener);
    }

    @Override
    public void setClient(Client client) {
        this.client = (ZookeeperClient) client;
        this.client.setWatcher(this);
    }

    @Override
    public void onEvent(ClusterEvent e) {
        if (null == e || null == listeners || listeners.isEmpty()) return;
        for (ClusterListener listener : listeners.values()){
            ClusterListenerFilter filter = listener.filter();
            if (null == filter || filter.onFilter(e)) listener.onEvent(e);
        }
    }

    @Override
    public Map<String, ClusterListener> getListener() {
        return Collections.unmodifiableMap(listeners);
    }

    @Override
    public void start() {
        try {
            ready.await();
            Stat preStat = client.exists(ZookeeperClusterListener.PREFIX_ATALOG, false);
            if (null == preStat) {
                client.create(ZookeeperClusterListener.PREFIX_ATALOG,false,"{}");
            }
            Stat stat = client.exists(ZookeeperClusterListener.BASE_CATALOG, false);
            if (null == stat) {
                client.create(ZookeeperClusterListener.BASE_CATALOG,false,"{}");
            }
            for (ClusterListener listener : listeners.values()){
                ZookeeperClusterListener zkListener = (ZookeeperClusterListener)listener;
                Stat listenerStat = client.exists(zkListener.path(), false);
                if (null == listenerStat) {
                    client.create(zkListener.path(), false,"{}");
                }
                //watch children changed
                client.getChildren(zkListener.path());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        try {
            //最后的清除任务
            ClusterProvider.sendCommand(new ShutwdownCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onEvent(List<ClusterEvent> events) {
        if (null != events && !events.isEmpty()) {
            for (ClusterEvent localEvent : events) {
                onEvent(localEvent);
            }
        }
    }


    /**
     * NodeCreated  A watch is set with a call to exists.
     * NodeDeleted A watch is set with a call to either exists or getData.
     * NodeDataChanged A watch is set with either exists or getData.
     * NodeChildrenChanged A watch is set with getChildren.
     * @param event
     */
    @Override
    public synchronized void process(WatchedEvent event) {
        List<ClusterEvent> localEvent = new ArrayList<>();
        //zookeeper event
        Event.EventType eventType = event.getType();
        String path = event.getPath();
        Event.KeeperState state = event.getState();
        //将WatchedEvent转换为ClusterEvent
        try {
            //连接建立
            if (eventType == Event.EventType.None && state == Event.KeeperState.SyncConnected && path == null) {
                ready.countDown();
            } else if (eventType == Event.EventType.NodeDeleted) {
                localEvent.add(new ZookeeperClusterEvent(EventType.OFFLINE, null, path));
            } else if (eventType == Event.EventType.NodeCreated) {
                localEvent.add(new ZookeeperClusterEvent(EventType.ONLINE, client.getData(path).getLeft(), path));
            } else if (eventType == Event.EventType.NodeDataChanged) {
                localEvent.add(new ZookeeperClusterEvent(EventType.DATA_CHANGED, client.getData(path).getLeft(), path));
            } else if (eventType == Event.EventType.NodeChildrenChanged) { //子节点创建、删除会触发该事件
                //构造子节点集合
                List<String> localChildren = null;
                if (nodeChildren.containsKey(path)) {
                    localChildren = nodeChildren.get(path);
                } else {
                    localChildren = new ArrayList<>();
                    nodeChildren.put(path, localChildren);
                }

                //create a watch event:NodeChildrenChanged
                List<String> remoteChildren = client.getChildren(path);

                //判断是否新节点创建
                for (String child : remoteChildren) {
                    String childFullPath = path + "/" +child;
                    //new node
                    if (! localChildren.contains(childFullPath)) {
                        localChildren.add(childFullPath);
                        //only for trigger watcher "getChildren"
                        client.getChildren(childFullPath);
                        localEvent.add(new ZookeeperClusterEvent(EventType.ONLINE, client.getData(childFullPath).getLeft(), childFullPath));
                    }
                }

            }
        } catch (Exception e) {
            //do something
        }
        //拿到事件，根据事件类型发送通知
        onEvent(localEvent);
    }
}