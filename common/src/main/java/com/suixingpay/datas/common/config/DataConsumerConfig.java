/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 16:43
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 16:43
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月03日 16:43
 */
public class DataConsumerConfig {

    public DataConsumerConfig() {

    }

    /**
     * 后台所用构造函数
     * 
     * @param consumerName
     *            消费插件
     * @param converter
     *            消费转换插件
     * @param includes
     *            期望处理的表 来源表 schema.table,schema.table
     * @param source
     *            消费元数据的数据源
     * @param metaSource
     *            同步数据数据源
     */
    public DataConsumerConfig(String consumerName, String converter, String includes, Map<String, String> source,
            Map<String, String> metaSource, JavaFileConfig eventProcessor) {
        this.consumerName = consumerName;
        this.converter = converter;
        this.includes = includes;
        this.source = source;
        this.metaSource = metaSource;
        this.eventProcessor = eventProcessor;
    }

    // 消费插件
    @Getter
    @Setter
    private String consumerName;

    // 消费转换插件
    @Getter
    @Setter
    private String converter;

    /*
     * 过滤掉不期望处理的表 与includes同时配置时，该设置不生效 schema.table,schema.table
     */
    @Getter
    @Setter
    private String excludes;

    /*
     * 期望处理的表 来源表 schema.table,schema.table
     */
    @Getter
    @Setter
    private String includes;

    // 消费数据的数据源
    @Getter
    @Setter
    private Map<String, String> source;

    // 同步数据查询的数据源 cancel kafka
    // 公用配置
    @Getter
    @Setter
    private Map<String, String> metaSource;

    /**
     * 事件处理器，自定义处理 两种形式： 1.源码内容； 2.class类相对路径，相对于
     */
    @Getter
    @Setter
    private JavaFileConfig eventProcessor;
}