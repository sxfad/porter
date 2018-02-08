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
import com.suixingpay.datas.common.config.source.SourceConfig;
import com.suixingpay.datas.common.config.source.ZookeeperConfig;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.common.util.BeanUtils;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public  abstract class Config {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    public static final String CONFIG_TYPE_KEY = "configType";
    public static final String PUBLIC_SOURCE_NAME_KEY = "sourceName";
    @Getter protected ConfigType configType;
    private Map<String, String> properties;

    protected abstract void childStuff();
    protected abstract String[] childStuffColumns();

    protected <T extends Config> T stuff() throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(properties, this, ArrayUtils.addAll(childStuffColumns(),CONFIG_TYPE_KEY));
        childStuff();
        return (T) this;
    }


    public static  <T extends Config> T getConfig(Map<String, String>  properties) throws ConfigParseException {
        T config = null;
        try {
            String configTypeStr = properties.getOrDefault(CONFIG_TYPE_KEY, "");
            ConfigType configType = !StringUtils.isBlank(configTypeStr) ? ConfigType.valueOf(configTypeStr) : null;
            if (null != configType) {
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
                    case PUBLIC_SOURCE:
                        config = (T) new SourceConfig();
                        break;
                }
            } else if (properties.containsKey(PUBLIC_SOURCE_NAME_KEY)) {
                config = (T) new SourceConfig();
            }
            if (null != config) {
                config.setProperties(properties);
                config.stuff();
            }
        } catch (Exception e) {
            LOGGER.error("failed to parse config:{}", properties, e);
            throw new ConfigParseException(e.getMessage());
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
