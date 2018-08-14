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

package cn.vbill.middleware.porter.common.cluster.impl;

import cn.vbill.middleware.porter.common.cluster.ClusterListener;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.ClusterMonitor;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.command.ShutdownCommand;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 14:47
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 14:47
 */
public abstract class AbstractClusterMonitor implements ClusterMonitor {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected final Map<String, ClusterListener> listeners = new LinkedHashMap<>();

    /**
     * doStart
     */
    protected abstract void doStart();

    @Override
    public void addListener(ClusterListener listener) {
        listeners.put(listener.getName(), listener);
    }

    @Override
    public void onEvent(ClusterEvent e) {
        if (null == e || null == listeners || listeners.isEmpty()) {
            return;
        }
        for (ClusterListener listener : listeners.values()) {
            ClusterListenerFilter filter = listener.filter();
            if (null == filter || filter.onFilter(e)) {
                listener.onEvent(e);
            }
        }
    }

    @Override
    public Map<String, ClusterListener> getListener() {
        return Collections.unmodifiableMap(listeners);
    }

    @Override
    public void stop() {
        try {
            //最后的清除任务
            ClusterProviderProxy.INSTANCE.broadcast(new ShutdownCommand());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("%s", e);
        }
    }

    @Override
    public void start() {
        doStart();
        //监听器初始化
        listeners.forEach((k, v) -> {
            try {
                v.start();
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("%s", e);
            }
        });
    }
}
