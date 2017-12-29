/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 10:17
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.boot.config;

import com.suixingpay.datas.common.cluster.ClusterDriver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 10:17
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月28日 10:17
 */
@ConfigurationProperties(prefix = "cluster")
@Component
public class ClusterDriverConfig {
    private ClusterDriver driver;

    public ClusterDriver getDriver() {
        return driver;
    }

    public void setDriver(ClusterDriver driver) {
        this.driver = driver;
    }
}
