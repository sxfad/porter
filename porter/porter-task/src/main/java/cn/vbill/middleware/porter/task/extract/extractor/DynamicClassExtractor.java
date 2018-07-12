/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 10:28
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.task.extract.extractor;

import cn.vbill.middleware.porter.core.event.etl.ETLBucket;
import cn.vbill.middleware.porter.task.extract.ExtractMetadata;

/**
 * 动态类Extractor
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 10:28
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月09日 10:28
 */
public class DynamicClassExtractor implements Extractor {

    @Override
    public void extract(ETLBucket bucket, ExtractMetadata metadata) {
        if (null != metadata && null != metadata.getProcessor()) {
            metadata.getProcessor().process(bucket);
        }
    }

    @Override
    public int order() {
        return 1;
    }
}
