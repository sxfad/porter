/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.data;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 13:45
 */
public class DTaskLock extends DObject {
    private String taskId;
    private String nodeId;
    private String topic;
    public DTaskLock() {

    }
    public DTaskLock(String taskId, String nodeId, String topic) {
        this.taskId = taskId;
        this.nodeId = nodeId;
        this.topic = topic;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public <T> void merge(T data) {
        DTaskLock lock = (DTaskLock) data;
        if (taskId.equals(lock.getTaskId()) && lock.getTopic().equals(topic)) {
            if (!StringUtils.isBlank(lock.nodeId)) this.nodeId = lock.nodeId;
        }
    }
}
