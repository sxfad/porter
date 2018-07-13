/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.common.cluster.data;

import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.common.dic.NodeHealthLevel;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

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
    @Getter @Setter private Map<String, TreeSet<String>> tasks;
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
