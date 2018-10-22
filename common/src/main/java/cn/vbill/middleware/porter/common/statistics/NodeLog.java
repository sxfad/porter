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

package cn.vbill.middleware.porter.common.statistics;

import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.command.StatisticUploadCommand;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.vbill.middleware.porter.common.alert.AlertProviderFactory;
import cn.vbill.middleware.porter.common.alert.AlertReceiver;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 任务日志
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月09日 16:16
 */
public class NodeLog extends StatisticData {
    private static final String NAME = "log";
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeLog.class);

    /**
     * 日志类型
     * taskStopAlarm 任务停止告警日志，该日志类型不仅会在后台看到，同时会通过邮件、短信推送给监控人。
     * taskLog  常规任务日志，仅发送给后台系统
     */
    @Setter
    @Getter
    private LogType type;
    //错误正文
    @Setter
    @Getter
    private String error;
    //任务所在主机IP
    @Getter
    @Setter
    private String address = MachineUtils.IP_ADDRESS;
    //任务所在主机
    @Getter
    @Setter
    private String hostName = MachineUtils.HOST_NAME;
    //进程ID
    @Getter
    @Setter
    private String processId = MachineUtils.getPID() + "";
    //任务ID
    @Setter
    @Getter
    private String taskId;
    //泳道ID
    @Setter
    @Getter
    private String swimlaneId;
    //异常上报时间
    @JSONField(format = "yyyyMMddHHmmss")
    @Setter
    @Getter
    private Date time;
    @Setter
    @Getter
    private String title;

    public NodeLog() {
        this.time = new Date();
    }

    public NodeLog(LogType type, String taskId, String swimlaneId, String error) {
        this();
        this.taskId = taskId;
        this.type = type;
        this.error = error;
        this.swimlaneId = swimlaneId;
    }

    @Override
    public String getCategory() {
        return NAME;
    }

    @Override
    protected String getSubId() {
        return processId;
    }

    /**
     * upload
     *
     * @param type
     * @param taskId
     * @param swimlaneId
     * @param error
     * @param receivers
     */
    public static void upload(LogType type, String taskId, String swimlaneId, String error, List<AlertReceiver> receivers) {
        upload(new NodeLog(type, taskId, swimlaneId, error), receivers);
    }

    /**
     * upload
     *
     * @param log
     * @param receivers
     */
    public static void upload(NodeLog log, List<AlertReceiver> receivers) {
        try {
            ClusterProviderProxy.INSTANCE.broadcast(new StatisticUploadCommand(log));
            LOGGER.info("判断是否发送邮件通知....." + JSONObject.toJSONString(log));
            if (log.type == LogType.TASK_ALARM || log.type == LogType.TASK_WARNING) {
                LOGGER.info("需要发送邮件通知.....");
                AlertProviderFactory.INSTANCE.notice(StringUtils.isBlank(log.type.title) ? log.type.title : log.title, log.toPrintln(), receivers);
            }
        } catch (Throwable e) {
            LOGGER.warn("发送邮件通知", e);
        }
    }

    /**
     * upload
     *
     * @param type
     * @param taskId
     * @param swimlaneId
     * @param error
     */
    public static void upload(LogType type, String taskId, String swimlaneId, String error) {
        upload(new NodeLog(type, taskId, swimlaneId, error), null);
    }

    /**
     * upload
     *
     * @param taskId
     * @param type
     * @param error
     */
    public static void upload(String taskId, LogType type, String error) {
        upload(new NodeLog(type, taskId, null, error), null);
    }

    /**
     * upload
     *
     * @param type
     * @param error
     */
    public static void upload(LogType type, String error) {
        upload(new NodeLog(type, "", "", error), null);
    }


    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum LogType {
        /**
         * taskStopAlarm
         */
        TASK_ALARM("任务停止告警", "taskStopAlarm"),

        /**
         * taskLog
         */
        TASK_LOG("任务日志", "taskLog"),

        /**
         * taskWatchAlarm
         */
        TASK_WARNING("任务关注警告", "taskWatchAlarm");
        @Getter
        private String title;
        @Getter
        private String type;

        LogType(String title, String type) {
            this.title = title;
            this.type = type;
        }
    }
}
