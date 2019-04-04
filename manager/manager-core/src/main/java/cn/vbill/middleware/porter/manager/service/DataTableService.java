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

import cn.vbill.middleware.porter.manager.core.entity.DataTable;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 数据表信息表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataTableService {

    /**
     * 新增
     *
     * @date 2018/8/10 上午11:16
     * @param: [dataTable]
     * @return: java.lang.Integer
     */
    Integer insert(DataTable dataTable);

    /**
     * 删除
     *
     * @date 2018/8/10 上午11:18
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 上午11:18
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.event.DataTable
     */
    DataTable selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 上午11:19
     * @param: [page, bankName, beginTime, endTime]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.event.DataTable>
     */
    Page<DataTable> page(Page<DataTable> page, String bankName, String beginTime, String endTime);

    /**
     * prefixList
     *
     * @date 2018/8/10 上午11:19
     * @param: [sourceId]
     * @return: java.util.List<java.lang.String>
     */
    List<String> prefixList(Long sourceId);

    /**
     * tableList
     *
     * @date 2018/8/10 上午11:24
     * @param: [page, sourceId, prefix, tableName]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<java.lang.Object>
     */
    Page<Object> tableList(Page<Object> page, Long sourceId, String prefix, String tableName);

    /**
     * dataTableList
     *
     * @date 2018/8/10 上午11:24
     * @param: [dataTablePage]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.event.DataTable>
     */
    Page<DataTable> dataTableList(Page<DataTable> dataTablePage);
}