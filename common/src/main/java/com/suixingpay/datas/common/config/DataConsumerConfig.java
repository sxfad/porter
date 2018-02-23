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
    @Getter @Setter private String consumerName;
    @Getter @Setter private String converter;

    /*
     *过滤掉不期望处理的表
     * 与includes同时配置时，该设置不生效
     *schema.table,schema.table
     */
    @Getter @Setter private String excludes;

    /*
     *期望处理的表
     *schema.table,schema.table
     */
    @Getter @Setter private String includes;

    //消费数据的数据源
    @Getter @Setter private Map<String,String> source;

    //元数据查询的数据源
    //公用配置
    @Getter @Setter private Map<String,String> metaSource;
}
