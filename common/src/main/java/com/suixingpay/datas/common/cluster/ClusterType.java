/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster;

import com.suixingpay.datas.common.cluster.zookeeper.ZookeeperClusterProvider;
import com.suixingpay.datas.common.cluster.zookeeper.ZookeeperDriverMeta;
import org.apache.commons.lang3.StringUtils;

/**
 * 集群中间件类型
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:20
 */
public enum  ClusterType {
    ZOOKEEPER(0, "ZK", ZookeeperDriverMeta.INSTANCE, ZookeeperClusterProvider.class),
    REDIS(1, "REDIS", null, null),
    UNKNOWN(-1, "UNKNOWN", null, null);
    private int index;
    private String value;
    private final ClusterDriverMeta meta;
    private Class implementClass;
    private ClusterType(int index, String value, ClusterDriverMeta meta, Class implementClass) {
        this.index = index;
        this.value = value;
        this.meta =meta;
        this.implementClass = implementClass;
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

    public boolean matches(String clazz) {
        return null != implementClass && !StringUtils.isBlank(clazz) && StringUtils.trim(clazz).equals(implementClass.getName());
    }
}
