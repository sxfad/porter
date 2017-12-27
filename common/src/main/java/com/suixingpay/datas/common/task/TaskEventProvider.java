/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 17:52
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.task;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 17:52
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 17:52
 */
public interface TaskEventProvider {
    void addTaskEventListener(TaskEventListener listener);
}
