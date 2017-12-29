/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:46
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.datasource;


import com.suixingpay.datas.common.datasource.meta.DataDriverMeta;
import com.suixingpay.datas.common.datasource.meta.JDBCDriverMeta;
import com.suixingpay.datas.common.datasource.meta.KafkaDriverMeta;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 10:46
 */
public enum  DataDriverType {
    KAFKA(0, 1, KafkaDriverMeta.INSTANCE),
    MYSQL(1, 2, JDBCDriverMeta.INSTANCE),
    ORACLE(2, 3, JDBCDriverMeta.INSTANCE);
    private final int index;
    private final int value;
    private final DataDriverMeta meta;
    public static final int KAFKA_VALUE = 1;
    public static final int MYSQL_VALUE = 2;
    public static final int ORACLE_VALUE = 3;
    private DataDriverType(int index, int value, DataDriverMeta meta){
        this.index = index;
        this.value = value;
        this.meta = meta;
    }
    public static DataDriverType valueOf(int value) {
        switch (value) {
            case 1:
                return KAFKA;
            case 2:
                return MYSQL;
            case 3:
                return ORACLE;
            default:
                return null;
        }
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }

    public DataDriverMeta getMeta() {
        return meta;
    }
}
