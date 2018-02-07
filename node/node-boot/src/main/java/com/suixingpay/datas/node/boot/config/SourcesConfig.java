/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 13:59
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.boot.config;

import com.suixingpay.datas.common.config.Config;
import com.suixingpay.datas.common.config.source.SourceConfig;
import com.suixingpay.datas.common.exception.ConfigException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 13:59
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 13:59
 */

@ConfigurationProperties(prefix = "node")
@Component
public class SourcesConfig {
    private Map<String,Map<String, String>> source;

    public Map<String, Map<String, String>> getSource() {
        return source;
    }

    public void setSource(Map<String, Map<String, String>> source) {
        this.source = source;
    }

    public List<Pair<String, Config>> getConfig() throws ConfigException {
        List<Pair<String, Config>> configs = new ArrayList<>();
        if (null != source && !source.isEmpty()) {
            source.forEach((k,v) -> {
                Config config = Config.getConfig(v);
                if (config instanceof SourceConfig) {
                    SourceConfig sourceConfig = (SourceConfig) config;
                    sourceConfig.setSourceName(k);
                    configs.add(new ImmutablePair<>(k, config));
                }
            });
            if (source.size() != configs.size()) {
                throw new ConfigException();
            }
        }
        return configs;
    }
}
