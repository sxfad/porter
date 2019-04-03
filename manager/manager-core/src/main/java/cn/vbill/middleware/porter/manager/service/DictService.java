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

package cn.vbill.middleware.porter.manager.service;

import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public interface DictService {

    /**
     * dictAll
     *
     * @author FuZizheng
     * @date 2018/8/10 上午11:35
     * @param: []
     * @return: java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.Object>>
     */
    Map<String, Map<String, Object>> dictAll();

    /**
     * dictByType
     *
     * @author FuZizheng
     * @date 2018/8/10 上午11:36
     * @param: [type]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> dictByType(String type);

    /**
     * 获取任务权限操作字典
     *
     * @author MurasakiSeiFu
     * @date 2019-04-03 14:10
     * @param: []
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> dictControlType();
}
