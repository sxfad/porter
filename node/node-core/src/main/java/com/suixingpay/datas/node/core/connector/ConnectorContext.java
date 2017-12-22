package com.suixingpay.datas.node.core.connector;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 15:16
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.connector.DataConnector;
import com.suixingpay.datas.common.connector.DataDriver;
import com.suixingpay.datas.common.connector.DataDriverType;
import com.suixingpay.datas.common.connector.NamedDataDriver;
import com.suixingpay.datas.node.core.connector.jdbc.JDBCConnector;
import com.suixingpay.datas.node.core.connector.mq.KafkaConnector;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:37
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 10:37
 */
public enum  ConnectorContext {
    INSTANCE();
    public final Map<String,DataConnector> connection;
    private ConnectorContext() {
        connection = new ConcurrentHashMap<String, DataConnector>();
    }
    public void initialize(Map<String,DataDriver> drivers) {
        for (Map.Entry<String, DataDriver> entry : drivers.entrySet()) {
            DataConnector publicConnector = newConnector(entry.getValue());
            if (null != publicConnector) {
                DataConnector putReturn = connection.putIfAbsent(entry.getKey(), publicConnector);
                if (null == putReturn) {//第一次添加
                    publicConnector.connect();
                    //将资源标注为公共资源,默认为私有
                    publicConnector.setPrivatePool(false);
                } else {
                    publicConnector.disconnect();
                    publicConnector = null;
                }
            }
        }
    }
    public DataConnector newConnector(DataDriver driver) {
        DataConnector connector = null;
        String namedDriverName = driver instanceof NamedDataDriver ? ((NamedDataDriver)driver).getName() : null;
        if (!StringUtils.isBlank(namedDriverName) && connection.containsKey(namedDriverName) ) {
            connector = connection.get(namedDriverName);
        } else {
            switch (driver.getType().getValue()) {
                case DataDriverType.KAFKA_VALUE :
                    connector = new KafkaConnector(driver);
                    break;
                case DataDriverType.MYSQL_VALUE :
                    connector = new JDBCConnector(driver);
                    break;
                case DataDriverType.ORACLE_VALUE :
                    connector = new JDBCConnector(driver);
                    break;
            }
        }
        return connector;
    }
}
