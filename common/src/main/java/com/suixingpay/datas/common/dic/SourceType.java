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

import java.util.HashMap;
import java.util.LinkedHashMap;

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

    ZOOKEEPER("ZOOKEEPER", "zookeeper", -1), KAFKA("KAFKA", "kafka", 1), JDBC("JDBC", "jdbc", 1), EMAIL("EMAIL", "email", -1),
    NAME_SOURCE("NAME_SOURCE", "nameSource", -1), KUDU("KUDU", "kudu", -1), CANAL("CANAL", "canal", 1);

    @Getter
    private final String code;
    @Getter
    private final String name;
    @Getter
    private final int state;

    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<String, Object>() {

        private static final long serialVersionUID = 1L;

        {
            if (ZOOKEEPER.state == 1)
                put(ZOOKEEPER.code, ZOOKEEPER.name);
            if (KAFKA.state == 1)
                put(KAFKA.code, KAFKA.name);
            if (JDBC.state == 1)
                put(JDBC.code, JDBC.name);
            if (EMAIL.state == 1)
                put(EMAIL.code, EMAIL.name);
            if (CANAL.state == 1)
                put(CANAL.code, CANAL.name);
            if (KUDU.state == 1)
                put(KUDU.code, KUDU.name);
            if (NAME_SOURCE.state == 1)
                put(NAME_SOURCE.code, NAME_SOURCE.name);

        }
    };
}
