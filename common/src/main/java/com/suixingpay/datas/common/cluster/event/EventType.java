package com.suixingpay.datas.common.cluster.event;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月18日 10:45
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

/**
 * 事件类型
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月18日 10:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月18日 10:45
 */
public enum  EventType {
    UNKNOWN(-1, 0),
    NONE(0, 1),
    ONLINE(1, 2),
    OFFLINE(2, 3),
    DATA_CHANGED(3, 4);
    private int index;
    private int value;
    private EventType(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public int index() {
        return  index;
    }
    public int value() {
        return value;
    }
}
