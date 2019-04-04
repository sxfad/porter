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

package cn.vbill.middleware.porter.cluster.standalone;

import cn.vbill.middleware.porter.cluster.CommonCodeBlock;
import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.*;
import cn.vbill.middleware.porter.common.task.statistics.DTaskLock;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.event.executor.TaskListenerStopTaskEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.executor.TaskPositionQueryEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.executor.TaskPositionUploadEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.executor.TaskStopByErrorEventExecutor;
import cn.vbill.middleware.porter.common.cluster.impl.standalone.StandaloneListener;
import cn.vbill.middleware.porter.common.task.exception.TaskLockException;
import cn.vbill.middleware.porter.core.NodeContext;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 任务信息监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 */
public class StandaloneClusterTaskListener extends StandaloneListener {
    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private CommonCodeBlock blockProxy;
    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent event) {

    }

    @Override
    public void setClient(ClusterClient client) {
        super.setClient(client);
        blockProxy = new CommonCodeBlock(client);
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ClusterListenerFilter() {
            @Override
            public boolean doFilter(ClusterTreeNodeEvent event) {
                return false;
            }

            @Override
            public String getPath() {
                return listenPath();
            }
        };
    }





    @Override
    public List<ClusterListenerEventExecutor> watchedEvents() {
        List<ClusterListenerEventExecutor> executors = new ArrayList<>();

        //任务因错误停止
        executors.add(new TaskStopByErrorEventExecutor(this.getClass(), listenPath()));
        //任务进度查询
        executors.add(new TaskPositionQueryEventExecutor(this.getClass(), listenPath()));
        //任务进度上传
        executors.add(new TaskPositionUploadEventExecutor(this.getClass(), listenPath()));
        //任务停止
        executors.add(new TaskListenerStopTaskEventExecutor(this.getClass(), listenPath(), NodeContext.INSTANCE.getNodeId()));

        //任务注册
        executors.add(new ClusterListenerEventExecutor(this.getClass(), ClusterListenerEventType.TaskRegister).bind(new BiConsumer<ClusterCommand, ClusterClient>() {
            @SneakyThrows
            public void accept(ClusterCommand clusterCommand, ClusterClient client) {
                TaskRegisterCommand task = (TaskRegisterCommand) clusterCommand;
                String taskPath = listenPath() + "/" + task.getTaskId();
                String assignPath = taskPath + "/lock";
                String errorPath = taskPath + "/error";
                String position = taskPath + "/position";
                client.createRoot(taskPath, false);
                client.createRoot(assignPath, false);
                client.createRoot(errorPath, false);
                client.createRoot(position, false);

                //任务分配
                String topicPath = assignPath + "/" + task.getSwimlaneId();
                if (!client.isExists(topicPath, false)) {
                    try {
                        client.create(topicPath, false, new DTaskLock(task.getTaskId(), NodeContext.INSTANCE.getNodeId(),
                                task.getSwimlaneId()).toString());
                        ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskAssignedCommand(task.getTaskId(), task.getSwimlaneId()));
                        client.delete(errorPath + "/" + task.getSwimlaneId());
                    } catch (Throwable e) {
                        if (blockProxy.taskAssignCheck(topicPath)) {
                            this.accept(task, client);
                        } else {
                            logger.error("任务{}已分配", topicPath);
                            throw new TaskLockException(topicPath + ",锁定资源失败。");
                        }
                    }
                } else {
                    if (blockProxy.taskAssignCheck(topicPath)) {
                        this.accept(task, client);
                    } else {
                        logger.error("任务{}已分配", topicPath);
                        throw new TaskLockException(topicPath + ",锁定资源失败。");
                    }
                }
            }
        }, client));
        return executors;
    }
}