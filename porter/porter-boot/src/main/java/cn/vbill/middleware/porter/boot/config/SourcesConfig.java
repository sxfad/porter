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

package cn.vbill.middleware.porter.boot.config;

import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.common.config.SourceConfig;
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
    private Map<String, Map<String, String>> source;

    public Map<String, Map<String, String>> getSource() {
        return source;
    }

    public void setSource(Map<String, Map<String, String>> source) {
        this.source = source;
    }

    public List<Pair<String, SourceConfig>> getConfig() throws ConfigParseException {
        List<Pair<String, SourceConfig>> configs = new ArrayList<>();
        if (null != source && !source.isEmpty()) {
            for (Map.Entry<String, Map<String, String>> p : source.entrySet()) {
                SourceConfig config = SourceConfig.getConfig(p.getValue());
                if (null != config) configs.add(new ImmutablePair<>(p.getKey(), config));
            }
            if (source.size() != configs.size()) {
                throw new ConfigParseException(source + "配置和解析结果不一致:" + JSONObject.toJSONString(configs));
            }
        }
        return configs;
    }
}
