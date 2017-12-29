/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 10:00
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.zookeeper;

import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 10:00
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 10:00
 */
public abstract class ZookeeperClusterListenerFilter implements ClusterListenerFilter {
    protected abstract String getPath();
    protected abstract boolean doFilter(ZookeeperClusterEvent event);
    @Override
    public boolean onFilter(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent)event;
        //是否路径匹配
        boolean isPathMatch = false;
        isPathMatch = zkEvent.getPath().startsWith(getPath());
        return isPathMatch && doFilter(zkEvent);
    }
}
