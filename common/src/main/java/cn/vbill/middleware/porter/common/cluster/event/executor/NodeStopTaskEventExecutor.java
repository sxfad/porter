/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.cluster.event.executor;


import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.event.command.TaskStopCommand;
import lombok.SneakyThrows;

import java.util.TreeSet;
import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @version: V1.0
 * @review: zkevin/2019年03月14日 15:47
 */
public class NodeStopTaskEventExecutor extends ClusterListenerEventExecutor {

    public NodeStopTaskEventExecutor(Class bindClass, String nodeId, String treeNodePath) {
        super(bindClass, ClusterListenerEventType.TaskStop);
        bind(getBlock(treeNodePath, nodeId));
    }

    @SneakyThrows
    private BiConsumer<ClusterCommand, ClusterClient> getBlock(String treeNodePath, String nodeId) {
        return new BiConsumer<ClusterCommand, ClusterClient>() {
            @SneakyThrows
            public void accept(ClusterCommand clusterCommand, ClusterClient client) {
                TaskStopCommand command = (TaskStopCommand) clusterCommand;
                String path = treeNodePath + "/" + nodeId + "/stat";
                synchronized (path.intern()) {
                    DNode nodeData = DNode.fromString(client.getData(path).getData(), DNode.class);
                    if (null != nodeData.getTasks() && !nodeData.getTasks().isEmpty()) {
                        TreeSet<String> swimlaneIdList = nodeData.getTasks().getOrDefault(command.getTaskId(), new TreeSet<>());
                        if (swimlaneIdList.contains(command.getSwimlaneId())) {
                            swimlaneIdList.remove(command.getSwimlaneId());
                        }
                        if (swimlaneIdList.isEmpty()) {
                            nodeData.getTasks().remove(command.getTaskId());
                        }
                    }
                    client.setData(path, nodeData.toString(), client.exists(path, true));
                }
            }
        };
    }
}
