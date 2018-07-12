/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 17:29
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.node;

import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.dic.NodeHealthLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 17:29
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 17:29
 */
public class Node {
    //节点ID
    @Getter @Setter private String nodeId;
    //节点状态
    @Getter @Setter private NodeStatusType status = NodeStatusType.SUSPEND;
    //是否上传统计信息
    @Getter @Setter private boolean uploadStatistic = true;
    //节点健康级别
    @Getter @Setter private NodeHealthLevel healthLevel = NodeHealthLevel.GREEN;
    //节点健康级别描述
    @Getter @Setter private String healthLevelDesc;
    //工作总阀值
    @Getter @Setter private int workLimit = 5;
    //工作指标用量
    @Getter @Setter private AtomicInteger workUsed = new AtomicInteger(0);
    @Getter @Setter private volatile DNode dnodeSnapshot = new DNode();
}
