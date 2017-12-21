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
import com.suixingpay.datas.common.connector.DataDriverType;
import com.suixingpay.datas.common.connector.meta.KafkaDriverMeta;
import com.suixingpay.datas.common.task.Task;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.data.Stat;

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
public class ZKClusterTaskListener extends ZookeeperClusterListener {
    private final Map<String, Pair<String,String>> TASK_TOPIC;
    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private String nodeId;

    public ZKClusterTaskListener() {
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
            List<Task> tasks = task.getTasks();
            for (Task t : tasks) {
                String taskPath = path() + "/" + t.getTaskId();
                String assignPath = taskPath + "/assign";
                //任务分配、工作节点注册目录创建
                Stat stat = client.exists(path() + "/" + t.getTaskId(), true);
                if (null == stat) {
                    client.create(taskPath,false,"{}");
                    client.create(assignPath,false,"{}");
                }
                //任务分配
                if (t.getDataDriver().getType() == DataDriverType.KAFKA) {
                    KafkaDriverMeta meta = (KafkaDriverMeta) DataDriverType.KAFKA.getMeta();
                    String[] topics = t.getDataDriver().getExtendAttr().getOrDefault(meta.TOPIC, "").split(",");
                    for (String topic : topics) {
                        Stat ifAssignTopic = client.exists(path() + "/" + t.getTaskId(), true);
                        if (null != ifAssignTopic) {
                            //为当前工作节点分配任务topic
                            client.create(assignPath + "/" + topic ,false,new DTaskStat(t.getTaskId(), nodeId ,topic).toString());
                            //保存映射关系到本地内存
                            TASK_TOPIC.putIfAbsent(t.getTaskId() + "_" + topic, new ImmutablePair<>(t.getTaskId(), topic));
                            //通知对此感兴趣的Listener
                            ClusterProvider.sendCommand(new TaskAssignedCommand(t.getTaskId(),topic));
                        }
                    }
                }
            }
        } else if (command instanceof ShutwdownCommand) {
            for (Pair<String,String> kv : TASK_TOPIC.values()) {
                String node = path() + "/" + kv.getLeft() + "/assign/" + kv.getRight();
                Stat stat = client.exists(node, false);
                if (null != stat) {
                    DTaskStat taskStat = DTaskStat.fromString(client.getData(node),DTaskStat.class);
                    if (taskStat.getNodeId().equals(nodeId)) {
                        client.delete(node);
                    }
                }
            }
        }
    }
}
