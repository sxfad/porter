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

package cn.vbill.middleware.porter.core.task;

import cn.vbill.middleware.porter.common.node.statistics.NodeLog;
import cn.vbill.middleware.porter.common.warning.WarningProviderFactory;
import cn.vbill.middleware.porter.common.warning.entity.WarningErrorCode;
import cn.vbill.middleware.porter.common.warning.entity.WarningMessage;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.task.loader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 当前任务上下文
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年04月01日 11:06
 * @version: V1.0
 * @review: zkevin/2019年04月01日 11:06
 */
public class TaskContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskContext.class);
    private static final InheritableThreadLocal<TaskInfo> CURRENT_TASK = new InheritableThreadLocal();

    public static void clearTrace() {
        CURRENT_TASK.set(null);
    }

    public static class TaskInfo {
        private String taskId;
        private String swimlaneId;
        private WarningReceiver[] receivers;
        public TaskInfo() {
            receivers = new WarningReceiver[0];
        }
        public TaskInfo(String taskId, DataConsumer consumer, DataLoader loader, WarningReceiver[] receivers) {
            this();
            this.taskId = taskId;
            this.swimlaneId = consumer.getSwimlaneId();
            this.receivers = receivers;
        }

        public String getTaskId() {
            return taskId;
        }

        public String getSwimlaneId() {
            return swimlaneId;
        }

        public WarningReceiver[] getReceivers() {
            return receivers;
        }
    }

    public static void trace(String taskId, DataConsumer consumer, DataLoader loader, List<WarningReceiver> receivers) {
        CURRENT_TASK.set(new TaskInfo(taskId, consumer, loader, receivers.toArray(new WarningReceiver[] {})));
    }

    public static  TaskInfo trace() {
        return CURRENT_TASK.get();
    }

    public static WarningMessage warning(NodeLog log) {
        return warning(log, new StringBuffer().append("【").append(log.getType().getTitle()).append("】")
                .append("【").append(WarningErrorCode.match(log.getError()).name()).append("】")
                .append(log.getTaskId()).append("-").append(log.getSwimlaneId()).toString());
    }
    public static WarningMessage warning(NodeLog log, String title) {
        WarningMessage message = new WarningMessage(title, log.getError(), WarningErrorCode.match(log.getError()));
        try {
            //异常或告警
            if (log.getType() == NodeLog.LogType.ERROR || log.getType() == NodeLog.LogType.WARNING) {
                List<WarningReceiver> receivers = new ArrayList<>(Arrays.asList(trace().receivers));
                if (log.getType() == NodeLog.LogType.ERROR || receivers.isEmpty()) {
                    receivers.addAll(Arrays.asList(NodeContext.INSTANCE.getReceivers()));
                }
                WarningProviderFactory.INSTANCE.notice(message.bindReceivers(receivers.toArray(new WarningReceiver[0])));
            }
            //在应用上下文标注任务异常
            if (log.getType() == NodeLog.LogType.ERROR) {
                NodeContext.INSTANCE.markTaskError(Arrays.asList(log.getTaskId(), log.getSwimlaneId()), message);
            }
        } catch (Throwable e) {
            LOGGER.error("fail to send warning message.", e);
        }
        return message;
    }
}
