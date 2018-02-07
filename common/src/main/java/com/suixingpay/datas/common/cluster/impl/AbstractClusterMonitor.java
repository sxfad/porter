/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 14:47
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.cluster.impl;

import com.suixingpay.datas.common.cluster.ClusterListener;
import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.ClusterMonitor;
import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.ShutdownCommand;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;

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
    protected final Map<String, ClusterListener> listeners = new LinkedHashMap<>();

    @Override
    public void addListener(ClusterListener listener) {
        listeners.put(listener.getName(),listener);
    }

    @Override
    public void onEvent(ClusterEvent e) {
        if (null == e || null == listeners || listeners.isEmpty()) return;
        for (ClusterListener listener : listeners.values()){
            ClusterListenerFilter filter = listener.filter();
            if (null == filter || filter.onFilter(e)) listener.onEvent(e);
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
        }
    }
}
