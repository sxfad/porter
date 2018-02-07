/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.plugin.loader.jdbc;

import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
import com.suixingpay.datas.node.core.util.CallbackMethodCreator;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:38
 */
public class JdbcSingleLoader extends BaseJdbcLoader {
    private static final String LOADER_NAME = "JdbcSingle";

    @Override
    protected String getName() {
        return LOADER_NAME;
    }

    @Override
    public void load(ETLBucket bucket, CallbackMethodCreator getter) {
        LOGGER.info("start loading bucket:{},size:{}", bucket.getSequence(), bucket.getRows().size());
        for (ETLRow row : bucket.getRows()) {
            DTaskStat stat = getter.invokeWithResult(row.getSchema(), row.getTable());
            //更新目标仓储
            int affect = loadSql(buildSql(row));
            //更新状态
            updateStat(new ImmutablePair<>(affect, row), stat);
        }
    }
}
