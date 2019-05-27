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

package cn.vbill.middleware.porter.core.message.converter;

import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 14:51
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月05日 14:51
 */
public enum  ConverterFactory {

    /**
     * INSTANCE
     */
    INSTANCE();

    private final List<EventConverter> converters;

    ConverterFactory() {
        /**
         * 自定义Converter插件由JavaFileCompiler载入，故需要JavaFileCompiler作为ClassLoader
         */
        converters = SpringFactoriesLoader.loadFactories(EventConverter.class, JavaFileCompiler.getInstance());
    }

    /**
     * 获取Converter
     *
     * @date 2018/8/8 下午5:56
     * @param: [name]
     * @return: cn.vbill.middleware.porter.core.message.converter.EventConverter
     */
    public  EventConverter getConverter(String name) {
        if (StringUtils.isBlank(name)) return null;
        for (EventConverter converter : converters) {
            if (converter.getName().equals(name)) {
                return converter;
            }
        }
        return null;
    }
}
