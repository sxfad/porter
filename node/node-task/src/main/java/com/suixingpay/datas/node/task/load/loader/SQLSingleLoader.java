/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 16:02
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.load.loader;

import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.core.db.dialect.DbDialectFactory;
import com.suixingpay.datas.node.core.db.dialect.SqlTemplate;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 16:02
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月27日 16:02
 */
public class SQLSingleLoader extends BaseSqlLoader implements Loader {
    private final DbDialectFactory dbFactory = DbDialectFactory.INSTANCE;

    @Override
    public String getName() {
        return "single";
    }

    //需要在之前对datasource进行二次封装
    @Override
    public void load(ETLBucket bucket, TaskWork work) {
        LOGGER.debug("start loading bucket:{},size:{}", bucket.getSequence(), bucket.getRows().size());
        DbDialect dbDialect = dbFactory.getDbDialect(bucket.getDataSourceId());
        SqlTemplate template = dbDialect.getSqlTemplate();
        JdbcTemplate jdbcTemplate = dbDialect.getJdbcTemplate();
        for (ETLRow row : bucket.getRows()) {
            DTaskStat stat = work.getDTaskStat(row.getSchema(), row.getTable());
            //更新目标仓储
            int affect = loadSql(buildSql(row, template),jdbcTemplate);
            //更新状态
            updateStat(new ImmutablePair<>(affect, row), stat);
        }
    }
}