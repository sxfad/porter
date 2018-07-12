/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:15
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.common.cluster.impl.zookeeper;

import cn.vbill.middleware.porter.common.client.impl.ZookeeperClient;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterProvider;
import cn.vbill.middleware.porter.common.config.ClusterConfig;
import cn.vbill.middleware.porter.common.config.source.ZookeeperConfig;
import cn.vbill.middleware.porter.common.dic.ClusterPlugin;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.cluster.ClusterMonitor;

/**
 * zookeeper集群提供者
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 18:15
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 18:15
 */
public class ZookeeperClusterProvider extends AbstractClusterProvider {
    @Override
    protected ClusterPlugin getMatchType() {
        return ClusterPlugin.ZOOKEEPER;
    }

    @Override
    protected ClusterMonitor newMonitor() {
        return new ZookeeperClusterMonitor();
    }

    @Override
    protected Class getClusterListenerClass() {
        return ZookeeperClusterListener.class;
    }

    @Override
    protected Client initClient(ClusterConfig clusterConfig) throws ConfigParseException {
        ZookeeperConfig config = new ZookeeperConfig(clusterConfig.getClient()).stuff();
        return new ZookeeperClient(config);
    }
}