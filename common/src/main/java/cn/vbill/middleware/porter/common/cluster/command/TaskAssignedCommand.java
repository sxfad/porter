/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 18:42
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.common.cluster.command;

import lombok.Getter;

/**
 * 任务已分配通知 （和后台无关，服务器=》zk）
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 18:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 18:42
 */
public class TaskAssignedCommand implements ClusterCommand {
    @Getter private final String taskId;
    @Getter private final String swimlaneId;

    public TaskAssignedCommand(String taskId, String swimlaneId) {
        this.taskId = taskId;
        this.swimlaneId = swimlaneId;
    }
}
