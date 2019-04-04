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

package cn.vbill.middleware.porter.common.statistics;

import com.alibaba.fastjson.JSON;

/**
 * 数据对象抽象
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 17:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 17:42
 */
public abstract class DObject {

    /**
     * DEFAULT_DATE_FORMAT
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * toString
     *
     * @return
     */
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * fromString
     *
     * @param string
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromString(String string, Class<T> clazz) {
        return JSON.parseObject(string, clazz);
    }

    /**
     * merge
     *
     * @param data
     * @param <T>
     */
    public abstract <T> void merge(T data);

    /**
     * snapshot
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T snapshot(Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(this), clazz);
    }
}
