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

package cn.vbill.middleware.porter.common.cluster.event.command;

import cn.vbill.middleware.porter.common.cluster.data.DTaskStat;
import cn.vbill.middleware.porter.common.cluster.data.DCallback;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;

/**
 * 任务状态上报（服务器上报zk）
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 18:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 18:42
 */
public class TaskStatCommand extends ClusterCommand {
    private final DTaskStat stat;
    private final DCallback callback;
    public TaskStatCommand(DTaskStat stat, DCallback callback) {
        this.stat = stat;
        this.callback = callback;
    }

    public DTaskStat getStat() {
        return stat;
    }

    public DCallback getCallback() {
        return callback;
    }

    @Override
    public ClusterListenerEventType getClusterListenerEventType() {
        return ClusterListenerEventType.TaskStatUpload;
    }
}
