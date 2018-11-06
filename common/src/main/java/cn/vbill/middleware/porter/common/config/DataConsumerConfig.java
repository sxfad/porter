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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 16:43
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月03日 16:43
 */
@NoArgsConstructor
public class DataConsumerConfig {

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


    //空查询通知间隔,单位秒
    @Setter @Getter private  long emptyFetchNoticeSpan = 60L * 60;

    //空查询通知时间阀值
    @Setter @Getter private  long emptyFetchThreshold = 60L * 60;

    //初始消费位置
    @Getter
    @Setter
    private String offset;

}