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
import lombok.Setter;

/**
 * 任务停止指令 （通知zk使用）
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 18:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 18:42
 */
public class TaskStopCommand implements ClusterCommand {
    @Getter @Setter private String taskId;
    @Getter @Setter private String swimlaneId;

    public TaskStopCommand(String taskId, String swimlaneId) {
        this.taskId = taskId;
        this.swimlaneId = swimlaneId;
    }
}
