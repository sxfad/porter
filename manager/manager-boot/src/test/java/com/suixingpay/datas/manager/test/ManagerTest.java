/**
 * 
 */
package com.suixingpay.datas.manager.test;

import com.suixingpay.datas.manager.core.util.ApplicationContextUtil;
import com.suixingpay.datas.manager.core.util.DateMathUtils;
import com.suixingpay.datas.manager.service.MrJobTasksMonitorService;
import com.suixingpay.datas.manager.service.MrLogMonitorService;
import com.suixingpay.datas.manager.service.MrNodesScheduleService;
import com.suixingpay.datas.manager.service.impl.MrJobTasksMonitorServiceImpl;
import com.suixingpay.datas.manager.service.impl.MrLogMonitorServiceImpl;
import com.suixingpay.datas.manager.service.impl.MrNodesScheduleServiceImpl;
import org.junit.Test;

import com.suixingpay.datas.manager.BaseTest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class ManagerTest extends BaseTest {

    @Test
    public void test() {
        System.out.println(111);
    }

    @Test
    public void testCreateTable() {

        MrNodesScheduleService mrNodesScheduleService = ApplicationContextUtil.getBean(MrNodesScheduleServiceImpl.class);
        MrLogMonitorService mrLogMonitorService = ApplicationContextUtil.getBean(MrLogMonitorServiceImpl.class);
        MrJobTasksMonitorService mrJobTasksMonitorService = ApplicationContextUtil.getBean(MrJobTasksMonitorServiceImpl.class);

        //获取当前时间 并计算出前天的时间
        Date date = DateMathUtils.dateAddDays(new Date(), -2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String newDate = sdf.format(date);
        String nowDate = sdf.format(DateMathUtils.getDate());
        //根据前天时间 转移数据到新表 准备：新表表名 -- 表_yyyyMMdd
        String mrNodesScheduleName = "mr_nodes_schedule_" + nowDate;

        String mrLogMonitorName = "mr_log_monitor_" + nowDate;
        mrLogMonitorService.createTable(mrLogMonitorName, newDate);
        String mrJobTasksMonitorName = "mr_job_tasks_monitor_" + nowDate;
        mrJobTasksMonitorService.createTable(mrJobTasksMonitorName, newDate);
    }

    @Test
    public void testDrop() {

        MrNodesScheduleService mrNodesScheduleService = ApplicationContextUtil.getBean(MrNodesScheduleServiceImpl.class);
        MrLogMonitorService mrLogMonitorService = ApplicationContextUtil.getBean(MrLogMonitorServiceImpl.class);
        MrJobTasksMonitorService mrJobTasksMonitorService = ApplicationContextUtil.getBean(MrJobTasksMonitorServiceImpl.class);

        Date date = DateMathUtils.dateAddDays(new Date(), -30);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String newDate = sdf.format(date);
        System.out.println(newDate);
        String mrLogMonitorName = "mr_log_monitor_20180424";
        mrLogMonitorService.dropTable(mrLogMonitorName);
    }
}
