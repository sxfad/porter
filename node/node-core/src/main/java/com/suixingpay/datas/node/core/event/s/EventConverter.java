/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.event.s;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;

public interface EventConverter {
    String getName();
    <T> MessageEvent convert(JSONObject head, JSONObject value) throws ParseException;
}
