/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 14:01
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.datacarrier;

import org.apache.commons.lang3.tuple.Pair;


/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @param <E>
 * @date: 2017年12月25日 14:01
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 14:01
 */

public interface DataCarrier<E> {
    void push(E item) throws InterruptedException;
    Pair<String, E> pullByOrder();
    <E> E pull();
    long size();
}
