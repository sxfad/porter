/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.statistics;

import com.alibaba.fastjson.annotation.JSONField;
import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.StatisticUploadCommand;
import com.suixingpay.datas.common.util.MachineUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 任务日志
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月09日 16:16
 */
public class NodeLog extends StatisticData {
    private static final String NAME = "log";
    @Setter @Getter private String taskId;
    @Getter @Setter private String address = MachineUtils.IP_ADDRESS;
    @Getter @Setter private String hostName = MachineUtils.HOST_NAME;
    @Getter @Setter private String processId = MachineUtils.getPID() + "";
    @Setter @Getter private String title;
    @Setter @Getter private String error;
    @Setter @Getter private String swimlaneId;

    @JSONField(format = "yyyyMMddHHmmss")
    @Setter @Getter private Date time;

    public NodeLog() {
        this.time = new Date();
    }

    public NodeLog(String taskId, String title, String error, String swimlaneId) {
        this();
        this.taskId = taskId;
        this.title = title;
        this.error = error;
        this.swimlaneId = swimlaneId;
    }

    public NodeLog(String taskId, String title, String content) {
        this(taskId, title, "", content);
    }

    @Override
    public String getCategory() {
        return NAME;
    }

    public static void upload(String taskId, String title, String error, String swimlaneId) {
        try {
            ClusterProviderProxy.INSTANCE.broadcast(new StatisticUploadCommand(new NodeLog(taskId, title, error, swimlaneId)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void upload(String taskId, String title, String error) {
        upload(taskId, title, error, "");
    }

    public static void upload(String title, String error) {
        upload("", title, error, "");
    }
}
