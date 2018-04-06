/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config.source;

import com.alibaba.fastjson.annotation.JSONField;
import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.dic.SourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class KafkaConfig  extends SourceConfig {
    @JSONField(serialize = false, deserialize = false)
    private static final String TOPIC_SPLIT_CHARACTER = ",";
    @Setter @Getter private String servers;
    @Setter @Getter private String group;
    @Setter @Getter private List<String> topics;
    @Setter @Getter private int pollTimeOut  = 30000;
    @Setter @Getter private int oncePollSize = 1000;
    @Setter @Getter private int onceCommitInterval = 1000;
    @Setter @Getter private String firstConsumeFrom = "earliest";
    @Setter @Getter private boolean autoCommit = Boolean.FALSE;

    public   KafkaConfig() {
        sourceType = SourceType.KAFKA;
    }

    @Override
    protected void childStuff() {
        String topicStr = getProperties().getOrDefault("topics", "");
        topics = Arrays.stream(topicStr.split(TOPIC_SPLIT_CHARACTER)).collect(Collectors.toList());
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {"topics"};
    }
}
