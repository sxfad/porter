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
import cn.vbill.middleware.porter.common.cluster.command.StatisticUploadCommand;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.StatisticUpload;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import cn.vbill.middleware.porter.common.statistics.StatisticData;
import cn.vbill.middleware.porter.core.NodeContext;

/**
 * 统计信息上传
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterStatisticListener extends ZookeeperClusterListener implements StatisticUpload {
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
    public void onEvent(ClusterEvent event) {

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
                return false;
            }
        };
    }

    @Override
    public void upload(StatisticUploadCommand command) throws Exception {
        StatisticData data = command.getStatisticData();
        data.setNodeId(NodeContext.INSTANCE.getNodeId());
        String dataNode = listenPath() + "/" + data.getCategory() + "/" + data.getId();
        client.uploadStatistic(dataNode, data.getKey(), data.toString());
    }

    @Override
    public void start() {
        client.createWhenNotExists(listenPath(), false, false, null);
        client.createWhenNotExists(listenPath() + "/task", false, false, "{}");
        client.createWhenNotExists(listenPath() + "/log", false, false, "{}");
    }
}