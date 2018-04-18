/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 告警策略
 * 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 11:42
 */

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AlertPlugin {

    EMAIL("EMAIL", "邮件"),

    MOBILE("MOBILE", "手机号");

    @Getter
    private final String code;
    @Getter
    private final String name;

    public static final Map<String, Object> LINKMAP = new LinkedHashMap<String, Object>() {

        private static final long serialVersionUID = 1L;

        {
            put(EMAIL.code, EMAIL.name);
            //put(MOBILE.code, MOBILE.name);
        }
    };

}
