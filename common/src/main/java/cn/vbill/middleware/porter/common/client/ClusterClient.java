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

package cn.vbill.middleware.porter.common.client;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @param <S>
 * @date: 2018年02月07日 14:08
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 14:08
 */
public interface ClusterClient<S> {

    List<String> getChildren(String path);

    Pair<String, S> getData(String path);

    String  create(String path, boolean isTemp, String data)  throws Exception;

    S setData(String path, String data, int version)  throws Exception;

    S exists(String path, boolean watch)  throws Exception;

    void delete(String path) throws Exception;

    boolean alive();

    default void clientSpinning() {

    }
}
