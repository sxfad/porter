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

package cn.vbill.middleware.porter.manager.cluster.zookeeper;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.command.TaskPushCommand;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskPush;
import cn.vbill.middleware.porter.common.cluster.data.DTaskStat;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import cn.vbill.middleware.porter.common.config.DataConsumerConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import cn.vbill.middleware.porter.manager.ManagerContext;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.service.MrJobTasksScheduleService;
import cn.vbill.middleware.porter.manager.service.impl.MrJobTasksScheduleServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 任务信息监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterTaskListener extends ZookeeperClusterListener implements TaskPush {

    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private static final Pattern TASK_STAT_PATTERN = Pattern.compile(ZK_PATH + "/.*/stat/.*");
    private static final Pattern TASK_ERROR_PATTERN = Pattern.compile(ZK_PATH + "/.*/error/.*");

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKClusterTaskListener.class);

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        String zkPath = zkEvent.getPath();
        LOGGER.debug("TaskListener:{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        try {
            // 任务进度更新
            if (TASK_STAT_PATTERN.matcher(zkPath).matches() && zkEvent.isDataChanged()) {
                DTaskStat stat = DTaskStat.fromString(zkEvent.getData(), DTaskStat.class);
                LOGGER.info("4-DTaskStat.... " + JSON.toJSON(stat));
                // do something
                try {
                    MrJobTasksScheduleService mrJobTasksScheduleService = ApplicationContextUtil
                            .getBean(MrJobTasksScheduleServiceImpl.class);
                    mrJobTasksScheduleService.dealDTaskStat(stat);
                } catch (Exception e) {
                    LOGGER.error("4-DTaskStat-Error....出错,请追寻...", e);
                }
            }
            // 任务错误
            if (TASK_ERROR_PATTERN.matcher(zkEvent.getPath()).matches()) {
                String[] taskAndSwimlane = null;
                try {
                    taskAndSwimlane = zkPath.replace(listenPath(), "").substring(1).split("/error/");
                } catch (Throwable e) {
                    LOGGER.error("zk任务错误消息解析失败！", e);
                }
                if (null == taskAndSwimlane || taskAndSwimlane.length != 2) {
                    LOGGER.error("zk任务错误消息未解析出合规的内容 [{}]",JSON.toJSONString(taskAndSwimlane));
                    return;
                }
                if (zkEvent.isDataChanged() || zkEvent.isOnline()) {
                    ManagerContext.INSTANCE.newStoppedTask(taskAndSwimlane[0], taskAndSwimlane[1]);
                    LOGGER.info("zk任务错误消息DataChanged or Online,内容:[{}]",JSON.toJSONString(taskAndSwimlane));
                    return;
                }
                if (zkEvent.isOffline()) {
                    LOGGER.info("zk任务错误消息Offline,内容:[{}]",JSON.toJSONString(taskAndSwimlane));
                    ManagerContext.INSTANCE.removeStoppedTask(taskAndSwimlane[0], taskAndSwimlane[1]);
                    return;
                }
            }
        } catch (Throwable e) {
            LOGGER.error("4-DTaskStat-Throwable....出错,请追寻...", e);
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
    public void push(TaskPushCommand command) throws Exception {
        TaskConfig config = command.getConfig();
        DataConsumerConfig consumerConfig = config.getConsumer();
        // 创建任务根节点
        String taskPath = ZK_PATH + "/" + config.getTaskId();
        String distPath = taskPath + "/dist";
        client.createWhenNotExists(taskPath, false, false, null);
        client.createWhenNotExists(distPath, false, false, null);
        // 拆分同步数据来源泳道
        List<SourceConfig> sourceConfigs = SourceConfig.getConfig(consumerConfig.getSource()).swamlanes();
        // 遍历泳道
        for (SourceConfig sc : sourceConfigs) {
            String pushPath = distPath + "/" + sc.getSwimlaneId();
            String errorPath = taskPath + "/error/" + sc.getSwimlaneId();
            if (!config.getStatus().isDeleted()) {
                // 为每个泳道填充参数
                config.getConsumer().setSource(sc.getProperties());
                client.changeData(pushPath, false, false, JSONObject.toJSONString(config));
            } else {
                client.delete(pushPath);
            }
            client.delete(errorPath);
        }
    }
}