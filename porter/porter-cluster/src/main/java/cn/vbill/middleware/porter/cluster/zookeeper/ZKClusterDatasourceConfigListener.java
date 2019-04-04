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

import cn.vbill.middleware.porter.common.task.loader.PublicClientContext;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.task.config.PublicSourceConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 节点监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterDatasourceConfigListener extends ZookeeperClusterListener {
    private static final String ZK_PATH = BASE_CATALOG + "/datesource";

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public List<ClusterListenerEventExecutor> watchedEvents() {
        return null;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent event) {
        logger.info("集群配置参数监听:{},{},{}", event.getId(), event.getData(), event.getEventType());
        try {
            PublicSourceConfig config = null;
            if (event.isDataChanged() || event.isOnline()) {
                config = JSONObject.parseObject(event.getData(), PublicSourceConfig.class);
            } else {
                config = new PublicSourceConfig();
                config.setUsing(false);
                config.setCode(event.getId().substring(event.getId().lastIndexOf("/")));
            }
            if (config.isUsing()) {
                PublicClientContext.INSTANCE.initialize(config.getCode(), SourceConfig.getConfig(config.getSource()));
            } else {
                PublicClientContext.INSTANCE.remove(config.getCode());
            }
        } catch (Throwable e) {
            logger.error("接收初始化公共数据源出错", e);
        }

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
                //应用自身，跳过
                return true;
            }
        };
    }
}