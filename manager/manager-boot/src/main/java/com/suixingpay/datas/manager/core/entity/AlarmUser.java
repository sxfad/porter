package com.suixingpay.datas.manager.core.entity;

/**
 * 告警用户关联表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class AlarmUser implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 告警信息表id.
     */
    private Long alarmId;

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
     * 告警信息表id get方法.
     */
    public Long getAlarmId() {
        return alarmId;
    }

    /**
     * 告警信息表id set方法.
     */
    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
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
