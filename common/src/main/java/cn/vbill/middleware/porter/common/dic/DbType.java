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
 * 关系数据库类型
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 16:27
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 16:27
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DbType {

    /**
     * MYSQL
     */
    MYSQL("MYSQL", "MYSQL", "com.mysql.cj.jdbc.Driver"),

    /**
     * ORACLE
     */
    ORACLE("ORACLE", "ORACLE", "oracle.jdbc.driver.OracleDriver"),

    /**
     * DB2
     */
    DB2("DB2", "DB2", "com.ibm.db2.jdbc.app.DB2Driver"),

    /**
     * SQLSERVER
     */
    SQLSERVER("SQLSERVER", "SQLSERVER", "com.microsoft.jdbc.sqlserver.SQLServerDriver"),

    /**
     * SQLSERVER
     */
    POSTGRESQL("POSTGRESQL", "POSTGRESQL", "org.postgresql.Driver");

    /**
     * LINKMAP
     */
    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<>();

    static {
        LINKMAP.put(MYSQL.code, MYSQL.name);
        LINKMAP.put(ORACLE.code, ORACLE.name);
    }

    @Getter
    private final String code;
    @Getter
    private final String name;
    @Getter
    private final String driverName;


    public static DbType match(DbType dbType, String url) {
        if (null != dbType) return dbType;
        if (url.startsWith("jdbc:oracle:thin")) {
            return ORACLE;
        }
        if (url.startsWith("jdbc:db2")) {
            return DB2;
        }
        if (url.startsWith("jdbc:microsoft:sqlserver")) {
            return SQLSERVER;
        }
        if (url.startsWith("jdbc:mysql")) {
            return MYSQL;
        }
        if (url.startsWith("jdbc:postgresql")) {
            return POSTGRESQL;
        }
        return null;
    }
}
