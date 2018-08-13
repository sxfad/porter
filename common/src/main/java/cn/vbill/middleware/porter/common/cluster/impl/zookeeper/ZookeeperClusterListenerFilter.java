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

package cn.vbill.middleware.porter.common.cluster.impl.zookeeper;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 10:00
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 10:00
 */
public abstract class ZookeeperClusterListenerFilter implements ClusterListenerFilter {

    /**
     * 获取path
     * @return
     */
    protected abstract String getPath();

    /**
     * doFilter
     * @param event
     * @return
     */
    protected abstract boolean doFilter(ZookeeperClusterEvent event);

    @Override
    public boolean onFilter(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        //是否路径匹配
        boolean isPathMatch = zkEvent.getPath().startsWith(getPath());
        return isPathMatch && doFilter(zkEvent);
    }
}
