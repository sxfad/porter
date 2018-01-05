/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 15:05
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.event.s.converter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 15:05
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月05日 15:05
 */
public class ConvertNotMatchException extends Exception {
    public ConvertNotMatchException() {
        super();
    }

    public ConvertNotMatchException(String message) {
        super(message);
    }
}
