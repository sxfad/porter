/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 16:08
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.zookeeper;

import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.event.EventType;

/**
 * TODO
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 16:08
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 16:08
 */
public class ZookeeperClusterEvent extends ClusterEvent {
    private final String path;
    public ZookeeperClusterEvent(EventType eventType, String data) {
        super(eventType, data);
        path = null;
    }
    public ZookeeperClusterEvent(EventType eventType, String data, String path) {
        super(eventType, data);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
