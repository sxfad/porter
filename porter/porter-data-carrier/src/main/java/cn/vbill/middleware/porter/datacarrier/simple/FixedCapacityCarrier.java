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

import cn.vbill.middleware.porter.datacarrier.DataMapCarrier;
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