package com.suixingpay.datas.manager.core.util;

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

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) APPLICATIONCONTEXT.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) APPLICATIONCONTEXT.getBean(clazz);
    }

    public static void cleanApplicationContext() {
        APPLICATIONCONTEXT = null;
    }

    private static void checkApplicationContext() {
        if (APPLICATIONCONTEXT == null) {
            throw new IllegalStateException("applicaitonContext未注入");
        }
    }
}
