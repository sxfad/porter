/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:45
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster;

import com.suixingpay.datas.common.cluster.event.ClusterEvent;

import java.util.Map;

/**
 * 某一个路径监听
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:45
 */
public interface ClusterMonitor {
    void addListener(ClusterListener listener);
    void setClient(Client client);
    void onEvent(ClusterEvent e);
    Map<String, ClusterListener> getListener();
    void start();
    void stop() throws Exception;
}
