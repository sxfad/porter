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

package cn.vbill.middleware.porter.common.node;

import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.dic.ClusterPlugin;
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

    //工作模式
    @Getter @Setter private ClusterPlugin workMode = ClusterPlugin.STANDALONE;


    //强制任务分配
    public static String FORCE_ASSIGN_SIGN = "--force";
    @Getter @Setter private boolean forceAssign = false;
}
