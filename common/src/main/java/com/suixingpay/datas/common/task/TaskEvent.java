/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 17:19
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.task;

import com.suixingpay.datas.common.config.TaskConfig;
import lombok.Getter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 17:19
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 17:19
 */
public class TaskEvent {
    @Getter private final TaskConfig config;
    @Getter private final TaskEventType type;

    public TaskEvent(TaskConfig config, TaskEventType type) {
        this.config = config;
        this.type = type;
    }


    public TaskEventType getType() {
        return type;
    }
}
