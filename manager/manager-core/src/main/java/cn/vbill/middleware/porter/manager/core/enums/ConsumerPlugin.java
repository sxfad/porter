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

package cn.vbill.middleware.porter.manager.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 消费器插件
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月07日 10:00
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月07日 10:00
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ConsumerPlugin {

    /**
     * CANAL
     */
    CANAL("CanalFetch", "Canal"),

    /**
     * KAFKA
     */
    KAFKA("KafkaFetch", "Kafka"),
    /**
     * JDBC
     */
    JDBC("JdbcFetch", "JDBCConsume");

    /**
     * LINKMAP
     */
    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<>();
    static {
        LINKMAP.put("CANAL", CANAL.name);
        LINKMAP.put("KAFKA", KAFKA.name);
        LINKMAP.put("JDBC", JDBC.name);
    }

    @Getter
    private final String code;
    @Getter
    private final String name;

    public static ConsumerPlugin enumByCode(String code) {
        for (ConsumerPlugin e : ConsumerPlugin.values()) {
            if (e.getCode().equalsIgnoreCase(code)) {
                return e;
            }
        }
        return null;
    }
}
