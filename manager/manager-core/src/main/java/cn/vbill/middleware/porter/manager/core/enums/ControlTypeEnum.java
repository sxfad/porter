package cn.vbill.middleware.porter.manager.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 任务权限操作类型
 *
 * @author MurasakiSeiFu
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ControlTypeEnum {

    /**
     * CHANGE
     */
    CHANGE("CHANGE", "移交"),

    /**
     * SHARE
     */
    SHARE("SHAREE", "共享"),

    /**
     * CANCEL
     */
    CANCEL("CANCEL", "作废"),

    /**
     * RECYCLE
     */
    RECYCLE("RECYCLE", "回收");

    @Getter
    private final String code;

    @Getter
    private final String name;

    /**
     * LINKMAP
     */
    public static Map<String, Object> LINKMAP = new LinkedHashMap<String, Object>() {

        private static final long serialVersionUID = 1L;

        {
            put(CHANGE.code, CHANGE.name);
            put(SHARE.code, SHARE.name);
            put(CANCEL.code, CANCEL.name);
            put(RECYCLE.code, RECYCLE.name);
        }
    };
}
