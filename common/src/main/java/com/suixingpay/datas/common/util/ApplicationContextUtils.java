/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 14:46
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.util;

import org.springframework.context.ApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 14:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月26日 14:46
 */
public enum  ApplicationContextUtils {
    INSTANCE();
    private final AtomicBoolean isInit;
    private ApplicationContext context;
    private ApplicationContextUtils() {
        isInit = new AtomicBoolean(false);
    }
    public void init(ApplicationContext context) {
        if (isInit.compareAndSet(false, true)){
            this.context = context;
        }
    }
    public <T> T getBean(Class<T> clazz) {
        return isInit.get() ? context.getBean(clazz) : null;
    }
}
