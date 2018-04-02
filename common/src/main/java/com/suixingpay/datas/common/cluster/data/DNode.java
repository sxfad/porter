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
import com.suixingpay.datas.common.dic.NodeHealthLevel;
import com.suixingpay.datas.common.dic.NodeStatusType;
import com.suixingpay.datas.common.util.MachineUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 集群节点
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 13:45
 */
public class DNode extends DObject {
    //节点ID
    @Getter @Setter private String nodeId;
    //心跳时间
    @JSONField(format = DEFAULT_DATE_FORMAT)
    @Getter @Setter private Date heartbeat;
    //ip
    @Getter @Setter private String address = MachineUtils.IP_ADDRESS;
    //主机名
    @Getter @Setter private String hostName = MachineUtils.HOST_NAME;
    //进程ID
    @Getter @Setter private String processId = MachineUtils.CURRENT_JVM_PID + "";
    //节点工作状态
    @Getter @Setter private NodeStatusType status;
    //节点当天任务
    @Getter @Setter private Map<String, List<String>> tasks;
    //节点健康级别
    @Getter @Setter private NodeHealthLevel healthLevel = NodeHealthLevel.GREEN;
    //节点健康级别描述
    @Getter @Setter private String healthLevelDesc;

    public DNode() {
        heartbeat = new Date();
        tasks = new LinkedHashMap<>();
    }

    public DNode(String nodeId) {
        this();
        this.nodeId = nodeId;
    }

    @Override
    public <T> void merge(T data) {

    }
}
