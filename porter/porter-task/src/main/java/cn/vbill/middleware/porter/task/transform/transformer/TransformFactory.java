/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.task.transform.transformer;

import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.task.worker.TaskWork;
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
    private final List<Transformer> extractors = SpringFactoriesLoader.loadFactories(Transformer.class, JavaFileCompiler.getInstance());
    private final AtomicBoolean isSort = new AtomicBoolean(false);

    public TransformFactory() {
        //服务排序
        sort();
    }

    /**
     * transform
     *
     * @date 2018/8/9 下午2:13
     * @param: [bucket, work]
     * @return: void
     */
    public void transform(ETLBucket bucket, TaskWork work) throws Exception {
        for (Transformer transformer : extractors) {
            transformer.transform(bucket, work);
        }
    }

    /**
     * sort
     *
     * @date 2018/8/9 下午2:13
     * @param: []
     * @return: void
     */
    private void sort() {
        if (isSort.compareAndSet(false, true)) {
            extractors.sort(Comparator.comparingInt(Transformer::order));
        }
    }
}
