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

package cn.vbill.middleware.porter.datacarrier;

import org.apache.commons.lang3.tuple.Pair;


/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @param <E>
 * @date: 2017年12月25日 14:01
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 14:01
 */

public interface DataCarrier<E>  extends DataContainer {

    /**
     * push
     *
     * @date 2018/8/9 上午11:56
     * @param: [item]
     * @return: void
     */
    void push(E item) throws InterruptedException;

    /**
     * pullByOrder
     *
     * @date 2018/8/9 上午11:56
     * @param: []
     * @return: org.apache.commons.lang3.tuple.Pair<java.lang.String,E>
     */
    Pair<String, E> pullByOrder();

    /**
     * pull
     *
     * @date 2018/8/9 上午11:56
     * @param: []
     * @return: E
     */
    <E> E pull();
}
