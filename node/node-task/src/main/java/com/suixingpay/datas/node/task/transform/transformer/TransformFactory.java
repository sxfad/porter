/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 19:23
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.transform.transformer;

import com.suixingpay.datas.common.db.TableMapper;
import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.core.db.dialect.DbDialectFactory;
import com.suixingpay.datas.node.core.event.ETLBucket;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 19:23
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 19:23
 */
@Component
@Scope("singleton")
public class TransformFactory {
    private List<Transformer> extractors = SpringFactoriesLoader.loadFactories(Transformer.class, null);
    public void transform(ETLBucket bucket, Map<String,TableMapper> tableMapper) {
        DbDialect targetDialect = DbDialectFactory.INSTANCE.getDbDialect(bucket.getDataSourceId());
        for (Transformer transformer : extractors) {
            transformer.transform(bucket, tableMapper, targetDialect);
        }
    }
}
