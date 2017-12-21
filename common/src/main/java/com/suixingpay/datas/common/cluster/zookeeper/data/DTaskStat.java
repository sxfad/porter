package com.suixingpay.datas.common.cluster.zookeeper.data;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

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
    private long insertRow = 0L;
    private long updateRow = 0L;
    private long deleteRow = 0L;
    private long maylostRow = 0L;
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

    public long getInsertRow() {
        return insertRow;
    }

    public void setInsertRow(long insertRow) {
        this.insertRow = insertRow;
    }

    public long getUpdateRow() {
        return updateRow;
    }

    public void setUpdateRow(long updateRow) {
        this.updateRow = updateRow;
    }

    public long getDeleteRow() {
        return deleteRow;
    }

    public void setDeleteRow(long deleteRow) {
        this.deleteRow = deleteRow;
    }

    public long getMaylostRow() {
        return maylostRow;
    }

    public void setMaylostRow(long maylostRow) {
        this.maylostRow = maylostRow;
    }
}
