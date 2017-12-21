package com.suixingpay.datas.common.cluster.command;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 18:42
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 18:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 18:42
 */
public class TaskStatCommand implements ClusterCommand {
    private final String taskId;
    private final String topic;
    private final long insertCount;
    private final long updateCount;
    private final long deleteCount;
    private final long errorCount;
    private final long mayLostCount;

    public TaskStatCommand(String taskId, String topic, long insertCount, long updateCount, long deleteCount, long errorCount, long mayLostCount) {
        this.taskId = taskId;
        this.topic = topic;
        this.insertCount = insertCount;
        this.updateCount = updateCount;
        this.deleteCount = deleteCount;
        this.errorCount = errorCount;
        this.mayLostCount = mayLostCount;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTopic() {
        return topic;
    }

    public long getInsertCount() {
        return insertCount;
    }

    public long getUpdateCount() {
        return updateCount;
    }

    public long getDeleteCount() {
        return deleteCount;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public long getMayLostCount() {
        return mayLostCount;
    }
}
