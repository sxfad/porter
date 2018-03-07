/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 11:42
 */

@AllArgsConstructor
public enum AlertPlugin {

    EMAIL("EMAIL", "邮件");

    @Getter private final String code;
    @Getter private final String name;

    public static final List<AlertPlugin> PLUGINS = new ArrayList<AlertPlugin>() {
        {
            add(EMAIL);
        }
    };

    public String toString() {
        JSONObject object = new JSONObject();
        object.put("code", code);
        object.put("name", name);
        return object.toJSONString();
    }

    public static void main(String[] args) {
        System.out.println(JSONObject.toJSONString(PLUGINS, SerializerFeature.WriteEnumUsingToString));
    }
}
