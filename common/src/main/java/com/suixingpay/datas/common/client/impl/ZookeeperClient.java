/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:37
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.ClusterClient;
import com.suixingpay.datas.common.config.source.ZookeeperConfig;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:37
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:37
 */
public class ZookeeperClient extends AbstractClient<ZookeeperConfig> implements ClusterClient<Stat> {
    private ZooKeeper zk;
    @Setter private Watcher watcher;

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
        if (null != zk) zk.close();
    }

    @Override
    protected boolean isAlready() {
        return true;
    }

    @Override
    public List<String> getChildren(String path){
        List<String> children = null;
        try {
            children = zk.getChildren(path, true);
        } catch (InterruptedException e) {
            e.printStackTrace();
            children = new ArrayList<>();
        } catch (KeeperException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return new ImmutablePair(new String(dataBytes), stat) ;
    }

    @Override
    public String  create(String path, boolean isTemp, String data) throws KeeperException, InterruptedException {
        return zk.create(path, null != data ? data.getBytes() : "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, isTemp ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT);
    }

    @Override
    public Stat setData(String path, String data, int version) throws KeeperException, InterruptedException {
        return zk.setData(path, null != data ? data.getBytes() : "".getBytes(), version);
    }

    @Override
    public Stat exists(String path, boolean watch) throws KeeperException, InterruptedException {
        return zk.exists(path, watch);
    }

    public void  createWhenNotExists(String path, boolean isTemp, boolean watch,  String data) {
        try {
            Stat stat = exists(path, watch);
            if (null == stat) {
                create(path, isTemp, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(String path) {
        try {
            Stat stat = zk.exists(path, false);
            if (null != stat) {
                zk.delete(path, stat.getVersion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
