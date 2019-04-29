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
package cn.vbill.middleware.porter.plugin.loader.jdbc.loader;

import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.core.task.setl.ETLColumn;
import cn.vbill.middleware.porter.core.task.setl.ETLRow;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

/**
 * jdbc多线程并发Load
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:38
 */
@RunWith(JUnit4.class)
public class JdbcMultiThreadLoaderTest {

    @Test
    public void sort() {
        List<ETLRow> rows = new ArrayList<>();
        rows.add(new ETLRow(0, 0, "s", "t", MessageAction.INSERT,
                Arrays.asList(new ETLColumn("id", "1", "", "1", true),
                        new ETLColumn("name", "zkevin", "", "zkevin", true)
                ), null, null));
        rows.add(new ETLRow(0, 0, "s", "t", MessageAction.UPDATE,
                Arrays.asList(new ETLColumn("id", "1", "1", "1", true),
                        new ETLColumn("name", "zkevin00", "zkevin", "zkevin00", true)), null, null));

        rows.add(new ETLRow(0, 0, "s", "t", MessageAction.INSERT,
                Arrays.asList(new ETLColumn("id", "2", "", "2", true),
                        new ETLColumn("name", "zkevin1", "", "zkevin1", true)
                ), null, null));

        rows.add(new ETLRow(0, 0, "s", "t", MessageAction.UPDATE,
                Arrays.asList(new ETLColumn("id", "2", "2", "2", true),
                        new ETLColumn("name", "zkevin11", "zkevin1", "zkevin11", true)
                ), null, null));

        rows.add(new ETLRow(0, 0, "s", "t", MessageAction.INSERT,
                Arrays.asList(new ETLColumn("id", "3", "", "3", true),
                        new ETLColumn("name", "zkevin", "", "zkevin", true)
                ), null, null));
        rows.add(new ETLRow(0, 0, "s", "t", MessageAction.INSERT,
                Arrays.asList(new ETLColumn("id", "4", "", "4", true),
                        new ETLColumn("name", "zkevin", "", "zkevin", true)
                ), null, null));


        rows.add(new ETLRow(0, 0, "s", "t", MessageAction.INSERT,
                Arrays.asList(new ETLColumn("id", "5", "", "5", true),
                        new ETLColumn("name", "zkevin", "", "zkevin", true)
                ), null, null));
        rows.add(new ETLRow(0, 0, "s", "t", MessageAction.INSERT,
                Arrays.asList(new ETLColumn("id", "6", "", "6", true),
                        new ETLColumn("name", "zkevin", "", "zkevin", true)
                ), null, null));

        ETLBucket bucket = new ETLBucket("0", rows, null);
        new JdbcMultiThreadLoader().sort(bucket);
        System.out.println(bucket.getParallelRows().size() + "---------");
        Assert.assertEquals(bucket.getParallelRows().stream().flatMapToLong(r -> LongStream.of(r.size())).sum(), rows.size());
    }
}
