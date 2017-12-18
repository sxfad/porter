package com.suixingpay.datas.node.core.connector;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:37
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */


import com.suixingpay.datas.node.core.connector.mq.KafkaConnector;

/**
 * TODO
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:37
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 10:37
 */
public enum  ConnectorBuilder {
    INSTANCE();

    private ConnectorBuilder() {
    }

    public DataConnector newConnector(DataDriver driver) {
        DataConnector connector = null;
        switch (driver.getType().getValue()) {
            case DataDriverType.KAFKA_VALUE :
                connector = new KafkaConnector(driver);
                break;
            case DataDriverType.MYSQL_VALUE :
                break;
            case DataDriverType.ORACLE_VALUE :
                break;
        }
        return connector;
    }
}