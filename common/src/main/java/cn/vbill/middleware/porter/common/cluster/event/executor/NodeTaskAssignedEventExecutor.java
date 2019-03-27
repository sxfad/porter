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

import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.event.command.TaskAssignedCommand;
import lombok.SneakyThrows;

import java.util.TreeSet;
import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @version: V1.0
 * @review: zkevin/2019年03月14日 15:47
 */
public class NodeTaskAssignedEventExecutor extends ClusterListenerEventExecutor {

    public NodeTaskAssignedEventExecutor(Class bindClass, String nodeId, String treeNodePath) {
        super(bindClass, ClusterListenerEventType.TaskAssigned);
        bind(getBlock(treeNodePath, nodeId));
    }

    @SneakyThrows
    private BiConsumer<ClusterCommand, ClusterClient> getBlock(String treeNodePath, String nodeId) {
        return new BiConsumer<ClusterCommand, ClusterClient>() {
            @SneakyThrows
            public void accept(ClusterCommand clusterCommand, ClusterClient client) {
                TaskAssignedCommand command = (TaskAssignedCommand) clusterCommand;
                String path = treeNodePath + "/" + nodeId + "/stat";
                synchronized (path.intern()) {
                    DNode nodeData = client.isExists(path, false)
                            ? DNode.fromString(client.getData(path).getData(), DNode.class)
                            : new DNode(nodeId);
                    TreeSet<String> resources = null != nodeData
                            ? nodeData.getTasks().getOrDefault(command.getTaskId(), new TreeSet<>())
                            : new TreeSet<>();
                    resources.add(command.getSwimlaneId());
                    nodeData.getTasks().put(command.getTaskId(), resources);
                    client.changeData(path, false, false, nodeData.toString());
                }
            }
        };
    }
}
