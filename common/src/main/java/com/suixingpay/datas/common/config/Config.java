/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;

import com.suixingpay.datas.common.config.source.JDBCConfig;
import com.suixingpay.datas.common.config.source.KafkaConfig;
import com.suixingpay.datas.common.config.source.ZookeeperConfig;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public  abstract class Config {
    public static final String CONFIG_TYPE_KEY = "configType";
    @Getter protected ConfigType configType;
    private Map<String, String> properties;

    protected abstract void childStuff();
    protected abstract String[] childStuffColumns();

    protected <T extends Config> T stuff() {
        BeanUtils.copyProperties(properties, this, childStuffColumns());
        childStuff();
        return (T) this;
    }


    public static  <T extends Config> T getConfig(Map<String, String>  properties) {
        String configTypeStr = properties.getOrDefault(CONFIG_TYPE_KEY, "");
        ConfigType configType = ! StringUtils.isBlank(configTypeStr) ? ConfigType.valueOf(configTypeStr) : null;
        T config = null;
        switch (configType) {
            case REDIS:
                config = null;
                break;
            case ZOOKEEPER:
                config = (T) new ZookeeperConfig();
                break;
            case JDBC:
                config = (T) new JDBCConfig();
                break;
            case KAFKA:
                config = (T) new KafkaConfig();
                break;
            case EMAIL_ALERT:
                config = (T) new EmailAlertConfig();
                break;
        }

        if (null != config) {
            config.setProperties(properties);
            config.stuff();
        }
        return config;
    }

    public final Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }
    protected void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
