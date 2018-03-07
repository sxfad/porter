/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 10:32
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点状态
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 10:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 10:32
 */

@AllArgsConstructor
public enum  NodeStatusType {

    WORKING("WORKING", "工作中"), SUSPEND("SUSPEND", "已暂停");

    @Getter private final String code;
    @Getter private final String name;

    public static final List<NodeStatusType> STATUSES = new ArrayList<NodeStatusType>() {
        {
            add(WORKING);
            add(SUSPEND);
        }
    };

    public boolean isSuspend() {
        return this == SUSPEND;
    }

    public boolean isWorking() {
        return this == WORKING;
    }

    public String toString() {
        JSONObject object = new JSONObject();
        object.put("code", code);
        object.put("name", name);
        return object.toJSONString();
    }

}
