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
    ZOOKEEPER("ZOOKEEPER", "zookeeper", -1),

    /**
     * kafka
     */
    KAFKA("KAFKA", "kafka", 1),

    /**
     * jdbc
     */
    JDBC("JDBC", "jdbc", 1),

    /**
     * email
     */
    EMAIL("EMAIL", "email", -1),

    /**
     * nameSource
     */
    NAME_SOURCE("NAME_SOURCE", "nameSource", -1),

    /**
     * canal
     */
    CANAL("CANAL", "canal", 1),

    /**
     * kafkaProduce
     */
    KAFKA_PRODUCE("KAFKA_PRODUCE", "kafkaProduce", 1),

    FILE("FILE", "file", 1),

    JDBC_CONSUME("JDBC_CONSUME", "jdbcConsume", 1);

    /**
     * LINKMAP
     */
    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<>();

    static {
        if (ZOOKEEPER.state == 1) {
            LINKMAP.put(ZOOKEEPER.code, ZOOKEEPER.name);
        }

        if (KAFKA.state == 1) {
            LINKMAP.put(KAFKA.code, KAFKA.name);
        }

        if (JDBC.state == 1) {
            LINKMAP.put(JDBC.code, JDBC.name);
        }

        if (EMAIL.state == 1) {
            LINKMAP.put(EMAIL.code, EMAIL.name);
        }

        if (CANAL.state == 1) {
            LINKMAP.put(CANAL.code, CANAL.name);
        }

        if (NAME_SOURCE.state == 1) {
            LINKMAP.put(NAME_SOURCE.code, NAME_SOURCE.name);
        }

        if (KAFKA_PRODUCE.state == 1) {
            LINKMAP.put(KAFKA_PRODUCE.code, KAFKA_PRODUCE.name);
        }
        if (FILE.state == 1) {
            LINKMAP.put(FILE.code, FILE.name);
        }
        if (JDBC_CONSUME.state == 1) {
            LINKMAP.put(JDBC_CONSUME.code, JDBC_CONSUME.name);
        }
    }

    @Getter
    private final String code;
    @Getter
    private final String name;
    @Getter
    private final int state;

}
