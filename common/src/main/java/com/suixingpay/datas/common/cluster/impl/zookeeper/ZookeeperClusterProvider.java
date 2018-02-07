/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:15
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.impl.zookeeper;

import com.suixingpay.datas.common.cluster.*;
import com.suixingpay.datas.common.cluster.impl.AbstractClusterProvider;
import com.suixingpay.datas.common.config.ConfigType;

/**
 * zookeeper集群提供者
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:15
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 18:15
 */
public class ZookeeperClusterProvider extends AbstractClusterProvider {
    @Override
    protected ConfigType getMatchType() {
        return ConfigType.ZOOKEEPER;
    }

    @Override
    protected ClusterMonitor newMonitor() {
        return new ZookeeperClusterMonitor();
    }

    @Override
    protected Class getClusterListenerClass() {
        return ZookeeperClusterListener.class;
    }
}