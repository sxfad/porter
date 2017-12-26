package com.suixingpay.datas.node.task.extract.extractor;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 14:26
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.node.core.event.ETLBucket;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 14:26
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月26日 14:26
 */
public class PrintETLDataExtractor implements  Extractor {
    @Override
    public void extract(ETLBucket bucket) {
        System.out.println("序列号:" + bucket.getSequence() + ", 大小:" + bucket.getRows().size());
    }
}
