/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.cluster.zookeeper;

import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.command.*;
import com.suixingpay.datas.common.cluster.command.broadcast.*;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import com.suixingpay.datas.common.statistics.StatisticData;
import com.suixingpay.datas.node.core.NodeContext;
import org.apache.zookeeper.data.Stat;

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
    public void onEvent(ClusterEvent event) {

    }

    @Override
    public ClusterListenerFilter filter() {
        return new ZookeeperClusterListenerFilter(){
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
        String statisticPath = listenPath() + "/" + data.getCategory();
        Stat stat = client.exists(statisticPath, false);
        if (null == stat) {
            client.create(statisticPath, false, null);
        }
        String dataNode = statisticPath + "/" + data.getId();
        client.create(dataNode, true,data.toString());
    }
}