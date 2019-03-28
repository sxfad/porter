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
    private BiConsumer<ClusterCommand, ClusterClient> getBlock(String treeNodePath, boolean regardless,
            boolean watchTasks) {
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
                    // 为每个泳道填充参数
                    config.getConsumer().setSource(sc.getProperties());
                    if (regardless) {
                        client.changeData(pushPath, false, watchTasks, JSONObject.toJSONString(config));
                    } else if (!regardless && !client.isExists(pushPath, watchTasks)) {
                        client.create(pushPath, false, JSONObject.toJSONString(config));
                    }
                    client.delete(errorPath);
                }
            }
        };
    }
}
