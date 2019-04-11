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

/**
 * map carrier
 *
 * @param <K>
 * @param <V>
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年05月02日 10:13
 * @version: V1.0
 * @review: zkevin/2018年05月02日 10:13
 */
public interface DataMapCarrier<K, V> extends DataContainer {

    /**
     * push
     *
     * @date 2018/8/9 上午11:58
     * @param: [key, v]
     * @return: boolean
     */
    boolean push(K key, V v) throws InterruptedException;

    /**
     * pull
     *
     * @date 2018/8/9 上午11:58
     * @param: [key]
     * @return: V
     */
    V pull(K key) throws InterruptedException;

    /**
     * containsKey
     *
     * @date 2018/8/9 上午11:58
     * @param: [key]
     * @return: boolean
     */
    boolean containsKey(K key);

    /**
     * printState
     *
     * @date 2018/8/9 上午11:58
     * @param: []
     * @return: void
     */
    void printState();

    /**
     * print
     *
     * @date 2018/8/9 上午11:58
     * @param: []
     * @return: void
     */
    void print();
}
