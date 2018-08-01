/**
 *
 */
package cn.vbill.middleware.manager.test;

import cn.vbill.middleware.manager.BaseTest;
import cn.vbill.middleware.porter.manager.core.icon.HomeBlockResult;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.service.impl.MonitorScheduledServiceImpl;
import cn.vbill.middleware.porter.manager.service.HomeService;
import cn.vbill.middleware.porter.manager.service.MonitorScheduledService;
import cn.vbill.middleware.porter.manager.service.impl.HomeServiceImpl;
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



















