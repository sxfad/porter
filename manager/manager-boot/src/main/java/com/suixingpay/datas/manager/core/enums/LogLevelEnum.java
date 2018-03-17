package com.suixingpay.datas.manager.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 付紫钲
 * @date: 2018/3/16
 * @copyright: ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LogLevelEnum {

    DEBUG("DEBUG", "DEBUG"),
    INFO("INFO", "INFO"),
    ERROR("ERROR", "ERROR");

    @Getter
    private final String code;

    @Getter
    private final String name;

    public static final Map<String, Object> LINKMAP = new LinkedHashMap<String, Object>() {

        private static final long serialVersionUID = 1L;

        {
            put(DEBUG.code, DEBUG.name);
            put(INFO.code, INFO.name);
            put(ERROR.code, ERROR.name);
        }
    };
}
