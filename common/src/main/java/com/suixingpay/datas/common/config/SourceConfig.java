/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:53
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;

import com.suixingpay.datas.common.config.source.CanalConfig;
import com.alibaba.fastjson.annotation.JSONField;
import com.suixingpay.datas.common.config.source.JDBCConfig;
import com.suixingpay.datas.common.config.source.KafkaConfig;
import com.suixingpay.datas.common.config.source.ZookeeperConfig;
import com.suixingpay.datas.common.config.source.EmailConfig;
import com.suixingpay.datas.common.config.source.NameSourceConfig;
import com.suixingpay.datas.common.dic.SourceType;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.common.util.BeanUtils;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 11:53
 */
public abstract class SourceConfig implements SwamlaneSupport {
    @JSONField(serialize = false, deserialize = false)
    protected static final Logger LOGGER = LoggerFactory.getLogger(SourceConfig.class);
    @JSONField(serialize = false, deserialize = false)
    public static final String SOURCE_TYPE_KEY = "sourceType";
    @JSONField(serialize = false, deserialize = false)
    public static final String NAME_SOURCE_KEY = "sourceName";
    @Getter protected SourceType sourceType;
    private Map<String, String> properties;

    protected abstract void childStuff();
    protected abstract String[] childStuffColumns();

    public  <T extends SourceConfig> T stuff() throws ConfigParseException {
        try {
            BeanUtils.copyProperties(properties, this, ArrayUtils.addAll(childStuffColumns(), SOURCE_TYPE_KEY));
            childStuff();
            return (T) this;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConfigParseException((null != properties ? properties.toString() : "{}") + "转换SourceConfig出错");
        }
    }


    public static  <T extends SourceConfig> T getConfig(Map<String, String>  properties) throws ConfigParseException {
        T config = null;
        try {
            String sourceTypeStr = properties.getOrDefault(SOURCE_TYPE_KEY, "");
            SourceType sourceType = !StringUtils.isBlank(sourceTypeStr) ? SourceType.valueOf(sourceTypeStr) : null;
            if (null != sourceType) {
                switch (sourceType) {
                    case ZOOKEEPER:
                        config = (T) new ZookeeperConfig();
                        break;
                    case JDBC:
                        config = (T) new JDBCConfig();
                        break;
                    case KAFKA:
                        config = (T) new KafkaConfig();
                        break;
                    case EMAIL:
                        config = (T) new EmailConfig();
                        break;
                    case NAME_SOURCE:
                        config = (T) new NameSourceConfig();
                        break;
                    case CANAL:
                        config = (T) new CanalConfig();
                        break;
                }
            } else if (properties.containsKey(NAME_SOURCE_KEY)) {
                config = (T) new NameSourceConfig();
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

    @Override
    public <T extends SourceConfig> List<T> swamlanes() throws ConfigParseException {
        return Arrays.asList((T) this);
    }
}
