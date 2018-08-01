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

import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import com.alibaba.fastjson.JSON;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.command.ConfigPushCommand;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.ConfigPush;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import org.apache.commons.lang3.StringUtils;

/**
 * 后台配置推送
 * 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterConfigListener extends ZookeeperClusterListener implements ConfigPush {
    private static final String ZK_PATH = BASE_CATALOG + "/config";
    private static final String LOG_CONFIG_PATH = ZK_PATH + "/log";
    private static final String ALERT_CONFIG_PATH = ZK_PATH + "/alert";

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        LOGGER.info("1-ZKClusterConfigListener....." + JSON.toJSONString(event));
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
    public void push(ConfigPushCommand command) throws Exception {
        String configPath = "";
        if (command.getType().isLog()) {
            configPath = LOG_CONFIG_PATH;
        } else if (command.getType().isAlert()) {
            configPath = ALERT_CONFIG_PATH;
        }

        if (!StringUtils.isBlank(configPath)) {
            client.changeData(configPath, false, false, command.render());
        }
    }
}