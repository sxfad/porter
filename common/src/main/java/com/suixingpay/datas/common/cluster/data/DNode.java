package com.suixingpay.datas.common.cluster.data;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.util.MachineUtils;

import java.util.*;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 13:45
 */
public class DNode extends DObject {
    private String nodeId;
    private Date heartbeat;
    private String address = MachineUtils.IP_ADDRESS;
    private String hostName = MachineUtils.HOST_NAME;
    private String processId = MachineUtils.CURRENT_JVM_PID + "";
    private Map<String,List<String>> tasks;
    public DNode() {
        heartbeat = new Date();
        tasks = new LinkedHashMap<>();
    }

    public DNode(String nodeId) {
        this();

    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Date getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Date heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Map<String,List<String>> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String,List<String>> tasks) {
        this.tasks = tasks;
    }

    @Override
    public <T> void merge(T data) {

    }
}
