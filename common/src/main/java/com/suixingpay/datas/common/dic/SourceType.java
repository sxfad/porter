/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 18:07
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据源类型
 * 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 18:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 18:07
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SourceType {
    ZOOKEEPER("ZOOKEEPER", "zookeeper"), KAFKA("KAFKA", "kafka"), JDBC("JDBC", "zookeeper"), EMAIL("EMAIL",
            "email"), NAME_SOURCE("NAME_SOURCE", "nameSource"), KUDU("KUDU", "kudu"), CANAL("CANAL", "canal");
    @Getter
    private final String code;
    @Getter
    private final String name;

    public static final List<SourceType> TYPES = new ArrayList<SourceType>() {
        {
            add(ZOOKEEPER);
            add(KAFKA);
            add(JDBC);
            add(EMAIL);
            add(CANAL);
            add(KUDU);
            add(NAME_SOURCE);
        }
    };

    public static final HashMap<String, Object> PLUGMAP = new HashMap<String, Object>() {
        {
            put(ZOOKEEPER.code, ZOOKEEPER.name);
            put(KAFKA.code, KAFKA.name);
            put(JDBC.code, JDBC.name);
            put(EMAIL.code, EMAIL.name);
            put(CANAL.code, CANAL.name);
            put(KUDU.code, KUDU.name);
            put(NAME_SOURCE.code, NAME_SOURCE.name);
        }
    };
}
