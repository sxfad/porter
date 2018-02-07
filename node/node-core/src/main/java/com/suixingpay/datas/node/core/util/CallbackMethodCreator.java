/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 11:20
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.util;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 11:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 11:20
 */
public interface CallbackMethodCreator {
    default void invoke(Object ... prams) {
        throw new UnsupportedOperationException();
    }

    default void invoke() {
        throw new UnsupportedOperationException();
    }

    default <T> T invokeWithResult(Object ... params) {
        throw new UnsupportedOperationException();
    }

    default <T> T invokeWithResult() {
        throw new UnsupportedOperationException();
    }
}
