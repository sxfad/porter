package com.suixingpay.datas.common.cluster;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.cluster.zookeeper.ZookeeperDriverMeta;

/**
 * 集群中间件类型
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:20
 */
public enum  ClusterType {
    ZOOKEEPER(0, "ZK", ZookeeperDriverMeta.INSTANCE),
    REDIS(1, "REDIS", null),
    UNKNOWN(-1, "UNKNOWN", null);
    private int index;
    private String value;
    private final ClusterDriverMeta meta;
    private ClusterType(int index, String value, ClusterDriverMeta meta) {
        this.index = index;
        this.value = value;
        this.meta =meta;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ClusterDriverMeta getMeta() {
        return meta;
    }
}
