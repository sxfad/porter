/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 19:18
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.extract.extractor;

import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.task.extract.ExtractMetadata;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 19:18
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 19:18
 */
public interface Extractor {
    /**
     * 执行顺序，数字越小越早执行
     * @return
     */
    default int order() {
        return 0;
    }

    void extract(ETLBucket bucket, ExtractMetadata metadata);
}
