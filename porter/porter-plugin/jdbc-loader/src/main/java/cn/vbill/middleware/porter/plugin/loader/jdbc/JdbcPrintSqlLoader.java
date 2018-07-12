/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.plugin.loader.jdbc;

import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.common.dic.LoaderPlugin;
import cn.vbill.middleware.porter.core.event.etl.ETLBucket;
import cn.vbill.middleware.porter.core.event.etl.ETLRow;
import cn.vbill.middleware.porter.core.loader.SubmitStatObject;
import org.apache.commons.lang3.StringUtils;
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
                LOGGER.info("sql:{}-{}", p.getLeft(), StringUtils.join(p.getRight(), ","));
            });
            affectRow.add(new SubmitStatObject(row.getFinalSchema(), row.getFinalTable(), row.getFinalOpType(),
                    1, row.getPosition(), row.getOpTime()));
        }
        if (null != bucket && null != bucket.getRows() && !bucket.getRows().isEmpty()) printTimeTaken(bucket.getRows().get(0));
        return new ImmutablePair(Boolean.TRUE, affectRow);
    }
}
