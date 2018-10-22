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

package cn.vbill.middleware.porter.cluster.zookeeper;

import cn.vbill.middleware.porter.common.client.impl.ZookeeperClient;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.command.*;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.*;
import cn.vbill.middleware.porter.common.cluster.data.DObject;
import cn.vbill.middleware.porter.common.cluster.data.DTaskLock;
import cn.vbill.middleware.porter.common.cluster.data.DTaskStat;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.broadcast.ZKTaskPush;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import cn.vbill.middleware.porter.common.exception.TaskLockException;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.task.TaskEventListener;
import cn.vbill.middleware.porter.common.task.TaskEventProvider;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.core.NodeContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 任务信息监听,一期配置文件配置
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterTaskListener extends ZookeeperClusterListener implements TaskEventProvider,
        TaskRegister, TaskStatUpload, TaskStop, TaskStatQuery, TaskStoppedByError, TaskPosition, ZKTaskPush {
    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private static final Pattern TASK_DIST_PATTERN = Pattern.compile(ZK_PATH + "/.*/dist/.*");
    private static final Pattern TASK_UNLOCKED_PATTERN = Pattern.compile(ZK_PATH + "/.*/lock/.*");
    private final List<TaskEventListener> taskListener;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKClusterTaskListener.class);
    private static final String LOCK_PATH = "/lock/";

    public ZKClusterTaskListener() {
        this.taskListener = new ArrayList<>();
    }

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;

        //获取任务信息
        //注册
        LOGGER.debug("{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        //任务分配
        Matcher taskMatcher = TASK_DIST_PATTERN.matcher(zkEvent.getPath());
        if (taskMatcher.matches()) {
            TaskConfig config = JSONObject.parseObject(zkEvent.getData(), TaskConfig.class);
            if (zkEvent.isOnline() || zkEvent.isDataChanged()) { //任务创建 、任务修改
                NodeContext.INSTANCE.removeTaskError(config.getTaskId());
                triggerTaskEvent(config);
            }
        }

        //任务释放
        if (TASK_UNLOCKED_PATTERN.matcher(zkEvent.getPath()).matches() && event.isOffline()
                && NodeContext.INSTANCE.getNodeStatus().isWorking()) {
            String stoppedErrorPath = zkEvent.getPath().replace(LOCK_PATH, "/error/");
            //如果不是因为错误停止任务
            if (!client.isExists(stoppedErrorPath, false)) {
                Pair<String, Stat> taskConfig = client.getData(zkEvent.getPath().replace(LOCK_PATH, "/dist/"));
                //获取任务配置信息
                if (null != taskConfig && !StringUtils.isBlank(taskConfig.getLeft())) {
                    TaskConfig config = JSONObject.parseObject(taskConfig.getLeft(), TaskConfig.class);
                    NodeContext.INSTANCE.removeTaskError(config.getTaskId());
                    triggerTaskEvent(config);
                }
            }
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ZookeeperClusterListenerFilter() {

            @Override
            protected String getPath() {
                return listenPath();
            }

            @Override
            protected boolean doFilter(ZookeeperClusterEvent event) {
                return true;
            }
        };
    }

    @Override
    public void addTaskEventListener(TaskEventListener listener) {
        taskListener.add(listener);
    }

    @Override
    public void removeTaskEventListener(TaskEventListener listener) {
        taskListener.remove(listener);
    }

    /**
     * 触发TaskEvent
     *
     * @date 2018/8/8 下午4:23
     * @param: [event]
     * @return: void
     */
    private void triggerTaskEvent(TaskConfig event) {
        taskListener.forEach(l -> l.onEvent(event));
    }

    @Override
    public void taskRegister(TaskRegisterCommand task) throws Exception {
        String taskPath = listenPath() + "/" + task.getTaskId();
        String assignPath = taskPath + "/lock";
        String statPath = taskPath + "/stat";
        String errorPath = taskPath + "/error";
        String position = taskPath + "/position";
        client.createWhenNotExists(taskPath, false, true, null);
        client.createWhenNotExists(assignPath, false, true, null);
        client.createWhenNotExists(statPath, false, true, null);
        client.createWhenNotExists(errorPath, false, true, null);
        client.createWhenNotExists(position, false, false, StringUtils.EMPTY);


        //创建任务统计节点
        try {
            String alertNode = statPath + "/" + task.getSwimlaneId();
            if (!client.isExists(alertNode, true)) {
                client.create(alertNode, false, "{}");
            }
        } catch (Exception e) {
            LOGGER.error("创建任务统计节点失败->task:{},node:{},consume-resource:{},", task.getTaskId(),
                    NodeContext.INSTANCE.getNodeId(), task.getSwimlaneId(), e);
        }

        //任务分配
        String topicPath = assignPath + "/" + task.getSwimlaneId();
        if (!client.isExists(topicPath, true)) {
            try {
                //为当前工作节点分配任务topic
                client.create(topicPath, false, new DTaskLock(task.getTaskId(), NodeContext.INSTANCE.getNodeId(),
                        task.getSwimlaneId()).toString());
                //通知对此感兴趣的Listener
                ClusterProviderProxy.INSTANCE.broadcast(new TaskAssignedCommand(task.getTaskId(), task.getSwimlaneId()));
                client.delete(errorPath + "/" + task.getSwimlaneId());
            } catch (KeeperException.NodeExistsException e) {
                throw new TaskLockException(topicPath + ",锁定资源失败。");
            }
        } else {
            throw new TaskLockException(topicPath + ",锁定资源失败。");
        }
    }

    @Override
    public void uploadStat(TaskStatCommand command) throws Exception {
        DTaskStat dataStat = command.getStat();
        //.intern()保证全局唯一字符串对象
        String node = (listenPath() + "/" + dataStat.getTaskId() + "/stat/" + dataStat.getSwimlaneId()
                + "/" + dataStat.getSchema() + "." + dataStat.getTable()).intern();
        //控制锁的粒度到每个consume-resource节点，
        synchronized (node) {
            //1.创建默认节点
            DTaskStat thisStat = new DTaskStat(dataStat.getTaskId(), NodeContext.INSTANCE.getNodeId(),
                    dataStat.getSwimlaneId(), dataStat.getSchema(), dataStat.getTable());
            client.createWhenNotExists(node, false, true, thisStat.toString());

            //2.节点合并赋值并上传
            Pair<String, Stat> nodePair = client.getData(node);
            LOGGER.debug("stat checkout from zookeeper:{}", nodePair.getLeft());
            //remoteStat
            DTaskStat taskStat = DTaskStat.fromString(nodePair.getLeft(), DTaskStat.class);
            //run callback before merge data
            if (null != command.getCallback()) {
                command.getCallback().callback(taskStat);
            }
            //merge from localStat
            taskStat.merge(dataStat);
            try {
                //upload stat
                client.setData(node, taskStat.toString(), nodePair.getRight().getVersion());
                LOGGER.debug("stat store in zookeeper:{}", JSON.toJSONString(taskStat));
            } catch (Throwable e) {
                logger.warn("任务进度状态上传失败", e);
            }
        }
    }

    @Override
    public void stopTask(TaskStopCommand command) throws Exception {
        String node = listenPath() + "/" + command.getTaskId() + LOCK_PATH + command.getSwimlaneId();

        if (client.isExists(node, true)) {
            Pair<String, Stat> remoteData = client.getData(node);
            DTaskLock taskLock = DTaskLock.fromString(remoteData.getLeft(), DTaskLock.class);
            if (taskLock.getNodeId().equals(NodeContext.INSTANCE.getNodeId()) && taskLock.getAddress().equals(MachineUtils.IP_ADDRESS)
                    && taskLock.getProcessId().equals(MachineUtils.CURRENT_JVM_PID + "")) {
                client.delete(node);
            }
        }
    }

    @Override
    public void queryTaskStat(TaskStatQueryCommand command) {
        String node = listenPath() + "/" + command.getTaskId() + "/stat/" + command.getSwimlaneId();
        LOGGER.debug("query \"{}\" children node.", node);

        List<String> children = client.getChildren(node);
        List<DObject> stats = new ArrayList<>();
        for (String child : children) {
            String fullChild = node + "/" + child;
            LOGGER.debug("got \"{}\" children node \"{}\".", node, fullChild);
            try {
                Pair<String, Stat> nodePair = client.getData(fullChild);
                if (null != nodePair && !StringUtils.isBlank(nodePair.getLeft())) {
                    LOGGER.debug("got \"{}\" children node \"{}\" value {}.", node, fullChild, nodePair.getLeft());
                    DTaskStat taskStat = DTaskStat.fromString(nodePair.getLeft(), DTaskStat.class);
                    stats.add(taskStat);
                }
            } catch (Exception e) {
                logger.warn("查询任务进度状态失败", e);
            }
        }
        if (null != command.getCallback()) {
            command.getCallback().callback(stats);
        }
    }

    @Override
    public void tagError(TaskStoppedByErrorCommand command) {
        String errorPath = listenPath() + "/" + command.getTaskId() + "/error/" + command.getSwimlaneId();
        client.createWhenNotExists(errorPath, false, false, command.getMsg());
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
        client.changeData(position, false, false, command.getPosition());
    }

    @Override
    public void query(TaskPositionQueryCommand command) throws Exception {
        String positionPath = listenPath() + "/" + command.getTaskId() + "/position/" + command.getSwimlaneId();
        Pair<String, Stat> positionPair = client.getData(positionPath);
        String position = null != positionPair && !StringUtils.isBlank(positionPair.getLeft())
                ? positionPair.getLeft() : StringUtils.EMPTY;
        if (null != command.getCallback()) {
            command.getCallback().callback(position);
        }
    }

    @Override
    public void push(TaskPushCommand command) throws Exception {
        push(command, false, true);
    }

    @Override
    public ZookeeperClient getZKClient() {
        return client;
    }

    @Override
    public String zkTaskPath() {
        return listenPath();
    }
}