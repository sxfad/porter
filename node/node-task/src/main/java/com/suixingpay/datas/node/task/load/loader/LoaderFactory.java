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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 15:56
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月27日 15:56
 */
@Component
@Scope("singleton")
public class LoaderFactory {
    private final Map<String, Loader> LOADERS = new LinkedHashMap<>();

    public LoaderFactory() {
        //获取loader实现列表
        List<Loader> loaderList = SpringFactoriesLoader.loadFactories(Loader.class, null);
        //排序
        loaderList.sort(new Comparator<Loader>() {
            @Override
            public int compare(Loader o1, Loader o2) {
                return o1.order() - o2.order();
            }
        });
        //填充
        for (Loader loader : loaderList) {
            LOADERS.put(loader.getName(), loader);
        }
    }

    private Loader getLoader(String name) {
        Loader loader = LOADERS.get(name);
        loader = null == loader ? LOADERS.values().stream().findFirst().get() : loader;
        return loader;
    }

    public void load(ETLBucket bucket, TaskWork work) {
        getLoader(work.getLoader()).load(bucket, work);
    }
}
