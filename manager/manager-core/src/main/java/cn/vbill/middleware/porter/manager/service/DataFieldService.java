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

import cn.vbill.middleware.porter.manager.core.entity.DataField;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 数据字段对应表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataFieldService {

    /**
     * 新增
     *
     * @date 2018/8/10 上午11:06
     * @param: [dataField]
     * @return: java.lang.Integer
     */
    Integer insert(DataField dataField);

    /**
     * 修改
     *
     * @date 2018/8/10 上午11:06
     * @param: [id, dataField]
     * @return: java.lang.Integer
     */
    Integer update(Long id, DataField dataField);

    /**
     * 删除
     *
     * @date 2018/8/10 上午11:09
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据di查询
     *
     * @date 2018/8/10 上午11:09
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.DataField
     */
    DataField selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 上午11:09
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.DataField>
     */
    Page<DataField> page(Page<DataField> page);
}