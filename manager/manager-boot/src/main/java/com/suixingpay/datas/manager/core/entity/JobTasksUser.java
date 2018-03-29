package com.suixingpay.datas.manager.core.entity;

/**
 * job_tasks_user 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class JobTasksUser implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 任务id.
     */
    private Long jobTaskId;

    /**
     * 用户id.
     */
    private Long userId;

    /**
     * 主键 get方法.
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键 set方法.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 任务id get方法.
     */
    public Long getJobTaskId() {
        return jobTaskId;
    }

    /**
     * 任务id set方法.
     */
    public void setJobTaskId(Long jobTaskId) {
        this.jobTaskId = jobTaskId;
    }

    /**
     * 用户id get方法.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 用户id set方法.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
