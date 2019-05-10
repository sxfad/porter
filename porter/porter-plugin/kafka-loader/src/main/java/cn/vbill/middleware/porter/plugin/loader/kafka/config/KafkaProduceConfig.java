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

package cn.vbill.middleware.porter.plugin.loader.kafka.config;

import cn.vbill.middleware.porter.common.plugin.config.PluginServiceConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.plugin.loader.kafka.KafkaLoaderConst;
import cn.vbill.middleware.porter.plugin.loader.kafka.client.KafkaProduceClient;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class KafkaProduceConfig extends SourceConfig implements PluginServiceConfig {
    //服务器列表
    @Setter @Getter private String servers;
    //自动生成
    @Setter @Getter private String group;
    //主题 只能一个
    @Setter @Getter private String topic;
    //不输入
    @Setter @Getter private boolean transaction = false;
    //是否格式化为ogg json格式
    @Setter @Getter private boolean oggJson = true;
    //重试次数
    @Setter @Getter private int retries = 3;
    //分片字段名
    //schema.表名->字段名1,字段名2
    @Setter @Getter private Map<String, String> partitionKey = new HashMap<>();

    @Override
    protected void childStuff() {
        String partitionKeyName = "partitionKey.";
        getProperties().entrySet().stream().filter(e -> e.getKey().startsWith(partitionKeyName)).forEach(e -> {
            partitionKey.put(e.getKey().substring(partitionKeyName.length()), e.getValue());
        });
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[0];
    }



    @Override
    protected boolean doCheck() {
        return true;
    }

    @Override
    public Map<String, Class> getInstance() {
        return new HashMap<String, Class>() {
            {
                put(KafkaLoaderConst.LOADER_SOURCE_TYPE_NAME.getCode(), KafkaProduceClient.class);
            }
        };
    }
}
