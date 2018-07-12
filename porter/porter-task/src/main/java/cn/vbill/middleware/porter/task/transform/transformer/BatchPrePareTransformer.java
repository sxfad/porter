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

package cn.vbill.middleware.porter.task.transform.transformer;

import cn.vbill.middleware.porter.core.event.etl.ETLBucket;
import cn.vbill.middleware.porter.core.event.etl.ETLRow;
import cn.vbill.middleware.porter.task.worker.TaskWork;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 13:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月28日 13:38
 */
public class BatchPrePareTransformer implements Transformer {

    @Override
    public int order() {
        return 1;
    }

    @Override
    public void transform(ETLBucket bucket, TaskWork work) {
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
            if (null != nextRow && nextRow.getFinalOpType() == row.getFinalOpType() && nextRow.getFinalSchema().equals(row.getFinalSchema())
                    && nextRow.getFinalTable().equals(row.getFinalTable())) {
                continue;
            } else {
                break;
            }
        }
        if (!groupOne.isEmpty()) bucket.getBatchRows().add(groupOne);
        if (from < size) groupRows(bucket, from);
    }
}
