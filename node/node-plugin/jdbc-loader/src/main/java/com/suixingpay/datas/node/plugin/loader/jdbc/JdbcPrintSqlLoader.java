/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.plugin.loader.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.dic.LoaderPlugin;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
import com.suixingpay.datas.node.core.loader.SubmitStatObject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:38
 */
public class JdbcPrintSqlLoader extends BaseJdbcLoader {

    @Override
    protected String getPluginName() {
        return LoaderPlugin.JDBC_SQL_PRINT.getCode();
    }

    @Override
    public Pair<Boolean, List<SubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException {
        LOGGER.info("start loading bucket:{},size:{}", bucket.getSequence(), bucket.getRows().size());
        List<SubmitStatObject> affectRow = new ArrayList<>();
        for (ETLRow row : bucket.getRows()) {
            LOGGER.info(JSONObject.toJSONString(row));
            //更新目标仓储
            List<Pair<String, Object[]>> sqlList = buildSql(row);
            sqlList.forEach(p -> {
                LOGGER.info(p.getLeft());
            });
            affectRow.add(new SubmitStatObject(row.getFinalSchema(), row.getFinalTable(), row.getFinalOpType(),
                    1, row.getPosition(), row.getOpTime()));
        }
        return new ImmutablePair(Boolean.TRUE, affectRow);
    }
}
