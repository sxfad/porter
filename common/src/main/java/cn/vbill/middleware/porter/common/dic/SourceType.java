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

    ZOOKEEPER("ZOOKEEPER", "zookeeper", -1), KAFKA("KAFKA", "kafka", 1), JDBC("JDBC", "jdbc", 1),
    EMAIL("EMAIL", "email", -1), NAME_SOURCE("NAME_SOURCE", "nameSource", -1), KUDU("KUDU", "kudu", -1),
    CANAL("CANAL", "canal", 1), HBASE("HBASE", "hbase", -1), KAFKA_PRODUCE("KAFKA_PRODUCE", "kafkaProduce", 1);

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
            if (HBASE.state == 1)
                put(HBASE.code, HBASE.name);
            if (KAFKA_PRODUCE.state == 1)
                put(KAFKA_PRODUCE.code, KAFKA_PRODUCE.name);

        }
    };
}
