package com.suixingpay.datas.manager.core.icon;

/**
 *
 * @author: 付紫钲
 * @date: 2018/4/26
 */
public class HomeBlockResult {

    /**
     * 10分钟内计数
     */
    private Integer tenMinutesCount;

    /**
     * 1小时内计数
     */
    private Integer oneHourCount;

    /**
     * 24小时内计数
     */
    private Integer twentyFourHourCount;

    /**
     * 工作中任务计数
     */
    private Integer TasksWorkingCount;

    /**
     * 10分钟内计数 get方法
     */
    public Integer getTenMinutesCount() {
        return tenMinutesCount;
    }

    /**
     * 10分钟内计数 set方法
     */
    public void setTenMinutesCount(Integer tenMinutesCount) {
        this.tenMinutesCount = tenMinutesCount;
    }

    /**
     * 1小时内计数 get方法
     */
    public Integer getOneHourCount() {
        return oneHourCount;
    }

    /**
     * 1小时内计数 set方法
     */
    public void setOneHourCount(Integer oneHourCount) {
        this.oneHourCount = oneHourCount;
    }

    /**
     * 24小时内计数 get方法
     */
    public Integer getTwentyFourHourCount() {
        return twentyFourHourCount;
    }

    /**
     * 24小时内计数 set方法
     */
    public void setTwentyFourHourCount(Integer twentyFourHourCount) {
        this.twentyFourHourCount = twentyFourHourCount;
    }

    /**
     * 工作中任务计数 get方法
     */
    public Integer getTasksWorkingCount() {
        return TasksWorkingCount;
    }

    /**
     * 工作中任务计数 set方法
     */
    public void setTasksWorkingCount(Integer tasksWorkingCount) {
        TasksWorkingCount = tasksWorkingCount;
    }
}
