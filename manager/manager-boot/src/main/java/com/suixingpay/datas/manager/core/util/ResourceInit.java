/**
 * 
 */
package com.suixingpay.datas.manager.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@Component
public class ResourceInit implements CommandLineRunner {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(String... args) throws Exception {
        logger.info("ResourceInit run...");
        ResourceUtils.getInstance().init();
    }

}
