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
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年05月02日 10:13
 * @version: V1.0
 * @review: zkevin/2018年05月02日 10:13
 */

public interface DataMapCarrier<K, V> extends DataContainer {
    boolean push(K key, V v) throws InterruptedException;
    V pull(K key);
    boolean containsKey(K key);
    void printState();
    void print();
}
