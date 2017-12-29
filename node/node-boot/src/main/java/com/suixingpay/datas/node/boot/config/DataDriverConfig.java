/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 13:59
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.boot.config;

import com.suixingpay.datas.common.datasource.DataDriver;
import com.suixingpay.datas.common.datasource.DataDriverType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 13:59
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 13:59
 */

@ConfigurationProperties(prefix = "dataDriver")
@Component
public class DataDriverConfig {
    private Map<String,DataDriver> drivers;
    public DataDriverConfig() {
        drivers = new HashMap<>();
    }

    public Map<String, DataDriver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Map<String, DataDriver> drivers) {
        this.drivers = drivers;
    }

    public static boolean error(DataDriverConfig config) {
        boolean error = false;
        if (null != config) {
            for (DataDriver driver : config.drivers.values()) {
                if (driver == null || driver.getType() == null || !(driver.getType() == DataDriverType.MYSQL ||
                        driver.getType() == DataDriverType.ORACLE )) {
                    error = true;
                    break;
                }
            }
        }
        return error;
    }

}
