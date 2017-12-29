/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 10:59
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.extract.extractor;

import com.suixingpay.datas.common.db.TableMapper;
import com.suixingpay.datas.node.core.event.ETLBucket;
import com.suixingpay.datas.node.core.event.ETLRow;
import com.suixingpay.datas.node.core.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 包含所有的忽略规则
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 10:59
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月27日 10:59
 */
public class IgnoreRowExtractor implements Extractor{
    private  static final Logger LOGGER = LoggerFactory.getLogger(IgnoreRowExtractor.class);

    @Override
    public void extract(ETLBucket bucket) {
        long initBucketSize = bucket.getRows().size();
        List<ETLRow> removals = new ArrayList<>();
        for (ETLRow row : bucket.getRows()) {
            //当前仅支持插入、更新、删除、截断表
            if (row.getOpType() == EventType.INSERT || row.getOpType() == EventType.UPDATE || row.getOpType() == EventType.DELETE) {
                //插入、删除、更新字段为空
                if ((null == row.getColumns() || row.getColumns().isEmpty()) && row.getOpType() != EventType.TRUNCATE) {
                    removals.add(row);
                }
            } else {
                removals.add(row);
            }
        }
        bucket.getRows().removeAll(removals);
        LOGGER.info("sequence:{},before bucketSize:{},after bucketSize:{}", bucket.getSequence(), initBucketSize, bucket.getRows().size());
    }
}
