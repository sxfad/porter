/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 14:47
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
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

    protected abstract void doStart();

    @Override
    public void addListener(ClusterListener listener) {
        listeners.put(listener.getName(), listener);
    }

    @Override
    public void onEvent(ClusterEvent e) {
        if (null == e || null == listeners || listeners.isEmpty()) return;
        for (ClusterListener listener : listeners.values()) {
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

    @Override
    public void start() {
        doStart();
        //监听器初始化
        listeners.forEach((k, v) -> {
            try {
                v.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
