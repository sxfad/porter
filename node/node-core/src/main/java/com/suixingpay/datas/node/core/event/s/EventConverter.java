package com.suixingpay.datas.node.core.event.s;

import com.suixingpay.datas.node.core.event.s.converter.ConvertNotMatchException;

import java.text.ParseException;

/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 11:45
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

public interface EventConverter {
    String getName();
    <T> MessageEvent convert(T selectObj) throws ParseException, ConvertNotMatchException;
}
