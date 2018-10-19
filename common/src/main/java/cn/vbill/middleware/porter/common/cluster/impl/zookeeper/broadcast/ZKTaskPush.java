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

package cn.vbill.middleware.porter.common.cluster.impl.zookeeper.broadcast;

import cn.vbill.middleware.porter.common.client.impl.ZookeeperClient;
import cn.vbill.middleware.porter.common.cluster.command.TaskPushCommand;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskPush;
import cn.vbill.middleware.porter.common.config.DataConsumerConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 15:43
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 15:43
 */
public interface ZKTaskPush extends TaskPush {

    void push(TaskPushCommand command) throws Exception;

    /**
     * push
     * @param command
     * @throws Exception
     */
    default void push(TaskPushCommand command, boolean regardless, boolean watchTasks) throws Exception {
        ZookeeperClient client = getZKClient();
        TaskConfig config = command.getConfig();
        DataConsumerConfig consumerConfig = config.getConsumer();
        // 创建任务根节点
        String taskPath = zkTaskPath() + "/" + config.getTaskId();
        String distPath = taskPath + "/dist";
        client.createWhenNotExists(taskPath, false, watchTasks, null);
        client.createWhenNotExists(distPath, false, watchTasks, null);
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
    ZookeeperClient getZKClient();
    String zkTaskPath();
}
