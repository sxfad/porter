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

package cn.vbill.middleware.porter.common.node.statistics;

import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.event.command.StatisticUploadCommand;
import cn.vbill.middleware.porter.common.statistics.StatisticData;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * 任务日志
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月09日 16:16
 */
public class NodeLog extends StatisticData {
    public static final String NAME = "log";
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeLog.class);

    /**
     * 日志类型 taskStopAlarm 任务停止告警日志，该日志类型不仅会在后台看到，同时会通过邮件、短信推送给监控人。 taskLog
     * 常规任务日志，仅发送给后台系统
     */
    @Setter
    @Getter
    private LogType type;
    // 错误正文
    @Setter
    @Getter
    private String error;
    // 任务所在主机IP
    @Getter
    @Setter
    private String address = MachineUtils.IP_ADDRESS;
    // 任务所在主机
    @Getter
    @Setter
    private String hostName = MachineUtils.HOST_NAME;
    // 进程ID
    @Getter
    @Setter
    private String processId = MachineUtils.getPID() + "";
    // 任务ID
    @Setter
    @Getter
    private String taskId;
    // 泳道ID
    @Setter
    @Getter
    private String swimlaneId;
    // 异常上报时间
    @JSONField(format = "yyyyMMddHHmmss")
    @Setter
    @Getter
    private Date time = Calendar.getInstance().getTime();

    @Setter @Getter
    private String relationship;


    public NodeLog() {
    }
    public NodeLog(LogType type, String error) {
        this.type = type;
        this.error = error;
        setCategory(NAME);
    }

    public NodeLog(LogType type, String taskId, String swimlaneId, String error) {
        this.taskId = taskId;
        this.type = type;
        this.error = error;
        this.swimlaneId = swimlaneId;
        setCategory(NAME);
    }

    public NodeLog bindTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public NodeLog bindSwimlaneId(String swimlaneId) {
        this.swimlaneId = swimlaneId;
        return this;
    }

    public NodeLog bindRelationship(String relationship) {
        this.relationship = relationship;
        return this;
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
     */
    public static NodeLog upload(LogType type, String taskId, String swimlaneId, String error) {
        return new NodeLog(type, taskId, swimlaneId, error).upload();
    }

    /**
     * upload
     */
    public NodeLog upload() {
        try {
            ClusterProviderProxy.INSTANCE.broadcastEvent(new StatisticUploadCommand(this));
        } catch (Throwable e) {
            LOGGER.warn("上传日志统计指标", e);
        }
        return this;
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum LogType {
        /**
         * taskStopAlarm
         */
        ERROR("告警", "error"),

        /**
         * taskLog
         */
        INFO("日常", "info"),

        /**
         * taskWatchAlarm
         */
        WARNING("关注", "warning");
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