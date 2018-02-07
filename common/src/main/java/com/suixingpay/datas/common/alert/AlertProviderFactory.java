/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:14
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.alert;

import com.suixingpay.datas.common.alert.provider.AlertProvider;
import com.suixingpay.datas.common.alert.provider.EmailAlertProvider;
import com.suixingpay.datas.common.config.Config;
import com.suixingpay.datas.common.config.ConfigType;
import com.suixingpay.datas.common.config.EmailAlertConfig;
import com.suixingpay.datas.common.util.ApplicationContextUtils;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月01日 20:14
 */
public enum AlertProviderFactory {
    INSTANCE();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private AlertProvider alert;
    public void initialize(Config config) {
        if (isInitialized.compareAndSet(false, true)) {
            if (config.getConfigType() == ConfigType.EMAIL_ALERT) {
                alert = new EmailAlertProvider(config, ApplicationContextUtils.INSTANCE.getBean(JavaMailSender.class));
            }
        }
    }
    public void notice(String msg) {
        alert.notice(msg);
    }
}
