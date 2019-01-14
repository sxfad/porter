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

import cn.vbill.middleware.porter.core.event.s.EventType;
import cn.vbill.middleware.porter.common.consumer.Position;
import cn.vbill.middleware.porter.core.event.etl.ETLBucket;
import cn.vbill.middleware.porter.core.event.etl.ETLRow;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月09日 17:52
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月09日 17:52
 */
@RunWith(JUnit4.class)
public class BatchPrePareTransformerTest {

    @Test
    public void transform() {
        List<ETLRow> rows = new ArrayList<>();
        rows.add(new ETLRow(0, 0, "s", "t", EventType.DELETE, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.DELETE, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.INSERT, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.DELETE, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.INSERT, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.INSERT, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.INSERT, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.TRUNCATE, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.TRUNCATE, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.UPDATE, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.TRUNCATE, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.INSERT, null, null, null));
        rows.add(new ETLRow(0, 0, "s", "t", EventType.INSERT, null, null, null));

        ETLBucket bucket = new ETLBucket("0", rows, new Position() {
            @Override
            public boolean checksum() {
                return true;
            }
        });

        BatchPrePareTransformer t = new BatchPrePareTransformer();
        t.transform(bucket, null);

        Assert.assertEquals(bucket.getBatchRows().size(), 8);
    }
}
