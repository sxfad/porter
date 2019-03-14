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

package cn.vbill.middleware.porter.common.cluster;

import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;

import java.util.Collections;
import java.util.List;

/**
 * 集群监听器监听者
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:35
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:35
 */
public interface ClusterListener {

    /**
     * 监听方法通知
     * @param event
     */
    void onEvent(ClusterTreeNodeEvent event);

    /**
     * 集群监听过滤器，如果不符合过滤器要求则停止通知
     * @return
     */
    ClusterListenerFilter filter();

    /**
     * 集群实现依赖客户端
     * @param client
     */
    void setClient(ClusterClient client);

    /**
     * start
     */
    default void start() {

    }

    /**
     * 是否监听listenPath
     *
     * @return
     */
    default boolean watchListenPath() {
        return false;
    }

    String listenPath();

    default String getName() {
        return listenPath();
    }

    default List<ClusterListenerEventExecutor> watchedEvents() {
        return Collections.emptyList();
    }
}
