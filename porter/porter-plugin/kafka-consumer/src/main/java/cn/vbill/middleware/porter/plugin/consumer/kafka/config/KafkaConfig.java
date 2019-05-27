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

package cn.vbill.middleware.porter.plugin.consumer.kafka.config;

import cn.vbill.middleware.porter.common.plugin.config.PluginServiceConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.plugin.consumer.kafka.KafkaConsumerConst;
import cn.vbill.middleware.porter.plugin.consumer.kafka.client.KafkaClient;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class KafkaConfig extends SourceConfig implements PluginServiceConfig {
    @JSONField(serialize = false, deserialize = false)
    private static final String TOPIC_SPLIT_CHARACTER = ",";
    @JSONField(serialize = false, deserialize = false)
    private static final String TOPICS_KEY = "topics";

    @Setter @Getter private String servers;
    @Setter @Getter private String group;
    @Setter @Getter private List<String> topics;
    @Setter @Getter private int pollTimeOut  = 1000;
    @Setter @Getter private int oncePollSize = 1000;
    @Setter @Getter private int onceCommitInterval = 1000;
    @Setter @Getter private String firstConsumeFrom = "earliest";
    @Setter @Getter private boolean autoCommit = Boolean.FALSE;

    //消费分区 2018.10.16 zhangkewei
    @Setter @Getter private int partition = 0;


    //允许自动重置offset
    @Setter @Getter private boolean allowOffsetReset = false;


    @Override
    protected void childStuff() {
        String topicStr = getProperties().getOrDefault(TOPICS_KEY, "");
        topics = Arrays.stream(topicStr.split(TOPIC_SPLIT_CHARACTER)).collect(Collectors.toList());
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {"topics"};
    }

    @Override
    public List<KafkaConfig> swamlanes() throws ConfigParseException {
        List<KafkaConfig> configs = new ArrayList<>(topics.size());
        if (null == topics || topics.size() < 2) {
            configs.add(this);
        } else {
            for (String topic : topics) {
                Map<String, String> properties = new HashMap<>();
                properties.putAll(getProperties());
                properties.put(TOPICS_KEY, topic);
                configs.add(SourceConfig.getConfig(properties));
            }
        }
        return configs;
    }

    @Override
    public String getSwimlaneId() {
        return StringUtils.join(topics, "_");
    }

    @Override
    protected boolean doCheck() {
        return !topics.isEmpty() && (partition == 0 || (partition > 0 && topics.size() == 1));
    }
    @Override
    public Map<String, Class> getInstance() {
        return new HashMap<String, Class>() {
            {
                put(KafkaConsumerConst.CONSUMER_PLUGIN_NAME.getCode(), KafkaClient.class);
            }
        };
    }
}