/**
 *
 */
package cn.vbill.middleware.porter.manager.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 输入框类型
 *
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum InputTypeEnum {

    TEXT("TEXT", "文本框"), RADIO("RADIO", "单选框");

    @Getter
    private final String code;
    @Getter
    private final String name;

    public static final Map<String, Object> LINKMAP = new LinkedHashMap<String, Object>() {

        private static final long serialVersionUID = 1L;

        {
            put(TEXT.code, TEXT.name);
            put(RADIO.code, RADIO.name);
        }
    };

}
