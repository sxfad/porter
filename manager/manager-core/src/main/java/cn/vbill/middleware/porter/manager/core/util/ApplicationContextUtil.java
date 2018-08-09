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

package cn.vbill.middleware.porter.manager.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * springboot类beans获取
 *
 * @author: guohongjian[guo_hj@suixingpay.com]
 * @date: 2017年8月10日 下午3:50:49
 * @version: V1.0
 * @review: guohongjian[guo_hj@suixingpay.com]/2017年8月10日 下午3:50:49
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext APPLICATIONCONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.APPLICATIONCONTEXT = applicationContext;
    }

    /**
     * getBean
     *
     * @date 2018/8/9 下午4:01
     * @param: [name]
     * @return: T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) APPLICATIONCONTEXT.getBean(name);
    }

    /**
     * getBean
     *
     * @date 2018/8/9 下午4:01
     * @param: [clazz]
     * @return: T
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) APPLICATIONCONTEXT.getBean(clazz);
    }

    /**
     * cleanApplicationContext
     *
     * @date 2018/8/9 下午4:01
     * @param: []
     * @return: void
     */
    public static void cleanApplicationContext() {
        APPLICATIONCONTEXT = null;
    }

    /**
     * checkApplicationContext
     *
     * @date 2018/8/9 下午4:01
     * @param: []
     * @return: void
     */
    private static void checkApplicationContext() {
        if (APPLICATIONCONTEXT == null) {
            throw new IllegalStateException("applicaitonContext未注入");
        }
    }
}
