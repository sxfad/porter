/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月07日 10:00
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 载入器插件
 * 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月07日 10:00
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月07日 10:00
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LoaderPlugin {

    JDBC_BATCH("JdbcBatch", "JDBC批量"), JDBC_SINGLE("JdbcSingle", "JDBC单行"), KUDU_SINGLE("KuduSingle", "kudu单行");

    @Getter
    private final String code;
    @Getter
    private final String name;

    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<String, Object>() {

        private static final long serialVersionUID = 1L;

        {
            put(JDBC_BATCH.code, JDBC_BATCH.name);
            put(JDBC_SINGLE.code, JDBC_SINGLE.name);
            put(KUDU_SINGLE.code, KUDU_SINGLE.name);
        }
    };
}
