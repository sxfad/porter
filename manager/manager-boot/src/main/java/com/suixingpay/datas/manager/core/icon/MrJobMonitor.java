/**
 * 
 */
package com.suixingpay.datas.manager.core.icon;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class MrJobMonitor {

    /**
     * 时间字符串
     */
    private String[] xAxisData;

    /**
     * 插入成功.
     */
    private String[] insertSucces;

    /**
     * 插入失败.
     */
    private String[] insertFailure;

    /**
     * 更新成功.
     */
    private String[] updateSucces;

    /**
     * 更新失败.
     */
    private String[] updateFailure;

    /**
     * 删除成功.
     */
    private String[] deleteSucces;

    /**
     * 删除失败.
     */
    private String[] deleteFailure;

    /**
     * 告警次数
     */
    private String[] alarmNumber;

    public String[] getxAxisData() {
        return xAxisData;
    }

    public void setxAxisData(String[] xAxisData) {
        this.xAxisData = xAxisData;
    }

    public String[] getInsertSucces() {
        return insertSucces;
    }

    public void setInsertSucces(String[] insertSucces) {
        this.insertSucces = insertSucces;
    }

    public String[] getInsertFailure() {
        return insertFailure;
    }

    public void setInsertFailure(String[] insertFailure) {
        this.insertFailure = insertFailure;
    }

    public String[] getUpdateSucces() {
        return updateSucces;
    }

    public void setUpdateSucces(String[] updateSucces) {
        this.updateSucces = updateSucces;
    }

    public String[] getUpdateFailure() {
        return updateFailure;
    }

    public void setUpdateFailure(String[] updateFailure) {
        this.updateFailure = updateFailure;
    }

    public String[] getDeleteSucces() {
        return deleteSucces;
    }

    public void setDeleteSucces(String[] deleteSucces) {
        this.deleteSucces = deleteSucces;
    }

    public String[] getDeleteFailure() {
        return deleteFailure;
    }

    public void setDeleteFailure(String[] deleteFailure) {
        this.deleteFailure = deleteFailure;
    }

    public String[] getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(String[] alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

}
