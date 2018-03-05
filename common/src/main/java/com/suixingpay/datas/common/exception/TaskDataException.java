/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月05日 10:53
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.exception;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月05日 10:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月05日 10:53
 */
public class TaskDataException extends TaskException {
    public TaskDataException(Throwable cause) {
        super(cause);
    }

    public TaskDataException(String message) {
        super(message);
    }
}
