/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月06日 11:53
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.event.s;

/**
 * 时间类型
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月06日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月06日 11:53
 */
public enum EventType {
    INSERT(0, "INSERT"),
    UPDATE(1, "UPDATE"),
    DELETE(2, "DELETE"),
    TRANSACTION_BEGIN(3, "BEGIN"),
    TRANSACTION_END(4, "END"),
    TRUNCATE(5, "TRUNCATE"),
    UNKNOWN(-1, "UNKNOWN");
    private int index;
    private String value;
    public static final int INSERT_INDEX = 0;
    public static final int UPDATE_INDEX = 1;
    public static final int DELETE_INDEX = 2;
    public static final int BEGIN_INDEX = 3;
    public static final int END_INDEX = 4;
    public static final int TRUNCATE_INDEX = 5;
    private EventType(int index, String value) {
        this.index =  index;
        this.value = value;
    }


    public static EventType type(String kafkaEvent) {
        if(kafkaEvent.equals("I")) {
            return  INSERT;
        } else if(kafkaEvent.equals("U")) {
            return  UPDATE;
        } else if(kafkaEvent.equals("D")) {
            return  DELETE;
        } else if(kafkaEvent.equals("BEGIN")) {
            return  TRANSACTION_BEGIN;
        } else if(kafkaEvent.equals("END")) {
            return  TRANSACTION_END;
        } else if(kafkaEvent.equals("T")) {
            return  TRUNCATE;
        } else {
            return UNKNOWN;
        }
    }
    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
}
