/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 19:23
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.transform.transformer;

import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final AtomicBoolean isSort = new AtomicBoolean(false);

    public TransformFactory() {
        //服务排序
        sort();
    }

    public void transform(ETLBucket bucket, TaskWork work) throws Exception {
        for (Transformer transformer : extractors) {
            transformer.transform(bucket, work);
        }
    }

    private void sort() {
        if (isSort.compareAndSet(false, true)) {
            extractors.sort((o1, o2) -> o1.order() - o2.order());
        }
    }
}
