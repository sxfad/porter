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

package cn.vbill.middleware.porter.task.extract.extractor;

import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.core.task.setl.ETLRow;
import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.task.extract.ExtractMetadata;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 包含所有的忽略规则
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 10:59
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月27日 10:59
 */
public class IgnoreRowExtractor implements Extractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoreRowExtractor.class);

    @Override
    public void extract(ETLBucket bucket, ExtractMetadata metadata) {
        long initBucketSize = bucket.getRows().size();
        List<ETLRow> removals = new ArrayList<>();
        for (ETLRow row : bucket.getRows()) {
            LOGGER.debug("trying extract row:{}", JSON.toJSONString(row));

            //包含、不包含表判断
            if (!metadata.getIncludeTables().isEmpty()) {
                String strSeg = new StringBuffer().append(row.getFinalSchema()).append(".")
                        .append(row.getFinalTable()).toString().intern();

                if (!metadata.getIncludeTables().contains(strSeg)) {
                    removals.add(row);
                }

            } else if (metadata.getIncludeTables().isEmpty() && !metadata.getExcludeTables().isEmpty()) { //不包含表
                String strSeg = new StringBuffer().append(row.getFinalSchema()).append(".")
                        .append(row.getFinalTable()).toString().intern();

                if (metadata.getExcludeTables().contains(strSeg)) {
                    removals.add(row);
                }
            }

            //当前仅支持插入、更新、删除、截断表
            if (row.getFinalOpType() == MessageAction.INSERT || row.getFinalOpType() == MessageAction.UPDATE
                    || row.getFinalOpType() == MessageAction.DELETE || row.getFinalOpType() == MessageAction.TRUNCATE) {
                //插入、删除、更新字段为空
                if ((null == row.getColumns() || row.getColumns().isEmpty()) && row.getFinalOpType() != MessageAction.TRUNCATE) {
                    LOGGER.debug("removing row:{}", JSON.toJSONString(row));
                    removals.add(row);
                }
            } else {
                LOGGER.debug("removing row:{}", JSON.toJSONString(row));
                removals.add(row);
            }
        }
        bucket.getRows().removeAll(removals);
        LOGGER.debug("sequence:{},before bucketSize:{},after bucketSize:{}", bucket.getSequence(), initBucketSize, bucket.getRows().size());
    }
}
