/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 10:32
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
 * 节点健康级别
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年04月02日 10:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 10:32
 */

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum NodeHealthLevel {

    GREEN("GREEN", "正常"), YELLOW("YELLOW", "需要关注"), RED("RED", "异常");
    @Getter
    private final String code;
    @Getter
    private final String name;

    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<String, Object>() {

        private static final long serialVersionUID = 1L;

        {
            put(GREEN.code, GREEN.name);
            put(YELLOW.code, YELLOW.name);
            put(RED.code, RED.name);
        }
    };
}
