/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:35
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster;

import com.suixingpay.datas.common.client.Client;
import com.suixingpay.datas.common.cluster.command.ClusterCommand;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;

/**
 * 集群监听器监听者
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:35
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:35
 */
public interface ClusterListener {
    String getName();
    /**
     * 监听方法通知
     * @param event
     */
    void onEvent(ClusterEvent event);

    /**
     * 集群监听过滤器，如果不符合过滤器要求则停止通知
     * @return
     */
    ClusterListenerFilter filter();

    /**
     * 集群实现依赖客户端
     * @param client
     */
    void setClient(Client client);
}
