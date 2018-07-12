/**
 *
 */
package cn.vbill.middleware.porter.manager.core.icon;

import cn.vbill.middleware.porter.manager.core.entity.MrNodesMonitor;

import java.util.Date;
import java.util.List;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class MrNodeMonitor {

    public MrNodeMonitor(List<MrNodesMonitor> list) {

        int size = list.size();

        /**
         * 时间字符串
         */
        this.xAxisData = new Date[size];

        /**
         * 告警次数
         */
        this.alarmNumber = new Long[size];

        /**
         * tps数据
         */
        this.tps = new Long[size];

        for (int i = 0; i < list.size(); i++) {
            /**
             * 时间字符串
             */
            this.xAxisData[i] = list.get(i).getMonitorDate();
            /**
             * 告警次数
             */
            this.alarmNumber[i] = list.get(i).getMonitorAlarm();
            /**
             * tps数据
             */
            this.tps[i] = list.get(i).getMonitorTps();
        }
    }

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