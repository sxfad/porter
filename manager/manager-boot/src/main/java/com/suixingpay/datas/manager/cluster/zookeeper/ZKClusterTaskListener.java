/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.cluster.zookeeper;

import java.util.List;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.command.TaskPushCommand;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskPush;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import com.suixingpay.datas.common.config.DataConsumerConfig;
import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.config.TaskConfig;
import com.suixingpay.datas.manager.ManagerContext;
import com.suixingpay.datas.manager.core.util.ApplicationContextUtil;
import com.suixingpay.datas.manager.service.MrJobTasksScheduleService;
import com.suixingpay.datas.manager.service.impl.MrJobTasksScheduleServiceImpl;

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
                }

                if (null == taskAndSwimlane || taskAndSwimlane.length != 2)
                    return;

                if (zkEvent.isDataChanged() || zkEvent.isOnline()) {
                    ManagerContext.INSTANCE.newStoppedTask(taskAndSwimlane[0], taskAndSwimlane[1]);
                    return;
                }

                if (zkEvent.isOffline()) {
                    ManagerContext.INSTANCE.removeStoppedTask(taskAndSwimlane[0], taskAndSwimlane[1]);
                    return;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
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