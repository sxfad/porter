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
import cn.vbill.middleware.porter.common.cluster.data.DTaskLock;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.event.command.TaskStopCommand;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import lombok.SneakyThrows;

import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @version: V1.0
 * @review: zkevin/2019年03月14日 15:47
 */
public class TaskListenerStopTaskEventExecutor extends ClusterListenerEventExecutor {

    public TaskListenerStopTaskEventExecutor(Class bindClass, String treeNodePath, String nodeId) {
        super(bindClass, ClusterListenerEventType.TaskStop);
        bind(getBlock(treeNodePath, nodeId));
    }

    @SneakyThrows
    private BiConsumer<ClusterCommand, ClusterClient> getBlock(String treeNodePath, String nodeId) {
        return (clusterCommand, client) -> {
            TaskStopCommand command = (TaskStopCommand) clusterCommand;
            String node = treeNodePath + "/" + command.getTaskId() + "/lock/" + command.getSwimlaneId();
            if (client.isExists(node, true)) {
                ClusterClient.TreeNode remoteData = client.getData(node);
                DTaskLock taskLock = DTaskLock.fromString(remoteData.getData(), DTaskLock.class);
                if (taskLock.getNodeId().equals(nodeId) && taskLock.getAddress().equals(MachineUtils.IP_ADDRESS)
                        && taskLock.getProcessId().equals(MachineUtils.CURRENT_JVM_PID + "")) {
                    client.delete(node);
                }
            }
        };
    }
}
