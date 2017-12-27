/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 10:12
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.task;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 10:12
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 10:12
 */
public  enum TaskEventType {
    CREATE, DELETE;
    private TaskEventType() {

    }
    public boolean isCreate() {
        return this == TaskEventType.CREATE;
    }

    public boolean isDelete() {
        return this == TaskEventType.DELETE;
    }
}