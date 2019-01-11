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

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.command.TaskAssignedCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskPositionQueryCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskPositionUploadCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskRegisterCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskStopCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskStoppedByErrorCommand;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskPosition;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskRegister;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskStop;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskStoppedByError;
import cn.vbill.middleware.porter.common.cluster.data.DTaskLock;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.standalone.StandaloneListener;
import cn.vbill.middleware.porter.common.exception.TaskLockException;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.core.NodeContext;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 任务信息监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 */
public class StandaloneClusterTaskListener extends StandaloneListener implements
        TaskRegister, TaskStop, TaskStoppedByError, TaskPosition {
    private static final String ZK_PATH = BASE_CATALOG + "/task";


    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {

    }

    @Override
    public ClusterListenerFilter filter() {
        return new ClusterListenerFilter() {
            @Override
            public boolean onFilter(ClusterEvent event) {
                return false;
            }
        };
    }


    @Override
    public void upload(TaskPositionUploadCommand command) throws Exception {
        //自旋获得ZK链接
        client.clientSpinning();
        //如果仍不能获得数据库连接
        if (!client.alive()) {
            throw new TaskStopTriggerException("节点集群客户端链接失效");
        }
        String position = listenPath() + "/" + command.getTaskId() + "/position/" + command.getSwimlaneId();
        client.setData(position, command.getPosition(), -1);
    }

    @Override
    public void query(TaskPositionQueryCommand command) {
        String positionPath = listenPath() + "/" + command.getTaskId() + "/position/" + command.getSwimlaneId();
        Pair<String, Boolean> positionPair = client.getData(positionPath);
        String position = null != positionPair && !StringUtils.isBlank(positionPair.getLeft())
                ? positionPair.getLeft() : StringUtils.EMPTY;
        if (null != command.getCallback()) {
            command.getCallback().callback(position);
        }
    }

    @Override
    public void taskRegister(TaskRegisterCommand task) throws Exception {
        String taskPath = listenPath() + "/" + task.getTaskId();
        String assignPath = taskPath + "/lock";
        String errorPath = taskPath + "/error";
        String position = taskPath + "/position";
        client.createDir(taskPath);
        client.createDir(assignPath);
        client.createDir(errorPath);
        client.createDir(position);

        //任务分配
        String topicPath = assignPath + "/" + task.getSwimlaneId();
        if (!client.exists(topicPath, false)) {
            try {
                client.create(topicPath, false, new DTaskLock(task.getTaskId(), NodeContext.INSTANCE.getNodeId(),
                        task.getSwimlaneId()).toString());
                ClusterProviderProxy.INSTANCE.broadcast(new TaskAssignedCommand(task.getTaskId(), task.getSwimlaneId()));
                client.delete(errorPath + "/" + task.getSwimlaneId());
            } catch (Throwable e) {
                if (taskAssignCheck(topicPath)) {
                    taskRegister(task);
                } else {
                    LOGGER.error("任务{}已分配", topicPath);
                    throw new TaskLockException(topicPath + ",锁定资源失败。");
                }
            }
        } else {
            if (taskAssignCheck(topicPath)) {
                taskRegister(task);
            } else {
                LOGGER.error("任务{}已分配", topicPath);
                throw new TaskLockException(topicPath + ",锁定资源失败。");
            }
        }
    }

    @Override
    public void stopTask(TaskStopCommand command) throws Exception {
        String node = listenPath() + "/" + command.getTaskId() + "/lock/" + command.getSwimlaneId();
        if (client.exists(node, false)) {
            Pair<String, Boolean> remoteData = client.getData(node);
            DTaskLock taskLock = DTaskLock.fromString(remoteData.getLeft(), DTaskLock.class);
            if (taskLock.getNodeId().equals(NodeContext.INSTANCE.getNodeId()) && taskLock.getAddress().equals(MachineUtils.IP_ADDRESS)
                    && taskLock.getProcessId().equals(MachineUtils.CURRENT_JVM_PID + "")) {
                client.delete(node);
            }
        }
    }

    @Override
    public void tagError(TaskStoppedByErrorCommand command) throws Exception {
        String errorPath = listenPath() + "/" + command.getTaskId() + "/error/" + command.getSwimlaneId();
        client.createWhenNotExists(errorPath, false, false, command.getMsg());
    }

    private boolean taskAssignCheck(String path) {
        try {
            if (!NodeContext.INSTANCE.forceAssign()) return false;
            Pair<String, Boolean> lockPair = client.getData(path);
            if (null != lockPair && StringUtils.isNotBlank(lockPair.getLeft())) {
                DTaskLock lockInfo = JSONObject.parseObject(lockPair.getLeft(), DTaskLock.class);
                if (lockInfo.getNodeId().equals(NodeContext.INSTANCE.getNodeId()) //节点Id相符
                        && lockInfo.getAddress().equals(NodeContext.INSTANCE.getAddress())) { //IP地址相符
                    client.delete(path);
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.warn("尝试删除任务占用");
        }
        return false;
    }
}