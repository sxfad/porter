/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.cluster.zookeeper;

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

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        String zkPath = zkEvent.getPath();
        LOGGER.debug("TaskListener:{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());

        // 任务进度更新
        if (TASK_STAT_PATTERN.matcher(zkPath).matches() && zkEvent.isDataChanged()) {
            DTaskStat stat = DTaskStat.fromString(zkEvent.getData(), DTaskStat.class);
            // do something
            System.err.println("DTaskStat.... "+JSON.toJSON(stat));
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
        //泳道id从现有api无法获得，随后修改
        String swimlaneId = "";
        String taskPath = ZK_PATH + "/" + command.getConfig().getTaskId();
        String pushPath = taskPath + "/dist/" + swimlaneId;
        String errorPath = taskPath + "/error/" + swimlaneId;
        client.createWhenNotExists(taskPath, false, false, null);
        client.changeData(pushPath, false, false, JSONObject.toJSONString(command.getConfig()));
        client.delete(errorPath);
    }
}