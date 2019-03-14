/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.cluster.event.executor;


import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.event.command.TaskPushCommand;
import cn.vbill.middleware.porter.common.config.DataConsumerConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @version: V1.0
 * @review: zkevin/2019年03月14日 15:47
 */
public class TaskPushEventExecutor extends ClusterListenerEventExecutor {

    public TaskPushEventExecutor(Class bindClass, boolean regardless, boolean watchTasks, String treeNodePath) {
        super(bindClass, ClusterListenerEventType.TaskPush);
        bind(getBlock(treeNodePath, regardless, watchTasks));
    }

    @SneakyThrows
    private BiConsumer<ClusterCommand, ClusterClient> getBlock(String treeNodePath, boolean regardless, boolean watchTasks) {
        return new BiConsumer<ClusterCommand, ClusterClient>() {
            @SneakyThrows
            public void accept(ClusterCommand clusterCommand, ClusterClient client) {
                TaskPushCommand command = (TaskPushCommand) clusterCommand;
                TaskConfig config = command.getConfig();
                DataConsumerConfig consumerConfig = config.getConsumer();
                // 创建任务根节点
                String taskPath = treeNodePath + "/" + config.getTaskId();
                String distPath = taskPath + "/dist";
                client.create(taskPath, null, false, watchTasks);
                client.create(distPath, null, false, watchTasks);
                // 拆分同步数据来源泳道
                List<SourceConfig> sourceConfigs = SourceConfig.getConfig(consumerConfig.getSource()).swamlanes();
                // 遍历泳道
                for (SourceConfig sc : sourceConfigs) {
                    String pushPath = distPath + "/" + sc.getSwimlaneId();
                    String errorPath = taskPath + "/error/" + sc.getSwimlaneId();
                    if (!config.getStatus().isDeleted()) {
                        // 为每个泳道填充参数
                        config.getConsumer().setSource(sc.getProperties());
                        if (regardless) {
                            client.changeData(pushPath, false, watchTasks, JSONObject.toJSONString(config));
                        } else if (!regardless && !client.isExists(pushPath, watchTasks)) {
                            client.create(pushPath, false, JSONObject.toJSONString(config));
                        }
                    } else {
                        client.delete(pushPath);
                    }
                    client.delete(errorPath);
                }
            }
        };
    }
}
