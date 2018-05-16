/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config.source;

import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.dic.SourceType;
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
public class KafkaProduceConfig extends SourceConfig {
    @Setter @Getter private String servers;
    @Setter @Getter private String group;
    @Setter @Getter private String topic;
    //分片字段名
    //schema.表名->字段名1,字段名2
    @Setter @Getter private Map<String, String> partitionKey = new HashMap<>();

    public KafkaProduceConfig() {
        sourceType = SourceType.KAFKA_PRODUCE;
    }

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
}
