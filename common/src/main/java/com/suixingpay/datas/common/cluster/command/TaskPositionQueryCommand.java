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
import lombok.Getter;

/**
 * 任务消费进度查询（和管理后台无关  服务器上报zk）
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月12日 18:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月12日 18:46
 */
public class TaskPositionQueryCommand implements ClusterCommand {
    @Getter private final String taskId;
    @Getter private final String swimlaneId;
    @Getter private final DCallback callback;

    public TaskPositionQueryCommand(String taskId, String swimlaneId, DCallback dCallback) {
        this.taskId = taskId;
        this.swimlaneId = swimlaneId;
        this.callback = dCallback;
    }
}
