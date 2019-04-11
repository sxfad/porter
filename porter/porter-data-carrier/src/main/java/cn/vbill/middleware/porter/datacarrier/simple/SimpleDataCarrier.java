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

package cn.vbill.middleware.porter.datacarrier.simple;

import cn.vbill.middleware.porter.datacarrier.DataCarrier;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

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
    public void push(Object item) throws InterruptedException {
        if (null != item) {
            buffer.put(item);
        }
    }

    /**
     * 生成序列号和数据对儿，需要通过锁保证原子性
     * 默认开启偏向锁(UseBiasedLocking),在单线程调用情况下，锁消耗可忽略
     * 锁可重入
     * @return
     */
    @Override
    public Pair pullByOrder() throws InterruptedException {
        Object item = pull();
        return null != item ? new ImmutablePair(generateId(), item) : null;
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
    public Object pull() throws InterruptedException {
        return buffer.take();
    }

    /**
     * generateId
     *
     * @date 2018/8/9 上午11:59
     * @param: []
     * @return: java.lang.String
     */
    private String generateId() {
        return UUID.randomUUID().toString() + System.currentTimeMillis();
    }
}