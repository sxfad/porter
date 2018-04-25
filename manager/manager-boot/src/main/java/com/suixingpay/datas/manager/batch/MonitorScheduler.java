package com.suixingpay.datas.manager.batch;

import com.suixingpay.datas.manager.service.MonitorScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author fuzizheng[fu_zz@suixingpay.com]
 */
@Component
public class MonitorScheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MonitorScheduledService monitorScheduledService;

    //每天凌晨2点启动定时器，删除前天以前的数据
    @Scheduled(cron = "0 0 2 * * ? ")
    @Transactional
    public void transferDataScheduler() {
        logger.info("启动定时器，转移删除前天以前的数据");
        monitorScheduledService.transferDataTask();
    }

    //每天凌晨3点启动定时器，删除存在30天以前的表
    @Scheduled(cron = "0 0 3 * * ? ")
    public void dropTableScheduler() {
        logger.info("启动定时器，删除存在30天的表");
        monitorScheduledService.dropTableTask();
    }
}