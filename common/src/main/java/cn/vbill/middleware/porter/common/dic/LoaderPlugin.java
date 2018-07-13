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

    JDBC_BATCH("JdbcBatch", "JDBC批量"),
    JDBC_SINGLE("JdbcSingle", "JDBC单行"),
    KUDU_NATIVE("KUDU_NATIVE", "kudu单行"),
    JDBC_SQL_PRINT("JDBC_SQL_PRINT", "SQL打印测试"),
    HBASE_NATIVE("HBASE_NATIVE", "HBase原生"),
    KAFKA_SYNC("KAFKA_SYNC", "kafka同步"),
    KAFKA_ASYNC("KAFKA_ASYNC", "kafka异步");

    @Getter private final String code;
    @Getter private final String name;

    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<String, Object>() {

        private static final long serialVersionUID = 1L;

        {
            put("JDBC_BATCH", JDBC_BATCH.name);
            put("JDBC_SINGLE", JDBC_SINGLE.name);
            put(JDBC_SINGLE.code, KUDU_NATIVE.name);
            put(JDBC_SQL_PRINT.code, JDBC_SQL_PRINT.name);
            put(HBASE_NATIVE.code, HBASE_NATIVE.name);
            put(KAFKA_SYNC.code, KAFKA_SYNC.name);
            put(KAFKA_ASYNC.code, KAFKA_ASYNC.name);
        }
    };
}
