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

package cn.vbill.middleware.porter.common.cluster;

import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.client.DistributedLock;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.config.ClusterConfig;
import cn.vbill.middleware.porter.common.task.TaskEventListener;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.dic.ClusterPlugin;

import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 11:10
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 11:10
 */
public interface ClusterProvider {
    /**
     * 用于集群监听到任务事件的分发
     * @param listener
     */
    void addTaskEventListener(TaskEventListener listener);

    /**
     * 用于集群监听到任务事件的分发
     * @param listener
     */
    void removeTaskEventListener(TaskEventListener listener);

    /**
     * 启动集群模块
     * @throws IOException
     */
    void start(ClusterConfig config) throws Exception;

    /**
     * 匹配配置文件指定的集群实现
     * @param type
     * @return
     */
    boolean matches(ClusterPlugin type);

    /**
     * 退出集群
     * 需在业务代码执行之后才能执行,进程退出Hook
     */
    void stop();

    /**
     * 命令广播，用于在别的模块通知集群的相关模块
     * @param command
     * @throws Exception
     */
    void broadcastEvent(ClusterCommand command) throws Exception;

    /**
     * 集群插件是否有效
     */
    boolean available();

    /**
     * 分布式锁功能实现
     * @return
     */
    DistributedLock getLock();

    void registerClusterEvent(ClusterListenerEventExecutor eventExecutor);
    void runWithClusterEvent(BiConsumer<ClusterCommand, ClusterClient> block, ClusterCommand command);
}
