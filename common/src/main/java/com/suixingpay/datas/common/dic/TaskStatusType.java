/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 10:32
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务状态
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 10:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 10:32
 */

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskStatusType {

    NEW("NEW", "新建"), STOPPED("STOPPED", "已停止"), WORKING("WORKING", "工作中");

    @Getter private final String code;
    @Getter private final String name;

    public static final List<TaskStatusType> STATUSES = new ArrayList<TaskStatusType>() {
        {
            add(NEW);
            add(WORKING);
            add(STOPPED);
        }
    };

    public boolean isStopped() {
        return this == STOPPED;
    }
    public boolean isWorking() {
        return this == WORKING;
    }
}
