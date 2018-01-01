/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:17
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.boot.config;

import com.suixingpay.datas.common.alert.AlertDriver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:17
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月01日 20:17
 */
@ConfigurationProperties(prefix = "alert")
@Component
public class AlertConfig {
    private AlertDriver driver;

    public AlertDriver getDriver() {
        return driver;
    }

    public void setDriver(AlertDriver driver) {
        this.driver = driver;
    }
}
