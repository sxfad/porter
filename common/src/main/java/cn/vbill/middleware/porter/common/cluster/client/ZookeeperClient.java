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

package cn.vbill.middleware.porter.common.cluster.client;

import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.cluster.config.ZookeeperConfig;
import cn.vbill.middleware.porter.common.lock.SupportDistributedLock;
import cn.vbill.middleware.porter.common.statistics.StatisticClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:37
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:37
 */
public class ZookeeperClient extends AbstractClient<ZookeeperConfig> implements ClusterClient, SupportDistributedLock {
    private volatile ZooKeeper zk;
    private volatile Watcher watcher;
    private volatile StatisticClient statisticClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);

    public ZookeeperClient(ZookeeperConfig config) {
        super(config);
    }

    @Override
    protected void doStart() throws IOException {
        ZookeeperConfig config = getConfig();
        zk = new ZooKeeper(config.getUrl(), config.getSessionTimeout(), watcher);
        if (null != statisticClient) {
            try {
                statisticClient.start();
            } catch (Throwable e) {
                LOGGER.warn("启动StatisticClient出错", e);
            }
        }
    }

    @Override
    protected void doShutdown() throws InterruptedException {
        if (null != zk) {
            zk.close();
        }
        if (null != statisticClient) {
            try {
                statisticClient.shutdown();
            } catch (Throwable e) {
                LOGGER.warn("关闭StatisticClient出错", e);
            }
        }
    }

    @Override
    protected boolean isAlready() {
        return true;
    }

    @Override
    public List<String> getChildren(String path) {
        List<String> children = null;
        try {
            children = zk.getChildren(path, true);
        } catch (InterruptedException e) {
            children = new ArrayList<>();
        } catch (KeeperException e) {
            children = new ArrayList<>();
        }
        return children;
    }

    @Override
    public TreeNode getData(String path)  {
        Stat stat = null;
        String content = null;
        try {
            stat = new Stat();
            byte[] dataBytes = new byte[0];
            dataBytes = zk.getData(path, true, stat);
            content = new String(dataBytes);
        } catch (KeeperException.NoNodeException e) {
            LOGGER.warn("获取{}值失败", path);
            stat = null;
        } catch (KeeperException e) {
            LOGGER.warn("获取{}值失败", path, e);
            stat = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            stat = null;
        }
        return null != stat ? new TreeNode(path, content, LockVersion.newVersion(stat.getVersion())) : null;
    }

    @Override
    public TreeNode  create(String path, boolean isTemp, String data) throws KeeperException, InterruptedException {
        String newPath = zk.create(path, null != data ? data.getBytes() : "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, isTemp
                ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT);
        return StringUtils.isNotBlank(newPath) ? new TreeNode(newPath, data, LockVersion.newVersion(-1)) : null;
    }

    @Override
    public LockVersion setData(String path, String data, LockVersion version) throws KeeperException, InterruptedException {
        Stat stat = zk.setData(path, null != data ? data.getBytes() : "".getBytes(), version.getVersion());
        return null != stat ? LockVersion.newVersion(stat.getVersion()) : null;
    }

    @Override
    public LockVersion exists(String path, boolean watch) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(path, watch);
        return null != stat ? LockVersion.newVersion(stat.getVersion()) : null;
    }

    @Override
    public void delete(String path) {
        try {
            Stat stat = zk.exists(path, false);
            if (null != stat) {
                zk.delete(path, stat.getVersion());
            }
        } catch (Throwable e) {
            LOGGER.error("zookeeper delete error", e);
        }
    }

    /**
     * zk客户端必须是CONNECTED状态，否则将会导致任务同步状态失效
     * @return
     */
    @Override
    public boolean alive() {
        return null != zk && null != zk.getState() && zk.getState().isConnected();
    }

    /**
     * canRestore
     *
     * @return
     */
    private boolean canRestore() {
        return null != zk && zk.getState().isAlive();
    }


    /**
     * zookeeper 链接自旋
     */
    @Override
    public void clientSpinning() {
        ZookeeperConfig config = getConfig();
        if (!alive() && !canRestore()) {
            try {
                zk = new ZooKeeper(config.getUrl(), config.getSessionTimeout(), watcher);
            } catch (IOException e) {
                LOGGER.error("reconnection when zookeeper isn't alive.", e);
                e.printStackTrace();
            }
        }

        int spannedTime = 0;
        while (!alive() && canRestore() && spannedTime < config.getSpinningTime()) {
            try {
                spannedTime += config.getSpinningPeer();
                Thread.currentThread().sleep(config.getSpinningPeer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setStatisticClient(StatisticClient client) {
        statisticClient = client;
    }

    @Override
    public void uploadStatistic(String target, String key, String data) {
        if (null != statisticClient && statisticClient != this) {
            statisticClient.uploadStatistic(target, key, data);
        } else {
            try {
                changeData(target, true, false, data);
            } catch (Throwable e) {
                LOGGER.warn("上传统计信息失败,忽略异常", e);
            }
        }
    }

    @Override
    public void createRoot(String nodePath, boolean isTemp) throws Exception {
        if (!isExists(nodePath, false)) {
            create(nodePath, isTemp, null);
        }
    }

    public void setWatcher(Watcher watcher) {
        this.watcher = watcher;
    }
}
