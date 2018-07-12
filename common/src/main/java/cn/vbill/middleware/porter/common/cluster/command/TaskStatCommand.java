/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 18:42
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.common.cluster.command;

import cn.vbill.middleware.porter.common.cluster.data.DTaskStat;
import cn.vbill.middleware.porter.common.cluster.data.DCallback;

/**
 * 任务状态上报（服务器上报zk）
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 18:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 18:42
 */
public class TaskStatCommand implements ClusterCommand {
    private final DTaskStat stat;
    private final DCallback callback;
    public TaskStatCommand(DTaskStat stat, DCallback callback) {
        this.stat = stat;
        this.callback = callback;
    }

    public DTaskStat getStat() {
        return stat;
    }

    public DCallback getCallback() {
        return callback;
    }
}
