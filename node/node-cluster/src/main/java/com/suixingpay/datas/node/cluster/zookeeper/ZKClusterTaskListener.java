package com.suixingpay.datas.node.cluster.zookeeper;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.cluster.ClusterListener;
import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterTaskListener implements ClusterListener {
    private static final String ZK_PATH = "/task";
    @Override
    public String path() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {

    }

    @Override
    public ClusterListenerFilter filter() {
        return null;
    }
}
