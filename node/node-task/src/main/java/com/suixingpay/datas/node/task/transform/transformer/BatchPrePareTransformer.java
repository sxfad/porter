/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 13:38
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.transform.transformer;

import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 13:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月28日 13:38
 */
public class BatchPrePareTransformer implements Transformer{
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchPrePareTransformer.class);

    @Override
    public int order() {
        return 1;
    }

    @Override
    public void transform(ETLBucket bucket, TaskWork work, DbDialect targetDialect) {
        groupRows(bucket, 0);
    }

    private void groupRows(ETLBucket bucket, int from) {
        List<ETLRow> groupOne = new ArrayList<>();
        int size = bucket.getRows().size();
        while (from < size) {
            ETLRow row = bucket.getRows().get(from);
            groupOne.add(row);
            from++;
            ETLRow nextRow = null;
            if (from < size) nextRow = bucket.getRows().get(from);
            //下个操作类型和该类型相同
            if (null != nextRow && nextRow.getOpType() == row.getOpType() && nextRow.getSchema().equals(row.getSchema())
                    && nextRow.getTable().equals(row.getTable())) {
                continue;
            } else {
                break;
            }
        }
        if (! groupOne.isEmpty()) bucket.getBatchRows().add(groupOne);
        if (from < size) groupRows(bucket, from);
    }
}