package com.suixingpay.datas.common.cluster.zookeeper;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:01
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.cluster.AbstractClient;
import com.suixingpay.datas.common.cluster.ClusterDriver;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
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
    public void doConnect() throws IOException {
        zk = new ZooKeeper(driver.getUrl(), sessionTimeout, watcher);
    }

    @Override
    public void doDisconnect() throws InterruptedException {
        zk.close();
    }

    @Override
    protected boolean doIsConnected() {
        return null != zk;
    }

    public void setWatcher(Watcher watcher) {
        this.watcher = watcher;
    }

    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren(path,true);
        return children;
    }
    public String getData(String path) throws KeeperException, InterruptedException {
        byte[] dataBytes = zk.getData(path, true, new Stat());
        if (null != dataBytes) {
            return  new String(dataBytes);
        }
        return null;
    }
}
