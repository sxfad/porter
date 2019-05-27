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

package cn.vbill.middleware.porter.common.util.db;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 完成字符串到java.sql.*格式的转换
 */
public class SqlUtils {
    private static final Map<Integer, Class<?>> SQL_TYPE_TO_JAVA_TYPE_MAP = new HashMap<Integer, Class<?>>();
    private static final ConvertUtilsBean CONVERT_UTILS_BEAN = new ConvertUtilsBean();

    static {
        // regist Converter
        CONVERT_UTILS_BEAN.register(SqlTimestampConverter.SQL_TIMESTAMP, java.sql.Date.class);
        CONVERT_UTILS_BEAN.register(SqlTimestampConverter.SQL_TIMESTAMP, java.sql.Time.class);
        CONVERT_UTILS_BEAN.register(SqlTimestampConverter.SQL_TIMESTAMP, java.sql.Timestamp.class);
        CONVERT_UTILS_BEAN.register(ByteArrayConverter.SQL_BYTES, byte[].class);

        // bool
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.BOOLEAN, Boolean.class);

        // int
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.TINYINT, Integer.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.SMALLINT, Integer.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.INTEGER, Integer.class);

        // long
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.BIGINT, Long.class);
        // mysql bit最多64位，无符号
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.BIT, BigInteger.class);

        // decimal
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.REAL, Float.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.FLOAT, Float.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.DOUBLE, Double.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.NUMERIC, BigDecimal.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.DECIMAL, BigDecimal.class);

        // date
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.DATE, java.sql.Date.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.TIME, java.sql.Time.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.TIMESTAMP, java.sql.Timestamp.class);

        // blob
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.BLOB, byte[].class);

        // byte[]
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.REF, byte[].class);

        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.ARRAY, byte[].class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.STRUCT, byte[].class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.SQLXML, byte[].class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.BINARY, byte[].class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.DATALINK, byte[].class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.DISTINCT, byte[].class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.VARBINARY, byte[].class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.JAVA_OBJECT, byte[].class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.LONGVARBINARY, byte[].class);

        // String
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.CHAR, String.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.VARCHAR, String.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.LONGVARCHAR, String.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.LONGNVARCHAR, String.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.NCHAR, String.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.NVARCHAR, String.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.NCLOB, String.class);
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.CLOB, String.class);

        //无法识别的other类型默认转换为字符串类型
        SQL_TYPE_TO_JAVA_TYPE_MAP.put(Types.OTHER, String.class);
    }

    /**
     * sqlValueToString方法的逆向过程
     *
     * @param value
     * @param sqlType
     * @param isEmptyStringNulled
     * @return
     */
    public static Object stringToSqlValue(String value, int sqlType, boolean isRequired, boolean isEmptyStringNulled) {
        // 设置变量
        String sourceValue = value;
        if (SqlUtils.isTextType(sqlType)) {
            if ((sourceValue == null) || (StringUtils.isEmpty(sourceValue) && isEmptyStringNulled)) {
                return isRequired ? " " : null;
            } else {
                return sourceValue;
            }
        } else {
            if (StringUtils.isEmpty(sourceValue)) {
                // oracle的返回null，保持兼容
                return isEmptyStringNulled ? null : sourceValue;
            } else {
                Class<?> requiredType = SQL_TYPE_TO_JAVA_TYPE_MAP.get(sqlType);
                if (requiredType == null) {
                    throw new IllegalArgumentException("unknow java.sql.Types - " + sqlType);
                } else if (requiredType.equals(String.class)) {
                    return sourceValue;
                } else if (isNumeric(sqlType)) {
                    return CONVERT_UTILS_BEAN.convert(sourceValue.trim(), requiredType);
                } else {
                    return CONVERT_UTILS_BEAN.convert(sourceValue, requiredType);
                }
            }
        }
    }


    /**
     * Check whether the given SQL type is numeric.
     */
    private static boolean isNumeric(int sqlType) {
        return (Types.BIT == sqlType) || (Types.BIGINT == sqlType) || (Types.DECIMAL == sqlType)
                || (Types.DOUBLE == sqlType) || (Types.FLOAT == sqlType) || (Types.INTEGER == sqlType)
                || (Types.NUMERIC == sqlType) || (Types.REAL == sqlType) || (Types.SMALLINT == sqlType)
                || (Types.TINYINT == sqlType);
    }

    /**
     * isTextType
     * @param sqlType
     * @return
     */
    private static boolean isTextType(int sqlType) {
        return sqlType == Types.CHAR || sqlType == Types.VARCHAR || sqlType == Types.CLOB || sqlType == Types.LONGVARCHAR
                || sqlType == Types.NCHAR || sqlType == Types.NVARCHAR || sqlType == Types.NCLOB
                || sqlType == Types.LONGNVARCHAR;
    }
}