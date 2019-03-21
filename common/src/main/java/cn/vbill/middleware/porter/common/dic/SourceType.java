/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.common.dic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * zookeeper
     */
    ZOOKEEPER("ZOOKEEPER"),
    /**
     * email
     */
    EMAIL("EMAIL"),

    /**
     * nameSource
     */
    NAME_SOURCE("NAME_SOURCE"),

    FILE("FILE");

    @Getter
    private final String code;

    private static Map<String, SourceType> ENUMS = new HashMap<String, SourceType>() {{
            put(ZOOKEEPER.code, ZOOKEEPER);
            put(EMAIL.code, EMAIL);
            put(NAME_SOURCE.code, NAME_SOURCE);
            put(FILE.code, FILE);
    }};

    public static SourceType getSourceType(String code) {
        return ENUMS.getOrDefault(code, null);
    }
}
