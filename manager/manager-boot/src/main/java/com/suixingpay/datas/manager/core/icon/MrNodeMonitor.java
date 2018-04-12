/**
 * 
 */
package com.suixingpay.datas.manager.core.icon;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class MrNodeMonitor {

    /**
     * 时间字符串
     */
    private String[] xAxisData;

    /**
     * 告警次数
     */
    private String[] alarmNumber;

    /**
     * tps数据
     */
    private String[] tps;

    public String[] getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(String[] alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public String[] getTps() {
        return tps;
    }

    public void setTps(String[] tps) {
        this.tps = tps;
    }

    public String[] getxAxisData() {
        return xAxisData;
    }

    public void setxAxisData(String[] xAxisData) {
        this.xAxisData = xAxisData;
    }
}
