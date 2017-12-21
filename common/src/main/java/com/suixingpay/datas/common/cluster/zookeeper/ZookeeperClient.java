package com.suixingpay.datas.common.cluster.zookeeper;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:01
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.cluster.AbstractClient;
import com.suixingpay.datas.common.cluster.ClusterDriver;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:01
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 17:01
 */
public class ZookeeperClient extends AbstractClient {
    private final  ClusterDriver driver;
    //单位 毫秒
    private final Integer sessionTimeout;
    private ZooKeeper zk;
    private Watcher watcher;
    public ZookeeperClient(ClusterDriver driver) {
        this.driver = driver;
        ZookeeperDriverMeta meta = (ZookeeperDriverMeta) driver.getType().getMeta();
        String timoutStr = driver.getExtendAttr().getOrDefault(meta.SESSION_TIMEOUT, "100000");
        this.sessionTimeout = Integer.parseInt(timoutStr);
    }


    @Override
    protected void doConnect() throws IOException {
        zk = new ZooKeeper(driver.getUrl(), sessionTimeout, watcher);
    }

    @Override
    protected void doDisconnect() throws InterruptedException {
        zk.close();
    }

    @Override
    protected boolean doIsConnected() {
        return null != zk;
    }

    protected void setWatcher(Watcher watcher) {
        this.watcher = watcher;
    }

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

    public String  create(String path, boolean isTemp, String data) throws KeeperException, InterruptedException {
       return zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, isTemp ? CreateMode.EPHEMERAL : CreateMode.PERSISTENT);
    }
    public Stat setData(String path, String data, int version) throws KeeperException, InterruptedException {
        return zk.setData(path, data.getBytes(), version);
    }
    public Stat exists(String path, boolean watch) throws KeeperException, InterruptedException {
        return zk.exists(path, watch);
    }
    public void delete(String path) throws KeeperException, InterruptedException {
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
