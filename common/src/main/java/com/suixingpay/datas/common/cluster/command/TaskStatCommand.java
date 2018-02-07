/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 18:42
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.command;

import com.suixingpay.datas.common.cluster.data.DCallback;
import com.suixingpay.datas.common.cluster.data.DTaskStat;

/**
 *
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
