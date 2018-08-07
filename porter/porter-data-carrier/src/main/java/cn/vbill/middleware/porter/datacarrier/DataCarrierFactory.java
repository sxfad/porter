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

package cn.vbill.middleware.porter.datacarrier;

import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import cn.vbill.middleware.porter.datacarrier.simple.SimpleDataCarrier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 19:23
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 19:23
 */
@Component
@Scope("singleton")
public class DataCarrierFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCarrierFactory.class);

    private final Class provider;
    public DataCarrierFactory() {
        List<String> clazz = SpringFactoriesLoader.loadFactoryNames(DataCarrier.class, JavaFileCompiler.getInstance());
        String providerString = clazz.get(0);
        Class tmpProvider = null;
        if (!StringUtils.isBlank(providerString)) {
            try {
                tmpProvider = ClassUtils.forName(providerString, DataCarrierFactory.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                tmpProvider = SimpleDataCarrier.class;
                LOGGER.error("%s", e);
            }
        }
        provider = tmpProvider;
    }
    public <E> DataCarrier<E> newDataCarrier(Object... initPrams) {
        Class[] paramsTypes = new Class[initPrams.length];
        for (int i = 0; i < initPrams.length; i++) {
            paramsTypes[i] =  initPrams[i].getClass();
        }
        DataCarrier<E> dc = null;

        //传入构造函数参数
        try {
            Constructor<DataCarrier<E>> constructor = provider.getDeclaredConstructor(paramsTypes);
            ReflectionUtils.makeAccessible(constructor);
            dc = constructor.newInstance(initPrams);
        } catch (Exception e) {
            LOGGER.error("%s", e);
        }

        //默认构造函数
        if (null == dc) {
            try {
                Constructor<DataCarrier<E>> constructor = provider.getDeclaredConstructor();
                ReflectionUtils.makeAccessible(constructor);
                dc = constructor.newInstance();
            } catch (Exception e) {
                LOGGER.error("%s", e);
            }
        }
        return dc;
    }
}
