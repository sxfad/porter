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
package cn.vbill.middleware.porter.manager.cluster.zookeeper;

import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.event.EventType;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class ZKClusterLockServiceImpl extends ZookeeperClusterListener implements ZKClusterLockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKClusterLockServiceImpl.class);

    private static final String ZK_PATH = BASE_CATALOG + "/relock";
    private static final Pattern NODE_LOCK_PATTERN = Pattern.compile(ZK_PATH + "/.*");
    private volatile CountDownLatch countDownLatch;

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        String zkPath = zkEvent.getPath();
        LOGGER.debug("TaskListener:{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        if (NODE_LOCK_PATTERN.matcher(zkPath).matches()) {
            System.out.println(zkPath + "-" + zkEvent.getEventType());
            if (zkEvent.getEventType() == EventType.OFFLINE) {
                this.countDownLatch.countDown();
            }
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ZookeeperClusterListenerFilter() {

            @Override
            protected String getPath() {
                return listenPath();
            }

            @Override
            protected boolean doFilter(ZookeeperClusterEvent event) {
                return true;
            }
        };
    }

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void createLock(String lockName) {
        if (addLock(lockName)) {
            System.out.println("--------------- " + lockName + " 拿到了锁！");
        } else {
            try {
                super.client.exists(ZK_PATH + "/" + lockName, true);
                this.countDownLatch = new CountDownLatch(1);
                countDownLatch.await();
                this.countDownLatch = null;
                this.createLock(lockName);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean addLock(String lockName) {
        Boolean key = false;
        try {
            Stat stat = super.client.exists(ZK_PATH + "/" + lockName, false);
            if (stat == null) {
                super.client.create(ZK_PATH + "/" + lockName, true, "");
                key = true;
            }
        } catch (KeeperException | InterruptedException e) {
            key = false;
        }
        return key;
    }

    @Override
    public void deleteLock(String lockName) {
        super.client.delete(ZK_PATH + "/" + lockName);
        System.out.println("---------------" + lockName + " 放回了锁！");
    }
}
