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

import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.event.TreeNodeEventType;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterMonitor;
import cn.vbill.middleware.porter.common.cluster.client.ZookeeperClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

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
    private final Map<String, List<String>> nodeChildren = new ConcurrentHashMap<>();
    private final CountDownLatch initiatedStatus = new CountDownLatch(1);

    @Override
    public void setClient(ClusterClient client) {
        super.setClient(client);
        ((ZookeeperClient) getClient()).setWatcher(this);
    }

    protected void initiate() throws InterruptedException {
        initiatedStatus.await();
        super.initiate();
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
        //将WatchedEvent转换为ClusterTreeNodeEvent
        try {
            //连接建立
            if (eventType == Event.EventType.None && state == Event.KeeperState.SyncConnected && path == null) {
                initiatedStatus.countDown();
            } else if (eventType == Event.EventType.NodeDeleted) {
                onEvent(new ClusterTreeNodeEvent(TreeNodeEventType.OFFLINE, null, path));
            } else if (eventType == Event.EventType.NodeCreated) {
                ClusterClient.TreeNode node =  getClient().getData(path);
                onEvent(new ClusterTreeNodeEvent(TreeNodeEventType.ONLINE, null != node ? node.getData() : "", path));
            } else if (eventType == Event.EventType.NodeDataChanged) {
                ClusterClient.TreeNode node =  getClient().getData(path);
                onEvent(new ClusterTreeNodeEvent(TreeNodeEventType.DATA_CHANGED, null != node ? node.getData() : "", path));
            } else if (eventType == Event.EventType.NodeChildrenChanged) { //子节点创建、删除会触发该事件
                triggerWatch(path);
            }
        } catch (Exception e) {
            logger.warn("zookeeper watcher process", e);
        }
    }

    /**
     * triggerTreeEvent
     *
     * @param path
     */
    @Override
    public void triggerWatch(String path) {
        //构造子节点集合
        List<String> localChildren = nodeChildren.computeIfAbsent(path, s -> new ArrayList<>());

        //create a watch event:NodeChildrenChanged
        List<String> remoteChildren = getClient().getChildren(path);

        //判断是否新节点创建
        for (String child : remoteChildren) {
            String childFullPath = path + "/" + child;

            //new node
            if (!localChildren.contains(childFullPath)) {
                triggerWatch(childFullPath);
                //only for trigger watcher "getChildren"
                getClient().getChildren(childFullPath);
                localChildren.add(childFullPath);
                ClusterClient.TreeNode node = getClient().getData(childFullPath);
                onEvent(new ClusterTreeNodeEvent(TreeNodeEventType.ONLINE, null != node ? node.getData() : "", childFullPath));
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
