/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 17:46
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;

import com.suixingpay.datas.common.node.NodeCommandType;
import com.suixingpay.datas.common.dic.NodeStatusType;
import lombok.*;

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
