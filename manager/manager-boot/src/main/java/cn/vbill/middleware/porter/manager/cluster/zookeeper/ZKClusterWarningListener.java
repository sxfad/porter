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

import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.warning.WarningProviderFactory;
import cn.vbill.middleware.porter.common.warning.entity.WarningMessage;

/**
 * 邮件告警监听，控制台发送
 *
 * @author: guohongjian[wszghj@aliyun.com]
 * @date: 2019年07月25日 10:09
 * @version: V1.0
 * @review: guohongjian[wszghj@aliyun.com]/2019年07月25日 10:09
 */
public class ZKClusterWarningListener extends ZookeeperClusterListener {

    private static final String ZK_PATH = BASE_CATALOG + "/warning";
    private static final Pattern LOG_PATTERN = Pattern.compile(ZK_PATH + "/.*");

    @Override
    public void onEvent(ClusterTreeNodeEvent zkEvent) {
        String zkPath = zkEvent.getId();
        logger.debug("5-ZKClusterWarningListener:{},{},{}", zkPath, zkEvent.getData(), zkEvent.getEventType());
        if (zkEvent.isOnline() && LOG_PATTERN.matcher(zkPath).matches()) {
            try {
                logger.info("5-ZKClusterWarningListener...[{}]", zkEvent.getData());
                WarningMessage message = JSONObject.parseObject(zkEvent.getData(), WarningMessage.class);
                if (message != null && 1 == message.getStreamTime()) {
                    WarningProviderFactory.INSTANCE.notice(message);
                } else {
                    logger.warn("5-ZKClusterWarningListener-Error...告警数据逻辑错误,WarningMessage数据:[{}]", zkEvent.getData());
                }
            } catch (Exception e) {
                logger.error("5-ZKClusterWarningListener-Error....邮件发送出错,WarningMessage数据:[{}],请追寻...",
                        zkEvent.getData(), e);
            } finally {
                client.delete(zkPath);
            }
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
                return true;
            }
        };
    }

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void start() {
        client.create(ZK_PATH, null, false, true);
    }
}
