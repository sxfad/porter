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

package cn.vbill.middleware.porter.common.config;

import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.node.NodeCommandType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 1、节点任务状态推送（运行中 or 暂停 ）WORKING("WORKING", "工作中"
 * 2、节点停止任务推送 SUSPEND("SUSPEND", "已暂停")
 * @author: zhangkewei[zhang_kw@suixingpay.com] 
 * @date: 2018年02月24日 17:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 17:46
 */

//无参构造器
@NoArgsConstructor
//全参构造器
@AllArgsConstructor
public class NodeCommandConfig {
    @Setter @Getter private String nodeId;
    @Setter @Getter private NodeStatusType status = NodeStatusType.SUSPEND;
    @Setter @Getter private Integer workLimit = -1;
    @Setter @Getter private NodeCommandType command;

    public NodeCommandConfig(String nodeId, NodeStatusType status, NodeCommandType command) {
        this.nodeId = nodeId;
        this.status = status;
        this.command = command;
    }
}
