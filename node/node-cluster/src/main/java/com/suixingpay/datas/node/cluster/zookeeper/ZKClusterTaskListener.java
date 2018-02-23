/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.cluster.zookeeper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.*;
import com.suixingpay.datas.common.cluster.command.broadcast.*;
import com.suixingpay.datas.common.cluster.data.DObject;
import com.suixingpay.datas.common.cluster.data.DTaskLock;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.config.TaskConfig;
import com.suixingpay.datas.common.exception.TaskLockException;
import com.suixingpay.datas.common.task.TaskEvent;
import com.suixingpay.datas.common.task.TaskEventListener;
import com.suixingpay.datas.common.task.TaskEventProvider;
import com.suixingpay.datas.common.task.TaskEventType;
import com.suixingpay.datas.common.util.MachineUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 任务信息监听,一期配置文件配置
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterTaskListener extends ZookeeperClusterListener implements TaskEventProvider, NodeRegister,
        TaskRegister, TaskStatUpload, TaskStop, TaskStatQuery {
    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private static final Pattern TASK_PUSH_PATTERN = Pattern.compile(ZK_PATH + "/[0-9]*/dist/.*");
    private final List<TaskEventListener> TASK_LISTENER;
    private String nodeId;

    public ZKClusterTaskListener() {
        this.TASK_LISTENER = new ArrayList<>();
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
        Matcher taskMatcher = TASK_PUSH_PATTERN.matcher(zkEvent.getPath());
        if (taskMatcher.matches()) {
            TaskEventType type = null;
            TaskConfig config = JSONObject.parseObject(zkEvent.getData(), TaskConfig.class);
            if (zkEvent.isOnline()) { //任务创建
                type = TaskEventType.CREATE;
            }

            if (zkEvent.isOffline()) {
                /**
                 * 任务停止,如果同一个任务配置多个消费资源的话（如果是kafka，指的是多个topic），
                 * 会重复停止同一个任务多次,相关逻辑需要实现幂等。
                 */
                type = TaskEventType.DELETE;
            }

            TaskEvent taskEvent = new TaskEvent(config, type);

            if (null != type) triggerTaskEvent(taskEvent);
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ZookeeperClusterListenerFilter(){

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
        TASK_LISTENER.add(listener);
    }

    @Override
    public void removeTaskEventListener(TaskEventListener listener) {
        TASK_LISTENER.remove(listener);
    }

    private void triggerTaskEvent(TaskEvent event) {
        TASK_LISTENER.forEach(l -> l.onEvent(event));
    }


    @Override
    public void nodeRegister(NodeRegisterCommand command) throws Exception {
        this.nodeId = command.getId();
    }

    @Override
    public void taskRegister(TaskRegisterCommand task) throws Exception {
        String taskPath = listenPath() + "/" + task.getTaskId();
        String assignPath = taskPath + "/lock";
        String statPath = taskPath + "/stat";
        //任务分配、工作节点注册目录创建
        Stat stat = client.exists(listenPath() + "/" + task.getTaskId(), true);
        if (null == stat) {
            client.create(taskPath,false,"{}");
            client.create(assignPath,false,"{}");
            client.create(statPath,false,"{}");
        }

        //创建任务统计节点
        try {
            String alertNode = statPath + "/" + task.getSwimlaneId();
            if (null == client.exists(alertNode, true)) {
                client.create(alertNode,false ,"{}");
            }
        } catch (Exception e) {
            LOGGER.error("创建任务统计节点失败->task:{},node:{},consume-resource:{},", task.getTaskId(), nodeId, task.getSwimlaneId(), e);
        }

        //任务分配
        String topicPath = assignPath + "/" + task.getSwimlaneId();
        Stat ifAssignTopic = client.exists(topicPath, true);
        if (null == ifAssignTopic) {
            //为当前工作节点分配任务topic
            client.create(topicPath,false, new DTaskLock(task.getTaskId(), nodeId, task.getSwimlaneId()).toString());
            //通知对此感兴趣的Listener
            ClusterProviderProxy.INSTANCE.broadcast(new TaskAssignedCommand(task.getTaskId(),task.getSwimlaneId()));
        } else {
            throw  new TaskLockException(topicPath+",锁定资源失败。");
        }
    }

    @Override
    public void uploadStat(TaskStatCommand command) throws Exception {
        DTaskStat dataStat = command.getStat();
        //.intern()保证全局唯一字符串对象
        String node = (listenPath() + "/" +dataStat.getTaskId() + "/stat/" + dataStat.getSwimlaneId()
                + "/" + dataStat.getSchema()+ "." + dataStat.getTable()).intern();
        //控制锁的粒度到每个consume-resource节点，
        synchronized (node) {
            Stat stat = client.exists(node, true);
            //节点不存在，创建新节点
            if (null == stat) {
                DTaskStat thisStat = new DTaskStat(dataStat.getTaskId(), nodeId, dataStat.getSwimlaneId(), dataStat.getSchema(), dataStat.getTable());
                client.create(node,false ,thisStat.toString());
                stat = client.exists(node, true);
            }

            //节点合并赋值并上传
            if (null != stat) {
                Pair<String, Stat> nodePair = client.getData(node);
                LOGGER.debug("stat checkout from zookeeper:{}", nodePair.getLeft());
                //remoteStat
                DTaskStat taskStat = DTaskStat.fromString(nodePair.getLeft(), DTaskStat.class);
                //run callback before merge data
                if (null != command.getCallback()) command.getCallback().callback(taskStat);
                //merge from localStat
                taskStat.merge(dataStat);
                //upload stat
                client.setData(node, taskStat.toString(), nodePair.getRight().getVersion());

                LOGGER.debug("stat store in zookeeper:{}", JSON.toJSONString(taskStat));
            }
        }
    }

    @Override
    public void stopTask(TaskStopCommand command) throws Exception {
        String node = listenPath() + "/" + command.getTaskId() + "/lock/" + command.getSwimlaneId();
        Stat stat = client.exists(node, false);
        if (null != stat) {
            Pair<String, Stat> remoteData = client.getData(node);
            DTaskLock taskLock = DTaskLock.fromString(remoteData.getLeft(), DTaskLock.class);
            if (taskLock.getNodeId().equals(nodeId) && taskLock.getAddress().equals(MachineUtils.IP_ADDRESS)
                    && taskLock.getProcessId().equals(MachineUtils.CURRENT_JVM_PID + "")) {
                client.delete(node);
            }
        }
    }

    @Override
    public void queryTaskStat(TaskStatQueryCommand command) {
        String node = listenPath() + "/" +command.getTaskId() + "/stat/" + command.getSwimlaneId();
        LOGGER.debug("query \"{}\" children node.", node);

        List<String> children = client.getChildren(node);
        List<DObject> stats = new ArrayList<>();
        for (String child : children) {
            String fullChild = node + "/" +child;
            LOGGER.debug("got \"{}\" children node \"{}\".", node, fullChild);
            try {
                Pair<String, Stat> nodePair = client.getData(fullChild);
                if (null != nodePair && !StringUtils.isBlank(nodePair.getLeft())) {
                    LOGGER.debug("got \"{}\" children node \"{}\" value {}.", node, fullChild, nodePair.getLeft());
                    DTaskStat taskStat = DTaskStat.fromString(nodePair.getLeft(), DTaskStat.class);
                    stats.add(taskStat);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(null != command.getCallback()) command.getCallback().callback(stats);
    }
}