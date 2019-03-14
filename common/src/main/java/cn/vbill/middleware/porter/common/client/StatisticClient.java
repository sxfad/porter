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

/**
 * 统计信息上传客户端
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月19日 11:16
 * @version: V1.0
 * @review: zkevin/2018年10月19日 11:16
 */
public interface StatisticClient extends Client {
    default void uploadStatistic(String target, String key, String data) {

    }
}
