/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月12日 18:46
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.cluster.command;

import com.suixingpay.datas.common.cluster.data.DCallback;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月12日 18:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月12日 18:46
 */
public class TaskStatQueryCommand implements ClusterCommand {
    private final String taskId;
    private final String topic;
    private final DCallback callback;
    public TaskStatQueryCommand(String taskId, String topic, DCallback dCallback) {
        this.taskId = taskId;
        this.topic = topic;
        this.callback = dCallback;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTopic() {
        return topic;
    }

    public DCallback getCallback() {
        return callback;
    }
}
