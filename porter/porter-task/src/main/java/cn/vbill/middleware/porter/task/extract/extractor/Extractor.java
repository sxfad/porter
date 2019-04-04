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

package cn.vbill.middleware.porter.task.extract.extractor;

import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.task.extract.ExtractMetadata;

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

    /**
     * extract
     *
     * @date 2018/8/9 下午2:08
     * @param: [bucket, metadata]
     * @return: void
     */
    void extract(ETLBucket bucket, ExtractMetadata metadata);
}
