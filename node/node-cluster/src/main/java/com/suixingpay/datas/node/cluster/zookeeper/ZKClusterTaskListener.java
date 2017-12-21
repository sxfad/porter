package com.suixingpay.datas.node.cluster.zookeeper;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.command.*;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.zookeeper.ZookeeperClusterEvent;
import com.suixingpay.datas.common.cluster.zookeeper.ZookeeperClusterListener;
import com.suixingpay.datas.common.cluster.zookeeper.ZookeeperClusterListenerFilter;
import com.suixingpay.datas.common.cluster.zookeeper.data.DTaskStat;
import com.suixingpay.datas.common.task.TaskEventListener;
import com.suixingpay.datas.common.task.TaskEventProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务信息监听,一期配置文件配置
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterTaskListener extends ZookeeperClusterListener implements TaskEventProvider{
    private final Map<String, Pair<String,String>> TASK_TOPIC;
    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private final List<TaskEventListener> TASK_LISTENER;
    private String nodeId;

    public ZKClusterTaskListener() {
        this.TASK_LISTENER = new ArrayList<>();
        this.TASK_TOPIC = new ConcurrentHashMap<>();
    }

    @Override
    public String path() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent)event;
        //获取任务信息
        //注册

    }

    @Override
    public ClusterListenerFilter filter() {
        return new ZookeeperClusterListenerFilter(){

            @Override
            protected String getPath() {
                return path();
            }

            @Override
            protected boolean doFilter(ZookeeperClusterEvent event) {
                return true;
            }
        };
    }

    @Override
    public void hobby(ClusterCommand command) throws Exception {
        if (command instanceof NodeRegisterCommand) {
            NodeRegisterCommand nodecmd = (NodeRegisterCommand)command;
            this.nodeId = nodecmd.getId();
        } else if (command instanceof TaskRegisterCommand) {
            TaskRegisterCommand task = (TaskRegisterCommand)command;
            String taskPath = path() + "/" + task.getTaskId();
            String assignPath = taskPath + "/assign";
            //任务分配、工作节点注册目录创建
            Stat stat = client.exists(path() + "/" + task.getTaskId(), true);
            if (null == stat) {
                client.create(taskPath,false,"{}");
                client.create(assignPath,false,"{}");
            }
            //任务分配
            String topicPath = assignPath + "/" + task.getTopic();
            Stat ifAssignTopic = client.exists(topicPath, true);
            if (null == ifAssignTopic) {
                //为当前工作节点分配任务topic
                client.create(topicPath,false,new DTaskStat(task.getTaskId(), nodeId ,task.getTopic()).toString());
                //保存映射关系到本地内存
                TASK_TOPIC.putIfAbsent(task.getTaskId() + "_" + task.getTopic(), new ImmutablePair<>(task.getTaskId(), task.getTopic()));
                //通知对此感兴趣的Listener
                ClusterProvider.sendCommand(new TaskAssignedCommand(task.getTaskId(),task.getTopic()));
            } else {
                throw  new Exception(topicPath+",锁定资源失败。");
            }
        } else if (command instanceof ShutwdownCommand) {
            for (Pair<String,String> kv : TASK_TOPIC.values()) {
                String node = path() + "/" + kv.getLeft() + "/assign/" + kv.getRight();
                Stat stat = client.exists(node, false);
                if (null != stat) {
                    DTaskStat taskStat = DTaskStat.fromString(client.getData(node).getLeft(),DTaskStat.class);
                    if (taskStat.getNodeId().equals(nodeId)) {
                        client.delete(node);
                    }
                }
            }
        } else if (command instanceof TaskStopCommand) {
            TaskStopCommand stopCommand = (TaskStopCommand) command;
            String node = path() + "/" + stopCommand.getTaskId() + "/assign/" + stopCommand.getTopic();
            Stat stat = client.exists(node, false);
            if (null != stat) {
                DTaskStat taskStat = DTaskStat.fromString(client.getData(node).getLeft(),DTaskStat.class);
                if (taskStat.getNodeId().equals(nodeId)) {
                    client.delete(node);
                }
            }
        } else if (command instanceof TaskStatCommand) {
            TaskStatCommand statCommand = (TaskStatCommand) command;
            String node = path() + "/" + statCommand.getTaskId() + "/assign/" + statCommand.getTopic();
            Stat stat = client.exists(node, false);
            if (null != stat) {
                Pair<String, Stat> nodePair = client.getData(node);
                DTaskStat taskStat = DTaskStat.fromString(nodePair.getLeft(), DTaskStat.class);
                if (taskStat.getNodeId().equals(nodeId)) {
                    taskStat.getDeleteRow().addAndGet(statCommand.getDeleteCount());
                    taskStat.getInsertRow().addAndGet(statCommand.getInsertCount());
                    taskStat.getMaylostRow().addAndGet(statCommand.getMayLostCount());
                    taskStat.getUpdateRow().addAndGet(statCommand.getUpdateCount());
                    client.setData(node, taskStat.toString(), nodePair.getRight().getVersion());
                }
            }
        }
    }

    @Override
    public void addTaskEventListener(TaskEventListener listener) {
        TASK_LISTENER.add(listener);
    }
}
