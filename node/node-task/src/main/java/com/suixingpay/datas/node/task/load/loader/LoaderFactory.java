/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 15:56
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.load.loader;

import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
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
    private Loader loader;

    public LoaderFactory() {
        List<Loader> loaders = SpringFactoriesLoader.loadFactories(Loader.class, null);
        if (null != loaders && loaders.size() == 1) {
            loader = loaders.get(0);
        } else {
            throw new RuntimeException("LoaderFactory仅允许配置单个Loader实现");
        }
    }

    public void load(ETLBucket bucket, DTaskStat stat) {
        loader.load(bucket, stat);
    }
}
