package com.suixingpay.datas.manager.web.config;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基类
 */
public class CommonController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 判断对象是否为空,如果为空将抛出 {@link NotFoundException}
     *
     * @param obj
     * @param msg
     * @throws NotFoundException
     */
    protected void assertFound(Object obj, String msg) throws NotFoundException {
        if (obj == null)
            throw new NotFoundException(msg);
    }
}
