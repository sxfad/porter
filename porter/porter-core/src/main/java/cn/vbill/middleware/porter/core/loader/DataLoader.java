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

package cn.vbill.middleware.porter.core.loader;

import cn.vbill.middleware.porter.core.event.etl.ETLBucket;
import cn.vbill.middleware.porter.common.client.LoadClient;
import cn.vbill.middleware.porter.common.client.MetaQueryClient;
import cn.vbill.middleware.porter.common.db.meta.TableSchema;
import cn.vbill.middleware.porter.common.exception.TaskDataException;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.event.etl.ETLRow;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 16:23
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月03日 16:23
 */
public interface DataLoader {

    /**
     * 用于Loader模块儿加载匹配
     * @param loaderName
     * @return
     */
    boolean isMatch(String loaderName);

    /**
     * 资源控制接口方法
     * @throws InterruptedException
     */
    void shutdown() throws Exception;

    /**
     * 资源控制接口方法
     * @throws Exception
     */
    void startup() throws Exception;

    /**
     * 资源控制接口方法
     */
    default boolean canStart() {
        return true;
    }


    /**
     * load数据接口,
     * @param bucket
     * @return key : true 会提交同步点， false不会提交同步点 ; value : 影响行数
     */
    Pair<Boolean, List<SubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException, InterruptedException;

    void setLoadClient(LoadClient c);
    void setMetaQueryClient(MetaQueryClient c);

    /**
     * 告警模块调用,用于查询某个时间段数据变化数量
     * @param schema
     * @param table
     * @param updateDateColumn
     * @param startTime
     * @param endTime
     * @return
     */
    int getDataCount(String schema, String table, String updateDateColumn, Date startTime, Date endTime);

    /**
     * 在transform阶段调用,用于映射到目标端表结构
     * @param finalSchema
     * @param finalTable
     * @return
     */
    TableSchema findTable(String finalSchema, String finalTable) throws Exception;

    /**
     * 在transform阶段调用,用于自定义处理数据行
     * @param row
     */
    void mouldRow(ETLRow row) throws TaskDataException;

    boolean isInsertOnUpdateError();

    void setInsertOnUpdateError(boolean insertOnUpdateError);

    String getClientInfo();
}
