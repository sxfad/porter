package com.suixingpay.datas.common.cluster.zookeeper.data;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 13:45
 */
public class DTaskStat  extends DObject {
    private String taskId;
    private String nodeId;
    private String topic;
    private AtomicLong insertRow = new AtomicLong(0);
    private AtomicLong updateRow = new AtomicLong(0);
    private AtomicLong deleteRow = new AtomicLong(0);
    private AtomicLong maylostRow = new AtomicLong(0);
    public DTaskStat() {

    }
    public DTaskStat(String taskId, String nodeId, String topic) {
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

    public AtomicLong getInsertRow() {
        return insertRow;
    }

    public void setInsertRow(AtomicLong insertRow) {
        this.insertRow = insertRow;
    }

    public AtomicLong getUpdateRow() {
        return updateRow;
    }

    public void setUpdateRow(AtomicLong updateRow) {
        this.updateRow = updateRow;
    }

    public AtomicLong getDeleteRow() {
        return deleteRow;
    }

    public void setDeleteRow(AtomicLong deleteRow) {
        this.deleteRow = deleteRow;
    }

    public AtomicLong getMaylostRow() {
        return maylostRow;
    }

    public void setMaylostRow(AtomicLong maylostRow) {
        this.maylostRow = maylostRow;
    }
}
