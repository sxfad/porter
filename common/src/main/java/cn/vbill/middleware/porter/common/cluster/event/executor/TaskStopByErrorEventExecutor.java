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
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.event.command.TaskStoppedByErrorCommand;
import cn.vbill.middleware.porter.common.task.statistics.DTaskError;
import lombok.SneakyThrows;

import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @version: V1.0
 * @review: zkevin/2019年03月14日 15:47
 */
public class TaskStopByErrorEventExecutor extends ClusterListenerEventExecutor {

    public TaskStopByErrorEventExecutor(Class bindClass, String treeNodePath) {
        super(bindClass, ClusterListenerEventType.TaskStoppedByError);
        bind(getBlock(treeNodePath));
    }

    @SneakyThrows
    private BiConsumer<ClusterCommand, ClusterClient> getBlock(String treeNodePath) {
        return (clusterCommand, client) -> {
            TaskStoppedByErrorCommand command = (TaskStoppedByErrorCommand) clusterCommand;
            String errorPath = treeNodePath + "/" + command.getTaskId() + "/error/" + command.getSwimlaneId();
            client.create(errorPath, new DTaskError(command.getTaskId(), command.getSwimlaneId(), command.getMsg()).toString(), false, false);
        };
    }
}
