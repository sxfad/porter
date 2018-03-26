/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月04日 17:56
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.cluster.command;

import lombok.Getter;

/**
 * 服务器上报zk 任务消费进度 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月04日 17:56
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月04日 17:56
 */
public class TaskPositionUploadCommand implements ClusterCommand {
    @Getter private final String taskId;
    @Getter private final String swimlaneId;
    @Getter private final String position;

    public TaskPositionUploadCommand(String taskId, String swimlaneId, String position) {
        this.taskId = taskId;
        this.swimlaneId = swimlaneId;
        this.position = position;
    }
}
