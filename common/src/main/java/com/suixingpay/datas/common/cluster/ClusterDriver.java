package com.suixingpay.datas.common.cluster;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:07
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 集群驱动信息
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 17:07
 */
@ConfigurationProperties(prefix = "cluster.driver")
@Component
public class ClusterDriver {
    private ClusterType type;
    private String url;
    //扩展属性
    private Map<String,String> extendAttr;
    public ClusterType getType() {
        return type;
    }

    public void setType(ClusterType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getExtendAttr() {
        return extendAttr;
    }

    public void setExtendAttr(Map<String, String> extendAttr) {
        this.extendAttr = extendAttr;
    }

    public static boolean error(ClusterDriver config){
        return null == config || config.getType() == null || config.getType() == ClusterType.UNKNOWN;
    }
}
