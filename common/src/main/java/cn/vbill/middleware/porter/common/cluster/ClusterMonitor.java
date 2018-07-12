/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:45
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.common.cluster;

import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;

import java.util.Map;

/**
 * 集群监听
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:45
 */
public interface ClusterMonitor {
    /**
     * 添加监听器
     * @param listener
     */
    void addListener(ClusterListener listener);

    /**
     * 设置集群提供客户端
     * @param client
     */
    void setClient(Client client);

    /**
     *
     * @param e
     */
    void onEvent(ClusterEvent e);

    /**
     * 获取监听器
     * @return
     */
    Map<String, ClusterListener> getListener();

    /**
     * 启动监听
     */
    void start();

    /**
     * 停止监听
     * @throws Exception
     */
    void stop();
}
