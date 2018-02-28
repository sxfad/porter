/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.manager;

import org.springframework.context.ApplicationContext;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 16:03
 */
public enum ManagerContext {
    INSTANCE();
    private ApplicationContext context;
    public <T> T getBean(Class<T> clazz) {
        return null != context ? context.getBean(clazz) : null;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }
}
