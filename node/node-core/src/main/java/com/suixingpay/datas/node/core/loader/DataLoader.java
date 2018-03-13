/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 16:23
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.loader;

import com.suixingpay.datas.common.client.LoadClient;
import com.suixingpay.datas.common.client.MetaQueryClient;
import com.suixingpay.datas.common.db.meta.TableSchema;
import com.suixingpay.datas.common.exception.TaskDataException;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
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
    Pair<Boolean, List<SubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException;

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


}
