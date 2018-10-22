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

package cn.vbill.middleware.porter.common.client.impl;

import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.client.StatisticClient;
import cn.vbill.middleware.porter.common.config.source.ZookeeperConfig;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
public class ZookeeperClient extends AbstractClient<ZookeeperConfig> implements ClusterClient<Stat>, StatisticClient {
    private volatile ZooKeeper zk;
    @Setter private volatile Watcher watcher;
    private volatile StatisticClient statisticClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);

    public ZookeeperClient(ZookeeperConfig config) {
        super(config);
    }

    @Override
    protected void doStart() throws IOException {
        ZookeeperConfig config = getConfig();
        zk = new ZooKeeper(config.getUrl(), config.getSessionTimeout(), watcher);
    }

    @Override
    protected void doShutdown() throws InterruptedException {
        if (null != zk) {
            zk.close();
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
            e.printStackTrace();
            children = new ArrayList<>();
        } catch (KeeperException e) {
            children = new ArrayList<>();
        }
        return children;
    }

    @Override
    public Pair<String, Stat> getData(String path)  {
        Stat stat = new Stat();
        byte[] dataBytes = new byte[0];
        try {
            dataBytes = zk.getData(path, true, stat);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.interrupted();
            e.printStackTrace();
        }
        return new ImmutablePair(new String(dataBytes), stat);
    }

    @Override
    public String  create(String path, boolean isTemp, String data) throws KeeperException, InterruptedException {
        return zk.create(path, null != data ? data.getBytes() : "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, isTemp
                ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT);
    }

    @Override
    public Stat setData(String path, String data, int version) throws KeeperException, InterruptedException {
        return zk.setData(path, null != data ? data.getBytes() : "".getBytes(), version);
    }

    @Override
    public Stat exists(String path, boolean watch) throws KeeperException, InterruptedException {
        return zk.exists(path, watch);
    }

    /**
     * isExists
     *
     * @param path
     * @param watch
     * @return
     */
    public boolean isExists(String path, boolean watch) {
        try {
            Stat stat = zk.exists(path, watch);
            return null != stat;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * changeData
     *
     * @param path
     * @param isTemp
     * @param watch
     * @param data
     */
    public void  changeData(String path, boolean isTemp, boolean watch,  String data) {
        try {
            Stat stat = exists(path, watch);
            if (null == stat) {
                create(path, isTemp, data);
            } else {
                setData(path, data, stat.getVersion());
            }
        } catch (Throwable e) {
            LOGGER.warn("改变zookeeper节点出错", e);
        }
    }

    /**
     * createWhenNotExists
     *
     * @param path
     * @param isTemp
     * @param watch
     * @param data
     * @return
     */
    public Stat  createWhenNotExists(String path, boolean isTemp, boolean watch,  String data) {
        Stat stat = null;
        try {
            stat = exists(path, watch);
            if (null == stat) {
                create(path, isTemp, data);
            }
        } catch (Throwable e) {
            LOGGER.warn("创建zookeeper节点", e);
        }
        return stat;
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
                Thread.interrupted();
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
                create(target, true, data);
            } catch (Throwable e) {
                LOGGER.warn("上传统计信息失败,忽略异常", e);
            }
        }
    }
}
