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

import cn.vbill.middleware.porter.cluster.CommonCodeBlock;
import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.executor.*;
import cn.vbill.middleware.porter.common.cluster.event.command.*;
import cn.vbill.middleware.porter.common.statistics.DObject;
import cn.vbill.middleware.porter.common.task.statistics.DTaskLock;
import cn.vbill.middleware.porter.common.task.statistics.DTaskStat;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import cn.vbill.middleware.porter.common.task.exception.TaskLockException;
import cn.vbill.middleware.porter.common.task.event.TaskEventListener;
import cn.vbill.middleware.porter.common.task.event.TaskEventProvider;
import cn.vbill.middleware.porter.core.NodeContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 任务信息监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterTaskListener extends ZookeeperClusterListener implements TaskEventProvider {
    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private static final Pattern TASK_DIST_PATTERN = Pattern.compile(ZK_PATH + "/.*/dist/.*");
    private static final Pattern TASK_UNLOCKED_PATTERN = Pattern.compile(ZK_PATH + "/.*/lock/.*");
    private final List<TaskEventListener> taskListener;

    private static final String LOCK_PATH = "/lock/";

    private CommonCodeBlock blockProxy;

    public ZKClusterTaskListener() {
        this.taskListener = new ArrayList<>();
    }

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void setClient(ClusterClient client) {
        super.setClient(client);
        blockProxy = new CommonCodeBlock(client);
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent event) {
        //获取任务信息
        //注册
        logger.debug("{},{},{}", event.getId(), event.getData(), event.getEventType());
        //任务分配
        Matcher taskMatcher = TASK_DIST_PATTERN.matcher(event.getId());
        if (taskMatcher.matches()) {
            TaskConfig config = JSONObject.parseObject(event.getData(), TaskConfig.class);
            NodeContext.INSTANCE.removeTaskError(config.getTaskId());
            if (event.isOnline() || event.isDataChanged()) { //任务创建 、任务修改
                triggerTaskEvent(config);
            }

        }

        //任务释放
        if (TASK_UNLOCKED_PATTERN.matcher(event.getId()).matches() && event.isOffline()
                && NodeContext.INSTANCE.getNodeStatus().isWorking()) {
            String stoppedErrorPath = event.getId().replace(LOCK_PATH, "/error/");
            //如果不是因为错误停止任务
            if (!client.isExists(stoppedErrorPath, false)) {
                ClusterClient.TreeNode taskConfig = client.getData(event.getId().replace(LOCK_PATH, "/dist/"));
                //获取任务配置信息
                if (null != taskConfig && !StringUtils.isBlank(taskConfig.getData())) {
                    TaskConfig config = JSONObject.parseObject(taskConfig.getData(), TaskConfig.class);
                    NodeContext.INSTANCE.removeTaskError(config.getTaskId());
                    triggerTaskEvent(config);
                }
            }
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ClusterListenerFilter() {
            @Override
            public String getPath() {
                return listenPath();
            }

            @Override
            public boolean doFilter(ClusterTreeNodeEvent event) {
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

        //任务上传事件
        executors.add(new TaskPushEventExecutor(this.getClass(), false, true, listenPath()));

        //任务注册
        executors.add(new ClusterListenerEventExecutor(this.getClass(), ClusterListenerEventType.TaskRegister).bind(new BiConsumer<ClusterCommand, ClusterClient>() {
            @SneakyThrows
            public void accept(ClusterCommand clusterCommand, ClusterClient client) {
                TaskRegisterCommand task = (TaskRegisterCommand) clusterCommand;
                String taskPath = listenPath() + "/" + task.getTaskId();
                String assignPath = taskPath + "/lock";
                String statPath = taskPath + "/stat";
                String errorPath = taskPath + "/error";
                String position = taskPath + "/position";
                client.create(taskPath, StringUtils.EMPTY, false, true);
                client.create(assignPath, StringUtils.EMPTY, false, true);
                client.create(statPath, StringUtils.EMPTY, false, true);
                client.create(errorPath, StringUtils.EMPTY, false, true);
                client.create(position, StringUtils.EMPTY, false, false);


                //创建任务统计节点
                try {
                    String alertNode = statPath + "/" + task.getSwimlaneId();
                    if (!client.isExists(alertNode, true)) {
                        client.create(alertNode, false, "{}");
                    }
                } catch (Exception e) {
                    logger.error("创建任务统计节点失败->task:{},node:{},consume-resource:{},", task.getTaskId(),
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
                        ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskAssignedCommand(task.getTaskId(), task.getSwimlaneId()));
                        client.delete(errorPath + "/" + task.getSwimlaneId());
                    } catch (KeeperException.NodeExistsException e) {
                        blockProxy.taskAssignCheck(topicPath);
                        logger.error("任务{}已分配", topicPath);
                        throw new TaskLockException(topicPath + ",锁定资源失败。");
                    }
                } else {
                    blockProxy.taskAssignCheck(topicPath);
                    logger.error("任务{}已分配", topicPath);
                    throw new TaskLockException(topicPath + ",锁定资源失败。");
                }
            }
        }, client));

        //任务状态查询
        executors.add(new ClusterListenerEventExecutor(this.getClass(), ClusterListenerEventType.TaskStatQuery).bind((clusterCommand, client) -> {
            TaskStatQueryCommand command = (TaskStatQueryCommand) clusterCommand;
            String node = listenPath() + "/" + command.getTaskId() + "/stat/" + command.getSwimlaneId();
            logger.debug("query \"{}\" children node.", node);

            List<String> children = client.getChildren(node);
            List<DObject> stats = new ArrayList<>();
            for (String child : children) {
                String fullChild = node + "/" + child;
                logger.debug("got \"{}\" children node \"{}\".", node, fullChild);
                try {
                    ClusterClient.TreeNode remoteData = client.getData(fullChild);
                    if (null != remoteData && !StringUtils.isBlank(remoteData.getData())) {
                        logger.debug("got \"{}\" children node \"{}\" value {}.", node, fullChild, remoteData.getData());
                        DTaskStat taskStat = DTaskStat.fromString(remoteData.getData(), DTaskStat.class);
                        stats.add(taskStat);
                    }
                } catch (Exception e) {
                    logger.warn("查询任务进度状态失败", e);
                }
            }
            if (null != command.getCallback()) {
                command.getCallback().callback(stats);
            }
        }, client));

        //任务状态上传
        executors.add(new ClusterListenerEventExecutor(this.getClass(), ClusterListenerEventType.TaskStatUpload).bind((clusterCommand, client) -> {
            TaskStatCommand command = (TaskStatCommand) clusterCommand;
            DTaskStat dataStat = command.getStat();
            //.intern()保证全局唯一字符串对象
            String node = (listenPath() + "/" + dataStat.getTaskId() + "/stat/" + dataStat.getSwimlaneId()
                    + "/" + dataStat.getSchema() + "." + dataStat.getTable()).intern();
            //控制锁的粒度到每个consume-resource节点，
            synchronized (node) {
                //1.创建默认节点
                DTaskStat thisStat = new DTaskStat(dataStat.getTaskId(), NodeContext.INSTANCE.getNodeId(),
                        dataStat.getSwimlaneId(), dataStat.getSchema(), dataStat.getTable());
                client.create(node, thisStat.toString(), false, true);

                //2.节点合并赋值并上传
                ClusterClient.TreeNode treeNode = client.getData(node);
                logger.debug("stat checkout from zookeeper:{}", treeNode.getData());
                //remoteStat
                DTaskStat taskStat = DTaskStat.fromString(treeNode.getData(), DTaskStat.class);
                //run callback before merge data
                if (null != command.getCallback()) {
                    command.getCallback().callback(taskStat);
                }
                //merge from localStat
                taskStat.merge(dataStat);
                try {
                    //upload stat
                    client.setData(node, taskStat.toString(), treeNode.getVersion());
                    logger.debug("stat store in zookeeper:{}", JSON.toJSONString(taskStat));
                } catch (Throwable e) {
                    logger.warn("任务进度状态上传失败", e);
                }
            }
        }, client));
        return executors;
    }
}