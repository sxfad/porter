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

package cn.vbill.middleware.porter.common.cluster.impl.zookeeper;

import cn.vbill.middleware.porter.common.cluster.event.EventType;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterMonitor;
import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.client.impl.ZookeeperClient;
import cn.vbill.middleware.porter.common.cluster.ClusterListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 18:22
 */
public class ZookeeperClusterMonitor extends AbstractClusterMonitor implements Watcher {
    private ZookeeperClient client;
    private final Map<String, List<String>> nodeChildren = new ConcurrentHashMap<>();

    private final CountDownLatch ready = new CountDownLatch(1);

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClusterMonitor.class);

    @Override
    public void setClient(Client client) {
        this.client = (ZookeeperClient) client;
        this.client.setWatcher(this);
    }

    @Override
    public void doStart() {
        try {
            ready.await();
            Stat preStat = client.exists(ZookeeperClusterListener.PREFIX_ATALOG, false);
            if (null == preStat) {
                client.create(ZookeeperClusterListener.PREFIX_ATALOG, false, "{}");
            }
            Stat stat = client.exists(ZookeeperClusterListener.BASE_CATALOG, false);
            if (null == stat) {
                client.create(ZookeeperClusterListener.BASE_CATALOG, false, "{}");
            }
            for (ClusterListener listener : listeners.values()) {
                try {
                    ZookeeperClusterListener zkListener = (ZookeeperClusterListener) listener;
                    LOGGER.info("init:{},watch:{}", zkListener.listenPath(), zkListener.watchListenPath());
                    client.createWhenNotExists(zkListener.listenPath(), false, false, "{}");
                    LOGGER.info("attempted create node:{}", zkListener.listenPath());
                    //只有该ClusterListener监听path变化时才会触发triggerTreeEvent
                    if (zkListener.watchListenPath()) {
                        LOGGER.info("trigger children watch event:{}", zkListener.listenPath());
                        //watch children changed
                        triggerTreeEvent(zkListener.listenPath());
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    LOGGER.error("%s", e);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            LOGGER.error("%s", e);
        }
    }

    /**
     * NodeCreated  A watch is set with a call to exists.
     * NodeDeleted A watch is set with a call to either exists or getData.
     * NodeDataChanged A watch is set with either exists or getData.
     * NodeChildrenChanged A watch is set with getChildren.
     *
     * @param event
     */
    @Override
    public synchronized void process(WatchedEvent event) {
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
                onEvent(new ZookeeperClusterEvent(EventType.OFFLINE, null, path));
            } else if (eventType == Event.EventType.NodeCreated) {
                onEvent(new ZookeeperClusterEvent(EventType.ONLINE, client.getData(path).getLeft(), path));
            } else if (eventType == Event.EventType.NodeDataChanged) {
                onEvent(new ZookeeperClusterEvent(EventType.DATA_CHANGED, client.getData(path).getLeft(), path));
            } else if (eventType == Event.EventType.NodeChildrenChanged) { //子节点创建、删除会触发该事件
                triggerTreeEvent(path);
            }
        } catch (Exception e) {
            //do something
            e.printStackTrace();
            LOGGER.error("%s", e);
        }
    }

    /**
     * triggerTreeEvent
     *
     * @param path
     */
    private void triggerTreeEvent(String path) {
        //构造子节点集合
        List<String> localChildren = nodeChildren.computeIfAbsent(path, s -> new ArrayList<>());

        //create a watch event:NodeChildrenChanged
        List<String> remoteChildren = client.getChildren(path);

        //判断是否新节点创建
        for (String child : remoteChildren) {
            String childFullPath = path + "/" + child;

            //new node
            if (!localChildren.contains(childFullPath)) {
                triggerTreeEvent(childFullPath);
                //only for trigger watcher "getChildren"
                client.getChildren(childFullPath);
                localChildren.add(childFullPath);
                onEvent(new ZookeeperClusterEvent(EventType.ONLINE, client.getData(childFullPath).getLeft(), childFullPath));
            }
        }

        //删除旧节点
        List<String> needRemoves = localChildren.stream().filter(s -> {
            String remotePath = s.replace(path + "/", "");
            return !remoteChildren.contains(remotePath);
        }).collect(Collectors.toList());
        localChildren.removeAll(needRemoves);
    }
}
