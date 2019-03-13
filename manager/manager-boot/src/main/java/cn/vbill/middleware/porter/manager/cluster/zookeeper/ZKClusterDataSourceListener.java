/**
 * 
 */
package cn.vbill.middleware.porter.manager.cluster.zookeeper;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.command.DataSourcePushCommand;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.DataSourcePush;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class ZKClusterDataSourceListener extends ZookeeperClusterListener implements DataSourcePush {

    private static final String ZK_PATH = BASE_CATALOG + "/datesource";

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
    public void push(DataSourcePushCommand command) throws Exception {
        String configPath = ZK_PATH + "/" + command.getLoader().getLoaderName();
        if (!StringUtils.isBlank(configPath)) {
            client.changeData(configPath, false, false, JSON.toJSONString(command.getLoader()));
        }
    }

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

}
