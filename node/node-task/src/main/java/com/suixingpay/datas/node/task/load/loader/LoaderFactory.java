/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 15:56
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.load.loader;

import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.s.EventConverter;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 15:56
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月27日 15:56
 */
@Component
@Scope("singleton")
public class LoaderFactory {
    private final List<Loader> LOADERS;

    public LoaderFactory() {
        LOADERS = SpringFactoriesLoader.loadFactories(Loader.class, null);
    }
    private Loader getLoader(String name) {
        for(Loader loader : LOADERS) {
            if (loader.getName().equals(name)) {
                return loader;
            }
        }
        return LOADERS.size() > 0 ? LOADERS.get(0) : null;
    }

    public void load(ETLBucket bucket, TaskWork work) {
        Loader loader = getLoader(work.getLoader());
        loader.load(bucket, work);
    }
}
