/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年05月02日 10:11
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.datacarrier.simple;

import com.suixingpay.datas.node.datacarrier.DataMapCarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年05月02日 10:11
 * @version: V1.0
 * @review: zkevin/2018年05月02日 10:11
 */
public class FixedCapacityCarrier implements DataMapCarrier {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixedCapacityCarrier.class);
    //map容量
    private final int capacity;
    private final Semaphore resources;
    private final Map<Object, Object> container = new ConcurrentHashMap<>();

    public FixedCapacityCarrier(int inputCapacity) {
        this.capacity = inputCapacity;
        resources = new Semaphore(capacity);
    }

    @Override
    public long size() {
        return container.size();
    }

    @Override
    public boolean push(Object key, Object value) throws InterruptedException {
        resources.acquire();
        container.put(key, value);
        return true;
    }

    @Override
    public Object pull(Object key) {
        return container.computeIfPresent(key, (k, v) -> {
            container.remove(key);
            resources.release();
            return v;
        });
    }

    @Override
    public boolean containsKey(Object key) {
        return container.containsKey(key);
    }

    @Override
    public void printState() {
        LOGGER.info("total:{},usable:{},used:{}", capacity, resources.availablePermits(), size());
    }

    @Override
    public void print() {
        LOGGER.info("total:{},usable:{},used:{},data:{}", capacity, resources.availablePermits(), size(), container);
    }
}