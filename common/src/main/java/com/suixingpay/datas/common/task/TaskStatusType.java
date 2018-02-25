/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 10:32
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.task;

/**
 * 节点状态
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 10:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 10:32
 */
public enum TaskStatusType {
    NEW, STOPPED, WORKING;

    public boolean isStopped () {
        return this == STOPPED;
    }
    public boolean isWorking () {
        return this == WORKING;
    }
}
