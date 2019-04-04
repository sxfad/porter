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
package cn.vbill.middleware.porter.common.cluster.event.executor;

import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.task.statistics.DTaskLock;
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
