/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月02日 09:52
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.cluster.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务因失败停止，后台接收到节点变更信息 （管理后台只需要接受失败通知）
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月02日 09:52
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月02日 09:52
 */
@AllArgsConstructor
public class TaskStoppedByErrorCommand implements ClusterCommand {
    //任务id
    @Getter private String taskId;
    //泳道id
    @Getter private String swimlaneId;
}
