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

package cn.vbill.middleware.porter.manager.batch;

import cn.vbill.middleware.porter.manager.service.MonitorScheduledService;
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

    /**
     * 每天凌晨2点启动定时器，删除前天以前的数据
     *
     * @author FuZizheng
     * @date 2018/8/9 下午4:15
     * @param: []
     * @return: void
     */
//    @Scheduled(cron = "0 0 2 * * ? ")
//    @Transactional
//    public void transferDataScheduler() {
//        logger.info("启动定时器，转移删除前天以前的数据");
//        monitorScheduledService.transferDataTask();
//    }

    /**
     * 每天凌晨3点启动定时器，删除存在30天以前的表
     *
     * @author FuZizheng
     * @date 2018/8/9 下午4:16
     * @param: []
     * @return: void
     */
    @Scheduled(cron = "0 0 3 * * ? ")
    public void dropTableScheduler() {
        try {
            logger.info("启动定时器，删除存在30天的表");
            monitorScheduledService.dropTableTask();
        } catch (Throwable e) {
            logger.error("删除存在30天的表失败，异常信息:[{}]", e);
        }
    }

    /**
     * 每天凌晨2点启动定时器，生成后天的表，检查明天的表是否生成，没生成则生成
     */
    @Scheduled(cron = "0 0 2 * * ? ")
    @Transactional
    public void createTableScheduler() {
        try {
            logger.info("启动定时器，创建数据库表");
            monitorScheduledService.createTableTask();
        } catch (Throwable e) {
            logger.error("定时任务创建表失败，异常信息:[{}]", e);
        }
    }
}