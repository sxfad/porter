/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.statistics;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.suixingpay.datas.common.alert.AlertProviderFactory;
import com.suixingpay.datas.common.alert.AlertReceiver;
import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.StatisticUploadCommand;
import com.suixingpay.datas.common.util.MachineUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 任务日志
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月09日 16:16
 */
public class NodeLog extends StatisticData {
    private static final String NAME = "log";
    /**
     *日志类型
     *taskStopAlarm 任务停止告警日志，该日志类型不仅会在后台看到，同时会通过邮件、短信推送给监控人。
     *taskLog  常规任务日志，仅发送给后台系统
     */
    @Setter @Getter private LogType type;
    //错误正文
    @Setter @Getter private String error;
    //任务所在主机IP
    @Getter @Setter private String address = MachineUtils.IP_ADDRESS;
    //任务所在主机
    @Getter @Setter private String hostName = MachineUtils.HOST_NAME;
    //进程ID
    @Getter @Setter private String processId = MachineUtils.getPID() + "";
    //任务ID
    @Setter @Getter private String taskId;
    //泳道ID
    @Setter @Getter private String swimlaneId;
    //异常上报时间
    @JSONField(format = "yyyyMMddHHmmss")
    @Setter @Getter private Date time;

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

    public static void upload(LogType type, String taskId, String swimlaneId, String error, List<AlertReceiver> receivers) {
        try {
            NodeLog log = new NodeLog(type, taskId, swimlaneId, error);
            ClusterProviderProxy.INSTANCE.broadcast(new StatisticUploadCommand(log));
            LOGGER.info("判断是否发送邮件通知....." + JSONObject.toJSONString(log));
            if (type == LogType.TASK_ALARM) {
                LOGGER.info("需要发送邮件通知.....");
                AlertProviderFactory.INSTANCE.notice(type.title, log.toPrintln(), receivers);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void upload(LogType type, String taskId, String swimlaneId, String error) {
        upload(type, taskId, swimlaneId, error, null);
    }

    public static void upload(String taskId, LogType type, String error) {
        upload(type, taskId, "", error, null);
    }

    public static void upload(LogType type, String error) {
        upload(type, "", "", error, null);
    }


    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum LogType {
        TASK_ALARM("任务停止告警", "taskStopAlarm"), TASK_LOG("任务日志", "taskLog");
        @Getter private String title;
        @Getter private String type;
        LogType(String title, String type) {
            this.title = title;
            this.type = type;
        }
    }
}
