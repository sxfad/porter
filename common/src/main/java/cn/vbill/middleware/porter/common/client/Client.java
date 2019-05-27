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

import cn.vbill.middleware.porter.common.exception.ClientConnectionException;


/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:00
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 17:00
 */
public interface Client {

    /**
     * start
     *
     * @date 2018/8/10 下午2:52
     * @param: []
     * @return: void
     */
    void start() throws Exception;

    /**
     * shutdown
     *
     * @date 2018/8/10 下午2:52
     * @param: []
     * @return: void
     */
    void shutdown() throws Exception;

    /**
     * isStarted
     *
     * @date 2018/8/10 下午2:52
     * @param: []
     * @return: boolean
     */
    boolean isStarted() throws InterruptedException;

    /**
     * getConfig
     *
     * @date 2018/8/10 下午2:54
     * @param: []
     * @return: T
     */
    <T> T getConfig();

    /**
     * setPublic
     *
     * @date 2018/8/10 下午2:54
     * @param: [isPublic]
     * @return: void
     */
    void setPublic(boolean isPublic);

    default boolean isPublic() {
        return false;
    }

    /**
     * testConnection
     *
     * @date 2018/8/10 下午2:54
     * @param: []
     * @return: void
     */
    default void testConnection() throws ClientConnectionException {

    }

    default boolean isAlive() {
        return true;
    }

    /**
     * getClientInfo
     *
     * @date 2018/8/10 下午2:55
     * @param: []
     * @return: java.lang.String
     */
    String getClientInfo();
}
