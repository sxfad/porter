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

package cn.vbill.middleware.porter.common.cluster.data;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月02日 15:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月02日 15:24
 */
public interface DCallback {

    /**
     * callback
     * @param object
     */
    default void callback(DObject object) {
        return;
    }

    /**
     * callback
     * @param data
     */
    default void callback(String data) {
        return;
    }

    /**
     * callback
     * @param objects
     */
    default void callback(List<DObject> objects) {
        return;
    }
}
