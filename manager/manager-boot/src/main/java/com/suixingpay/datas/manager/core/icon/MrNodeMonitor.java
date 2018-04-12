/**
 * 
 */
package com.suixingpay.datas.manager.core.icon;

import java.util.Date;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class MrNodeMonitor {

    /**
     * 时间字符串
     */
    private Date[] xAxisData;

    /**
     * 告警次数
     */
    private Long[] alarmNumber;

    /**
     * tps数据
     */
    private Long[] tps;

    public Date[] getxAxisData() {
        return xAxisData;
    }

    public void setxAxisData(Date[] xAxisData) {
        this.xAxisData = xAxisData;
    }

    public Long[] getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(Long[] alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public Long[] getTps() {
        return tps;
    }

    public void setTps(Long[] tps) {
        this.tps = tps;
    }
}