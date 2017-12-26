package com.suixingpay.datas.node.datacarrier.simple;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 14:04
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.node.datacarrier.DataCarrier;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 14:04
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 14:04
 */
public class SortedDataCarrier implements DataCarrier {
    @Override
    public void push(List list) throws InterruptedException {

    }

    @Override
    public void push(Object item) throws InterruptedException {

    }

    @Override
    public Pair pull() {
        return null;
    }

    @Override
    public Pair<Long, List> greedyPull() {
        return null;
    }
}
