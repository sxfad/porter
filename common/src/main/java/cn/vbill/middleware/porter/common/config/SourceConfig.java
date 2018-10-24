/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.common.config;

import cn.vbill.middleware.porter.common.config.source.CanalConfig;
import cn.vbill.middleware.porter.common.config.source.EmailConfig;
import cn.vbill.middleware.porter.common.config.source.FileOperationConfig;
import cn.vbill.middleware.porter.common.config.source.JDBCConfig;
import cn.vbill.middleware.porter.common.config.source.KafkaConfig;
import cn.vbill.middleware.porter.common.config.source.KafkaProduceConfig;
import cn.vbill.middleware.porter.common.config.source.KuduConfig;
import cn.vbill.middleware.porter.common.config.source.NameSourceConfig;
import cn.vbill.middleware.porter.common.config.source.ZookeeperConfig;
import cn.vbill.middleware.porter.common.dic.SourceType;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.util.BeanUtils;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

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
    //插件服务配置文件
    @JSONField(serialize = false, deserialize = false)
    private static final List<PluginServiceConfig> PLUGIN_SERVICE_CONFIGS =
            SpringFactoriesLoader.loadFactories(PluginServiceConfig.class, JavaFileCompiler.getInstance());

    @JSONField(serialize = false, deserialize = false)
    protected static final Logger LOGGER = LoggerFactory.getLogger(SourceConfig.class);

    /**
     * SOURCE_TYPE_KEY
     */
    @JSONField(serialize = false, deserialize = false)
    public static final String SOURCE_TYPE_KEY = "sourceType";

    /**
     * NAME_SOURCE_KEY
     */
    @JSONField(serialize = false, deserialize = false)
    public static final String NAME_SOURCE_KEY = "sourceName";
    @Getter
    protected SourceType sourceType;
    private Map<String, String> properties;

    /**
     * childStuff
     */
    protected abstract void childStuff();

    /**
     * childStuffColumns
     *
     * @return
     */
    protected abstract String[] childStuffColumns();

    /**
     * stuff
     *
     * @param <T>
     * @return
     * @throws ConfigParseException
     */
    public <T extends SourceConfig> T stuff() throws ConfigParseException {
        try {
            BeanUtils.copyProperties(properties, this, ArrayUtils.addAll(childStuffColumns(), SOURCE_TYPE_KEY));
            childStuff();
            return (T) this;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("填充配置文件失败", e);
            throw new ConfigParseException((null != properties ? properties.toString() : "{}") + "转换SourceConfig出错");
        }
    }

    /**
     * 获取config
     *
     * @param properties
     * @param <T>
     * @return
     * @throws ConfigParseException
     */
    public static <T extends SourceConfig> T getConfig(Map<String, String> properties) throws ConfigParseException {
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
                    case KUDU:
                        config = (T) new KuduConfig();
                        break;
                    case KAFKA_PRODUCE:
                        config = (T) new KafkaProduceConfig();
                        break;
                    case FILE:
                        config = (T) new FileOperationConfig();
                        break;
                    default:
                        break;
                }
            }
            if (null == config && properties.containsKey(NAME_SOURCE_KEY)) {
                config = (T) new NameSourceConfig();
            }

            //如果配置文件对象仍不存在，尝试从插件服务SPI加载
            if (null == config && null != sourceType) {
                for (PluginServiceConfig c : PLUGIN_SERVICE_CONFIGS) {
                    if (c instanceof SourceConfig && c.isMatch(sourceType.getCode())) {
                        config = (T) c.getClass().newInstance();
                        break;
                    }
                }
            }

            if (null != config) {
                config.setProperties(properties);
                config.stuff();
                if (!config.check()) {
                    throw new ConfigParseException("参数格式不正确:" + properties);
                }
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

    /**
     * 格式校验
     *
     * @return
     */
    protected boolean check() {
        return doCheck();
    }

    /**
     * doCheck
     *
     * @return
     */
    protected abstract boolean doCheck();
}
