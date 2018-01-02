/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 16:24
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.zookeeper;

import com.suixingpay.datas.common.cluster.Client;
import com.suixingpay.datas.common.cluster.ClusterListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 16:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 16:24
 */
public abstract class ZookeeperClusterListener implements ClusterListener {
    protected static final  String PREFIX_ATALOG = "/suixingpay";
    protected static final  String BASE_CATALOG = PREFIX_ATALOG + "/datas";
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected ZookeeperClient client;
    public abstract String path();

    @Override
    public void setClient(Client client) {
        this.client = (ZookeeperClient)client;
    }
}
