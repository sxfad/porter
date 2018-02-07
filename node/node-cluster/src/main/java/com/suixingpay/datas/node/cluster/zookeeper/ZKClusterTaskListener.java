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
import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.command.*;
import com.suixingpay.datas.common.cluster.data.DObject;
import com.suixingpay.datas.common.cluster.data.DTaskLock;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.event.EventType;
import com.suixingpay.datas.common.cluster.zookeeper.ZookeeperClusterEvent;
import com.suixingpay.datas.common.cluster.zookeeper.ZookeeperClusterListener;
import com.suixingpay.datas.common.cluster.zookeeper.ZookeeperClusterListenerFilter;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.task.TaskEvent;
import com.suixingpay.datas.common.task.TaskEventListener;
import com.suixingpay.datas.common.task.TaskEventProvider;
import com.suixingpay.datas.common.task.TaskEventType;
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
public class ZKClusterTaskListener extends ZookeeperClusterListener implements TaskEventProvider{
    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private static final Pattern ALERT_PATTERN = Pattern.compile(ZK_PATH + "/[0-9]*/alert/.*");
    private final List<TaskEventListener> TASK_LISTENER;
    private String nodeId;

    public ZKClusterTaskListener() {
        this.TASK_LISTENER = new ArrayList<>();
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
        LOGGER.debug("{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        //警告节点建立
        Matcher alertMatcher = ALERT_PATTERN.matcher(zkEvent.getPath());
        if (alertMatcher.matches() && (zkEvent.getEventType() == EventType.ONLINE || zkEvent.getEventType() == EventType.DATA_CHANGED)) {
            DTaskStat stat = DTaskStat.fromString(event.getData(), DTaskStat.class);
            triggerTaskEvent(new TaskEvent(stat, TaskEventType.STAT_READY));
        }
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
            String assignPath = taskPath + "/lock";
            String statPath = taskPath + "/stat";
            //任务分配、工作节点注册目录创建
            Stat stat = client.exists(path() + "/" + task.getTaskId(), true);
            if (null == stat) {
                client.create(taskPath,false,"{}");
                client.create(assignPath,false,"{}");
                client.create(statPath,false,"{}");
            }

            //创建任务统计节点
            try {
                String alertNode = statPath + "/" + task.getResourceId();
                if (null == client.exists(alertNode, true)) {
                    client.create(alertNode,false ,"{}");
                }
            } catch (Exception e) {
                LOGGER.error("创建任务统计节点失败->task:{},node:{},consume-resource:{},", task.getTaskId(), nodeId, task.getResourceId(), e);
            }

            //任务分配
            String topicPath = assignPath + "/" + task.getResourceId();
            Stat ifAssignTopic = client.exists(topicPath, true);
            if (null == ifAssignTopic) {
                //为当前工作节点分配任务topic
                client.create(topicPath,false, new DTaskLock(task.getTaskId(), nodeId, task.getResourceId()).toString());
                //通知对此感兴趣的Listener
                ClusterProvider.sendCommand(new TaskAssignedCommand(task.getTaskId(),task.getResourceId()));
            } else {
                throw  new Exception(topicPath+",锁定资源失败。");
            }
        } else if (command instanceof TaskStopCommand) {
            TaskStopCommand stopCommand = (TaskStopCommand) command;
            String node = path() + "/" + stopCommand.getTaskId() + "/lock/" + stopCommand.getResourceId();
            Stat stat = client.exists(node, false);
            if (null != stat) {
                Pair<String, Stat> remoteData = client.getData(node);
                DTaskLock taskLock = DTaskLock.fromString(remoteData.getLeft(), DTaskLock.class);
                if (taskLock.getNodeId().equals(nodeId)) {
                    client.delete(node);
                }
            }
        } else if (command instanceof TaskStatCommand) {
            TaskStatCommand statCommand = (TaskStatCommand) command;
            DTaskStat dataStat = statCommand.getStat();
            //.intern()保证全局唯一字符串对象
            String node = (path() + "/" +dataStat.getTaskId() + "/stat/" + dataStat.getResourceId()
                    + "/" + dataStat.getSchema()+ "." + dataStat.getTable()).intern();
            //控制锁的粒度到每个consume-resource节点，
            synchronized (node) {
                Stat stat = client.exists(node, true);
                //节点不存在，创建新节点
                if (null == stat) {
                    DTaskStat thisStat = new DTaskStat(dataStat.getTaskId(), nodeId, dataStat.getResourceId(), dataStat.getSchema(), dataStat.getTable());
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
                    if (null != statCommand.getCallback()) statCommand.getCallback().callback(taskStat);
                    //merge from localStat
                    taskStat.merge(dataStat);
                    //upload stat
                    client.setData(node, taskStat.toString(), nodePair.getRight().getVersion());

                    LOGGER.debug("stat store in zookeeper:{}", JSON.toJSONString(taskStat));
                }
            }
        } else if (command instanceof TaskStatQueryCommand) {
            TaskStatQueryCommand queryCommand = (TaskStatQueryCommand) command;
            String node = path() + "/" +queryCommand.getTaskId() + "/stat/" + queryCommand.getResourceId();
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
            if(null != queryCommand.getCallback()) queryCommand.getCallback().callback(stats);
        }
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
}