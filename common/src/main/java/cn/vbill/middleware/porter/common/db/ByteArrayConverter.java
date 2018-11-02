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

package cn.vbill.middleware.porter.common.db;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.ByteConverter;

/**
 * 接入eromanga后，需要特殊处理下byte类型数据，先尝试byte[]处理
 */
public class ByteArrayConverter implements Converter {

    /**
     * SQL_BYTES
     */
    public static final Converter SQL_BYTES = new ByteArrayConverter(null);
    private static final Converter CONVERTER = new ArrayConverter(byte[].class, new ByteConverter());

    protected final Object defaultValue;
    protected final boolean useDefault;

    public ByteArrayConverter(Object defaultValue) {
        this.defaultValue = defaultValue;
        this.useDefault = true;
    }

    /**
     * convert
     * @param type
     * @param value
     * @return
     */
    public Object convert(Class type, Object value) {
        if (value == null) {
            if (useDefault) {
                return defaultValue;
            } else {
                throw new ConversionException("No value specified");
            }
        }

        if (value instanceof byte[]) {
            return value;
        }
        if (value instanceof String) {
            try {
                return ((String) value).getBytes("ISO-8859-1");
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }

        return CONVERTER.convert(type, value);
    }
}