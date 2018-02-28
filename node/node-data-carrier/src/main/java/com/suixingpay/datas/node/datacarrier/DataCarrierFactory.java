/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 19:23
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.datacarrier;

import com.suixingpay.datas.node.datacarrier.simple.SimpleDataCarrier;
import org.apache.commons.lang3.StringUtils;
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
    private final Class provider;
    public DataCarrierFactory() {
        List<String> clazz = SpringFactoriesLoader.loadFactoryNames(DataCarrier.class, null);
        String providerString = clazz.get(0);
        Class tmpProvider = null;
        if (!StringUtils.isBlank(providerString)) {
            try {
                tmpProvider = ClassUtils.forName(providerString, DataCarrierFactory.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                tmpProvider = SimpleDataCarrier.class;
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
        }

        //默认构造函数
        if (null == dc) {
            try {
                Constructor<DataCarrier<E>> constructor = provider.getDeclaredConstructor();
                ReflectionUtils.makeAccessible(constructor);
                dc = constructor.newInstance();
            } catch (Exception e) {
            }
        }
        return dc;
    }
}
