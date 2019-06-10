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
import cn.vbill.middleware.porter.common.warning.entity.WarningOwner;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.task.loader.DataLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final ThreadLocal<TaskInfo> CURRENT_TASK = new InheritableThreadLocal();

    public static void clearTrace() {
        CURRENT_TASK.set(null);
    }

    public static class TaskInfo {
        private String taskId;
        private String swimlaneId;
        private WarningOwner owner;
        public TaskInfo() {
            owner = new WarningOwner();
        }
        public TaskInfo(String taskId, DataConsumer consumer, DataLoader loader, WarningOwner owner) {
            this();
            this.taskId = taskId;
            this.swimlaneId = consumer.getSwimlaneId();
            this.owner = owner;
        }

        public String getTaskId() {
            return taskId;
        }

        public String getSwimlaneId() {
            return swimlaneId;
        }

        public WarningOwner getOwner() {
            return owner;
        }
        public void setOwner(WarningOwner owner) {
            this.owner = owner;
        }
    }

    public static void trace(String taskId, DataConsumer consumer, DataLoader loader, List<WarningReceiver> receivers) {
        WarningOwner owner = new WarningOwner();
        if (null != receivers && receivers.size() > 0) owner.setOwner(receivers.get(0));
        if (null != receivers && receivers.size() > 1) owner.setShareOwner(receivers.subList(1, receivers.size()));
        CURRENT_TASK.set(new TaskInfo(taskId, consumer, loader, owner));
    }
    public static void trace(WarningOwner owner) {
        TaskInfo info = CURRENT_TASK.get();
        if (null == info) CURRENT_TASK.set(new TaskInfo());
        info.setOwner(owner);
    }

    public static  TaskInfo trace() {
        return CURRENT_TASK.get();
    }

    public static  String taskOwnerInfo() {
        TaskInfo taskInfo = trace();
        StringBuilder sb = new StringBuilder();
        if (null != taskInfo && null != taskInfo.getOwner() && null != taskInfo.getOwner().getOwner()) {
            sb.append(StringUtils.trimToEmpty(taskInfo.getOwner().getOwner().getRealName())).append("-");
            sb.append(StringUtils.trimToEmpty(taskInfo.getOwner().getOwner().getPhone())).append("-");
            sb.append(StringUtils.trimToEmpty(taskInfo.getOwner().getOwner().getEmail()));
        }
        return sb.toString();
    }

    public static  String taskId() {
        TaskInfo taskInfo = trace();
        if (null != taskInfo) {
            return StringUtils.trimToEmpty(taskInfo.taskId);
        }
        return StringUtils.EMPTY;
    }
    public static  String swimlaneId() {
        TaskInfo taskInfo = trace();
        if (null != taskInfo) {
            return StringUtils.trimToEmpty(taskInfo.swimlaneId);
        }
        return StringUtils.EMPTY;
    }



    public static WarningMessage warning(NodeLog log) {
        return warning(log, null);
    }
    public static WarningMessage warning(NodeLog log, String title) {
        WarningMessage message = new WarningMessage(title, log.toPrintln(), WarningErrorCode.match(log.getError()), null != trace() ? trace().getOwner() : null);
        try {
            //任务信息
            message.appendTitlePrefix(Arrays.asList(log.getTaskId(), log.getSwimlaneId()));
            //异常或告警
            if (log.getType() == NodeLog.LogType.ERROR || log.getType() == NodeLog.LogType.WARNING) {
                if (log.getType() == NodeLog.LogType.ERROR) {
                    message.bindCopy(NodeContext.INSTANCE.getReceivers());
                }
                WarningProviderFactory.INSTANCE.notice(message);
            }
        } catch (Throwable e) {
            LOGGER.error("fail to send warning message.", e);
        } finally {
            //在应用上下文标注任务异常
            if (log.getType() == NodeLog.LogType.ERROR) {
                NodeContext.INSTANCE.markTaskError(Arrays.asList(log.getTaskId(), log.getSwimlaneId()), message);
            }
        }
        return message;
    }
}
