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

import cn.vbill.middleware.porter.common.db.meta.TableSchema;

import java.util.Date;

/**
 * 消费源源数据查询客户端
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 14:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 14:42
 */
public interface MetaQueryClient extends Client {

    /**
     * getTable
     *
     * @date 2018/8/10 下午2:58
     * @param: [schema, tableName]
     * @return: cn.vbill.middleware.porter.common.db.meta.TableSchema
     */
    TableSchema getTable(String schema, String tableName) throws Exception;

    /**
     * getDataCount
     *
     * @date 2018/8/10 下午2:58
     * @param: [schema, table, updateDateColumn, startTime, endTime]
     * @return: int
     */
    int getDataCount(String schema, String table, String updateDateColumn, Date startTime, Date endTime);
}
