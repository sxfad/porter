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

import cn.vbill.middleware.porter.manager.core.entity.Dictionary;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.Map;

/**
 * 数据字典表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DictionaryService {

    /**
     * 新增
     *
     * @date 2018/8/10 上午11:37
     * @param: [dictionary]
     * @return: java.lang.Integer
     */
    Integer insert(Dictionary dictionary);

    /**
     * 更新
     *
     * @date 2018/8/10 上午11:37
     * @param: [id, dictionary]
     * @return: java.lang.Integer
     */
    Integer update(Long id, Dictionary dictionary);

    /**
     * 删除
     *
     * @date 2018/8/10 上午11:37
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查找
     *
     * @date 2018/8/10 上午11:37
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.Dictionary
     */
    Dictionary selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 上午11:37
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.Dictionary>
     */
    Page<Dictionary> page(Page<Dictionary> page);

    /**
     * selectMap
     *
     * @date 2018/8/10 上午11:37
     * @param: []
     * @return: java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.lang.Object>>
     */
    Map<String, Map<String, Object>> selectMap();
}
