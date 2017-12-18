package com.suixingpay.datas.common.cluster.zookeeper;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.cluster.Client;
import com.suixingpay.datas.common.cluster.ClusterListener;
import com.suixingpay.datas.common.cluster.ClusterMonitor;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.event.EventType;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.*;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 18:22
 */
public class ZookeeperClusterMonitor implements ClusterMonitor, Watcher {
    final  String BASE_CATALOG = "/suixingpay/datas";

    private ZookeeperClient client;
    private Map<String, ClusterListener> listeners;
    private Map<String,List<String>> nodeChildren;
    public ZookeeperClusterMonitor(){
        listeners = new LinkedHashMap<>();
        nodeChildren = new HashMap<>();
    }

    @Override
    public void addListener(ClusterListener listener) {
        listeners.put(BASE_CATALOG+listener.path(),listener);
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
            if (listener.filter().onFilter()) listener.onEvent(e);
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

        //将WatchedEvent转换为ClusterEvent
        try {
            if (eventType == Event.EventType.NodeDeleted) {
                localEvent.add(new ClusterEvent(EventType.OFFLINE,client.getData(path)));
            } else if (eventType == Event.EventType.NodeCreated) {
                localEvent.add(new ClusterEvent(EventType.ONLINE,client.getData(path)));
            } else if (eventType == Event.EventType.NodeDataChanged) {
                localEvent.add(new ClusterEvent(EventType.DATA_CHANGED,client.getData(path)));
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
                         localEvent.add(new ClusterEvent(EventType.ONLINE,client.getData(childFullPath)));
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
