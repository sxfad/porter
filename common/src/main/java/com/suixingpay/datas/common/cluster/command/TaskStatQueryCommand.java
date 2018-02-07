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
 * 任务状态查询
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月12日 18:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月12日 18:46
 */
public class TaskStatQueryCommand implements ClusterCommand {
    @Getter private final String taskId;
    @Getter private final String resourceId;
    @Getter private final DCallback callback;

    public TaskStatQueryCommand(String taskId, String resourceId, DCallback dCallback) {
        this.taskId = taskId;
        this.resourceId = resourceId;
        this.callback = dCallback;
    }
}
