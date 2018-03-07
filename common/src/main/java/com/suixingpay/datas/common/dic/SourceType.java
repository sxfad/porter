/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 18:07
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据源类型
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 18:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 18:07
 */
@AllArgsConstructor
public enum SourceType {
    ZOOKEEPER("ZOOKEEPER", "zookeeper"),
    KAFKA("KAFKA", "kafka"),
    JDBC("JDBC", "zookeeper"),
    EMAIL("EMAIL", "email"),
    NAME_SOURCE("NAME_SOURCE", "nameSource"),
    CANAL("CANAL", "canal");
    @Getter private final String code;
    @Getter private final String name;

    public static final List<SourceType> TYPES = new ArrayList<SourceType>() {
        {
            add(ZOOKEEPER);
            add(KAFKA);
            add(JDBC);
            add(EMAIL);
            add(CANAL);
            add(NAME_SOURCE);
        }
    };
}
