/**
 *
 */
package com.suixingpay.datas.manager.test;

import com.suixingpay.datas.manager.BaseTest;
import com.suixingpay.datas.manager.core.icon.HomeBlockResult;
import com.suixingpay.datas.manager.core.util.ApplicationContextUtil;
import com.suixingpay.datas.manager.service.HomeService;
import com.suixingpay.datas.manager.service.MonitorScheduledService;
import com.suixingpay.datas.manager.service.impl.HomeServiceImpl;
import com.suixingpay.datas.manager.service.impl.MonitorScheduledServiceImpl;
import org.junit.Test;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class ManagerTest extends BaseTest {

    @Test
    public void test() {
        System.out.println(111);
    }

    /**
     * entryData.getKey():log_date
     * entry.getKey():mr_log_monitor
     * entry.getValue()mr_log_monitor_20180425
     * <p>
     * entryData.getKey():monitor_date
     * entry.getKey():mr_job_tasks_monitor
     * entry.getValue()mr_job_tasks_monitor_20180425
     * <p>
     * entryData.getKey():monitor_date
     * entry.getKey():mr_nodes_monitor
     * entry.getValue()mr_nodes_monitor_20180425
     */
    @Test
    public void test1() {

        MonitorScheduledService monitorScheduledService = ApplicationContextUtil.getBean(MonitorScheduledServiceImpl.class);
        monitorScheduledService.dropTableTask();
    }

    @Test
    public void test2() {
        HomeService homeService = ApplicationContextUtil.getBean(HomeServiceImpl.class);
        HomeBlockResult homeBlockResult = homeService.bolck();

    }
}



















