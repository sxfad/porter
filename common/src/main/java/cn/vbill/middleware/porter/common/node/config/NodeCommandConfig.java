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

package cn.vbill.middleware.porter.common.node.config;

import cn.vbill.middleware.porter.common.node.dic.NodeCommandType;
import cn.vbill.middleware.porter.common.node.dic.NodeStatusType;

/**
 * 1、节点任务状态推送（运行中 or 暂停 ）WORKING("WORKING", "工作中"
 * 2、节点停止任务推送 SUSPEND("SUSPEND", "已暂停")
 * @author: zhangkewei[zhang_kw@suixingpay.com] 
 * @date: 2018年02月24日 17:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 17:46
 */

public class NodeCommandConfig {
    private String nodeId;
    private NodeStatusType status = NodeStatusType.SUSPEND;
    private Integer workLimit = -1;
    private NodeCommandType command;

    public NodeCommandConfig(String nodeId, NodeStatusType status, Integer workLimit, NodeCommandType command) {
        this.nodeId = nodeId;
        this.status = status;
        this.workLimit = workLimit;
        this.command = command;
    }

    public NodeCommandConfig(String nodeId, NodeStatusType status, NodeCommandType command) {
        this.nodeId = nodeId;
        this.status = status;
        this.command = command;
    }

    public NodeCommandConfig() {
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public NodeStatusType getStatus() {
        return status;
    }

    public void setStatus(NodeStatusType status) {
        this.status = status;
    }

    public Integer getWorkLimit() {
        return workLimit;
    }

    public void setWorkLimit(Integer workLimit) {
        this.workLimit = workLimit;
    }

    public NodeCommandType getCommand() {
        return command;
    }

    public void setCommand(NodeCommandType command) {
        this.command = command;
    }
}
