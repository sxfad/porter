package cn.vbill.middleware.porter.manager.core.init;

import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.service.MonitorScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: murasakiseifu
 * @date: 2019-05-29 10:15}
 * @version: V1.0
 * @review: murasakiseifu/2019-05-29 10:15}
 */
public class MonitorUtils {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * INSTANCE
     */
    public static MonitorUtils INSTANCE;

    /**
     * getInstance
     *
     * @return
     */
    public static MonitorUtils getInstance() {
        if (null == INSTANCE) {
            return new MonitorUtils();
        }
        return INSTANCE;
    }

    /**
     * init
     */
    public void init() {
        logger.info("MonitorUtils init");
        createMonitorTable();
    }

    private void createMonitorTable() {
        try {
            MonitorScheduledService monitorScheduledService = ApplicationContextUtil.getBean(MonitorScheduledService.class);
            monitorScheduledService.initMonitorTable();
        } catch (Exception e) {
            logger.error("MonitorUtils 初始化异常...！！！异常信息：[{}]", e);
        }
    }
}
