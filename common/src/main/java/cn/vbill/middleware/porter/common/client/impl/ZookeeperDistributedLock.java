/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月30日 11:17
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.client.impl;

import cn.vbill.middleware.porter.common.client.DistributedLock;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.event.EventType;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月30日 11:17
 * @version: V1.0
 * @review: zkevin/2018年10月30日 11:17
 */
public class ZookeeperDistributedLock extends ZookeeperClusterListener implements DistributedLock {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperDistributedLock.class);
    private static final String ZK_PATH = BASE_CATALOG + "/relock";
    private static final String LOCK_ROOT = ZK_PATH + "/";
    private static final Pattern NODE_LOCK_PATTERN = Pattern.compile(LOCK_ROOT + ".*");

    private static final Map<String, CountDownLatch> LATCH = new ConcurrentHashMap<>();
    private static final Map<String, AtomicLong> LATCH_WAIT_COUNT = new ConcurrentHashMap<>();

    private final ZookeeperClient client;

    public ZookeeperDistributedLock(ZookeeperClient client) {
        this.client = client;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        String zkPath = zkEvent.getPath();
        LOGGER.info("TaskListener:{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        if (NODE_LOCK_PATTERN.matcher(zkPath).matches()) {
            if (zkEvent.getEventType() == EventType.OFFLINE) {
                //获取锁资源
                String resource = zkPath.replace(LOCK_ROOT, "");
                LATCH.computeIfPresent(resource, (key, latch) -> {
                    latch.countDown();
                    return latch;
                });
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


    public void lock(String resource) {
        tagResource(resource);
        try {
            lock(resource, false);
        } finally {
            clearResourceTag(resource);
        }
    }


    public void lockInterruptibly(String resource) throws InterruptedException {
        tagResource(resource);
        try {
            lock(resource, true);
        } finally {
            clearResourceTag(resource);
        }
    }

    public boolean tryLock(String resource) {
        return getSignal(resource);
    }

    public boolean tryLock(String resource, long time, TimeUnit unit) throws InterruptedException {
        long timeout = unit.toMillis(time);
        long times = 0;
        boolean lockResult = getSignal(resource);
        while (!lockResult && times <= timeout) {
            times += 200;
            Thread.sleep(200);
            lockResult = getSignal(resource);
        }
        return lockResult;
    }

    public void unlock(String resource) {
        String resourcePath = LOCK_ROOT + resource;
        //判断是否存在锁，并且为当前线程所占
        if (client.isExists(resourcePath, true) && client.getData(resourcePath).getLeft().equals(Thread.currentThread().getId() + "")) {
            client.delete(resourcePath);
        }
    }

    private void lock(String resource, boolean interruptibly) {
        if (!getSignal(resource)) {
            LATCH.computeIfAbsent(resource, key -> {
                return new CountDownLatch(1);
            });
            LATCH.computeIfPresent(resource, new BiFunction<String, CountDownLatch, CountDownLatch>() {
                @Override
                @SneakyThrows(InterruptedException.class)
                public CountDownLatch apply(String s, CountDownLatch latch) {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        if (interruptibly) throw e;
                    }
                    lock(resource, interruptibly);
                    return latch;
                }
            });
        }
    }

    private void tagResource(String resource) {
        LATCH_WAIT_COUNT.computeIfAbsent(resource, key -> new AtomicLong());
        LATCH_WAIT_COUNT.computeIfPresent(resource, (key, value) -> {
            value.incrementAndGet();
            return value;
        });
    }
    private void clearResourceTag(String resource) {
        LATCH_WAIT_COUNT.computeIfPresent(resource, (key, value) -> {
            value.decrementAndGet();
            return value;
        });
        synchronized (LATCH_WAIT_COUNT) {
            AtomicLong signal = LATCH_WAIT_COUNT.containsKey(resource) ? LATCH_WAIT_COUNT.get(resource) : null;
            if (null != signal && signal.get() <= 0) {
                LATCH_WAIT_COUNT.remove(resource);
                LATCH.remove(resource);
            }
        }
    }

    private boolean getSignal(String resource) {
        String resourcePath = LOCK_ROOT + resource;
        boolean locked = false;
        try {
            locked = !client.isExists(resourcePath, true) && null != client.create(resourcePath, true,
                    Thread.currentThread().getId() + "");
        } catch (Throwable e) {
            locked = false;
        }
        return locked;
    }
}
