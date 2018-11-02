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
 * @param <S>
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 14:08
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 14:08
 */
public interface ClusterClient<S> {

    /**
     * getChildren
     *
     * @date 2018/8/10 下午2:56
     * @param: [path]
     * @return: java.util.List<java.lang.String>
     */
    List<String> getChildren(String path);

    /**
     * getData
     *
     * @date 2018/8/10 下午2:56
     * @param: [path]
     * @return: org.apache.commons.lang3.tuple.Pair<java.lang.String,S>
     */
    Pair<String, S> getData(String path);

    /**
     * create
     *
     * @date 2018/8/10 下午2:56
     * @param: [path, isTemp, data]
     * @return: java.lang.String
     */
    String create(String path, boolean isTemp, String data) throws Exception;

    /**
     * setData
     *
     * @date 2018/8/10 下午2:56
     * @param: [path, data, version]
     * @return: S
     */
    S setData(String path, String data, int version) throws Exception;

    /**
     * exists
     *
     * @date 2018/8/10 下午2:57
     * @param: [path, watch]
     * @return: S
     */
    S exists(String path, boolean watch) throws Exception;

    /**
     * delete
     *
     * @author FuZizheng
     * @date 2018/8/10 下午2:57
     * @param: [path]
     * @return: void
     */
    void delete(String path) throws Exception;

    /**
     * alive
     *
     * @date 2018/8/10 下午2:57
     * @param: []
     * @return: boolean
     */
    boolean alive();

    /**
     * clientSpinning
     *
     * @date 2018/8/10 下午2:57
     * @param: []
     * @return: void
     */
    default void clientSpinning() {

    }

    void setStatisticClient(StatisticClient client) throws Exception;
}
