/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 14:04
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.datacarrier.simple;

import com.suixingpay.datas.node.datacarrier.DataCarrier;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 14:04
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 14:04
 */
public class SimpleDataCarrier implements DataCarrier {
    private final ArrayBlockingQueue buffer;
    private final int pullBatchSize;
    public SimpleDataCarrier(Integer bufferSize, Integer segmentSize) {
        buffer = new ArrayBlockingQueue(bufferSize);
        pullBatchSize = segmentSize;
    }

    @Override
    public void push(List list) throws InterruptedException {
        if ( null != list && !list.isEmpty()) {
            for (Object t : list) {
                push(t);
            }
        }
    }

    @Override
    public void push(Object item) throws InterruptedException {
        if (null != item) buffer.put(item);
    }

    /**
     * 生成序列号和数据对儿，需要通过锁保证原子性
     * 默认开启偏向锁(UseBiasedLocking),在单线程调用情况下，锁消耗可忽略
     * 锁可重入
     * @return
     */
    @Override
    public synchronized Pair pullByOrder() {
        Object item = pull();
        return null != item ? new ImmutablePair(generateId(), item) :null;
    }

    /**
     * 生成序列号和数据对儿，需要通过锁保证原子性
     * 默认开启偏向锁(UseBiasedLocking),在单线程调用情况下，锁消耗可忽略
     * 锁可重入
     * @return
     */
    @Override
    public synchronized Pair<String, List> greedyPullByOrder() {
        List list = greedyPull();
        return null == list || list.isEmpty() ? null : new ImmutablePair<>(generateId(), list);
    }

    @Override
    public long size() {
        return buffer.size();
    }

    /**
     * 默认开启偏向锁(UseBiasedLocking),在单线程调用情况下，锁消耗可忽略
     * 锁可重入
     * @return
     */
    @Override
    public synchronized List greedyPull() {
        List list = new ArrayList();
        int currentCount = 0;
        Object item = null;
        while ((item = pull()) != null) {
            list.add(item);
            currentCount ++;
            if (currentCount >= pullBatchSize) break;
        }
        return list;
    }

    /**
     * 默认开启偏向锁(UseBiasedLocking),在单线程调用情况下，锁消耗可忽略
     * 锁可重入
     * @return
     */
    @Override
    public synchronized Object pull() {
        return buffer.poll();
    }

    private String generateId() {
        return UUID.randomUUID().toString() + System.currentTimeMillis();
    }
}