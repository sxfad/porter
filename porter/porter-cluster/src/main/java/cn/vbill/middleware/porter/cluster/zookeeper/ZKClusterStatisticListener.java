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

package cn.vbill.middleware.porter.cluster.zookeeper;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.StatisticUploadCommand;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.statistics.StatisticData;
import cn.vbill.middleware.porter.core.NodeContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计信息上传
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterStatisticListener extends ZookeeperClusterListener {
    private static final String ZK_PATH = BASE_CATALOG + "/statistic";

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public boolean watchListenPath() {
        return false;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent event) {

    }

    @Override
    public ClusterListenerFilter filter() {
        return new ClusterListenerFilter() {
            @Override
            public String getPath() {
                return listenPath();
            }

            @Override
            public boolean doFilter(ClusterTreeNodeEvent event) {
                return false;
            }
        };
    }

    @Override
    public void start() {
        client.create(listenPath(),  null, false, false);
        client.create(listenPath() + "/task", "{}", false, false);
        client.create(listenPath() + "/log", "{}", false, false);
    }

    @Override
    public List<ClusterListenerEventExecutor> watchedEvents() {
        List<ClusterListenerEventExecutor> executors = new ArrayList<>();
        //消费进度上传事件
        executors.add(new ClusterListenerEventExecutor(this.getClass(), ClusterListenerEventType.TaskStop).bind((command, client) -> {
            StatisticData data = ((StatisticUploadCommand) command).getStatisticData();
            data.setNodeId(NodeContext.INSTANCE.getNodeId());
            client.create(listenPath() + "/" + data.getCategory(), "{}", false, false);
            String dataNode = listenPath() + "/" + data.getCategory() + "/" + data.getId();
            client.uploadStatistic(dataNode, data.getKey(), data.toString());
        }, client));
        return executors;
    }
}