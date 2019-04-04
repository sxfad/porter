/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年04月01日 11:06
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
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

import java.util.List;

/**
 * 当前任务上下文
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年04月01日 11:06
 * @version: V1.0
 * @review: zkevin/2019年04月01日 11:06
 */
public class TaskContext {

    private static final ThreadLocal<TaskInfo> CURRENT_TASK = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskContext.class);
    public static class TaskInfo {
        private String taskId;
        private String swimlaneId;
        private DataConsumer consumer;
        private DataLoader loader;
        private WarningReceiver[] receivers;

        public TaskInfo(String taskId, DataConsumer consumer, DataLoader loader, WarningReceiver[] receivers) {
            this.taskId = taskId;
            this.swimlaneId = consumer.getSwimlaneId();
            this.consumer = consumer;
            this.loader = loader;
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

    public static synchronized void trace(String taskId, DataConsumer consumer, DataLoader loader, List<WarningReceiver> receivers) {
        CURRENT_TASK.set(new TaskInfo(taskId, consumer, loader, receivers.toArray(new WarningReceiver[] {})));
    }

    public static  TaskInfo trace() {
        return CURRENT_TASK.get();
    }

    public static void warning(NodeLog log) {
        try {
            if (log.getType() == NodeLog.LogType.ERROR) {
                WarningProviderFactory.INSTANCE.notice(new WarningMessage(log.getTitle(), log.getError(), WarningErrorCode.match(log.getError())), NodeContext.INSTANCE.getReceivers());
                WarningProviderFactory.INSTANCE.notice(new WarningMessage(log.getTitle(), log.getError(), WarningErrorCode.match(log.getError())), trace().receivers);
            }
            if (log.getType() == NodeLog.LogType.WARNING) {
                WarningProviderFactory.INSTANCE.notice(new WarningMessage(log.getTitle(), log.getError(), WarningErrorCode.match(log.getError())), trace().receivers);
            }
        } catch (Throwable e) {
            LOGGER.error("fail to send warning", e);
        }
    }
}
