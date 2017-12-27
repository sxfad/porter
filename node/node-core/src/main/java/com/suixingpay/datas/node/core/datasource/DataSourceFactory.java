/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 15:16
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.datasource;

import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import com.suixingpay.datas.common.datasource.DataDriver;
import com.suixingpay.datas.common.datasource.DataDriverType;
import com.suixingpay.datas.common.datasource.NamedDataDriver;
import com.suixingpay.datas.node.core.datasource.jdbc.JDBCSourceWrapper;
import com.suixingpay.datas.node.core.datasource.mq.KafkaSourceWrapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:37
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 10:37
 */
public enum DataSourceFactory {
    INSTANCE();
    private final Map<String,String> publicDataSources;
    private final Map<String,DataSourceWrapper> allSources;
    private DataSourceFactory() {
        allSources = new ConcurrentHashMap<String, DataSourceWrapper>();
        publicDataSources = new ConcurrentHashMap<String, String>();
    }
    public void initialize(Map<String,DataDriver> drivers) {
        for (Map.Entry<String, DataDriver> entry : drivers.entrySet()) {
            newDataSource(entry.getValue());
        }
    }
    public DataSourceWrapper getDataSource(String sourceId) {
        return allSources.get(sourceId);
    }
    public DataSourceWrapper newDataSource(DataDriver driver) {
        DataSourceWrapper wrapper = null;
        String namedDriverName = driver instanceof NamedDataDriver ? ((NamedDataDriver)driver).getName() : null;
        if (!StringUtils.isBlank(namedDriverName) && publicDataSources.containsKey(namedDriverName) ) {
            wrapper = allSources.get(publicDataSources.get(namedDriverName));
        } else {
            switch (driver.getType().getValue()) {
                case DataDriverType.KAFKA_VALUE :
                    wrapper = new KafkaSourceWrapper(driver);
                    break;
                case DataDriverType.MYSQL_VALUE :
                    wrapper = new JDBCSourceWrapper(driver);
                    break;
                case DataDriverType.ORACLE_VALUE :
                    wrapper = new JDBCSourceWrapper(driver);
                    break;
            }
        }
        if (null != wrapper && ! allSources.containsKey(wrapper.getUniqueId())) {
            allSources.putIfAbsent(wrapper.getUniqueId(), wrapper);
            if (driver instanceof NamedDataDriver && ! publicDataSources.containsKey(namedDriverName)) {
                publicDataSources.putIfAbsent(namedDriverName, wrapper.getUniqueId());
                wrapper.setPrivatePool(false);
                wrapper.create();
            }
        }
        return wrapper;
    }
}
