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

import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.plugin.loader.jdbc.JdbcLoaderConst;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.core.event.etl.ETLBucket;
import cn.vbill.middleware.porter.core.event.etl.ETLRow;
import cn.vbill.middleware.porter.core.loader.SubmitStatObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:38
 */
public class JdbcPrintSqlLoader extends BaseJdbcLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcPrintSqlLoader.class);

    @Override
    protected String getPluginName() {
        return JdbcLoaderConst.LOADER_PLUGIN_JDBC_SQL_PRINT.getCode();
    }

    @Override
    public Pair<Boolean, List<SubmitStatObject>> doLoad(ETLBucket bucket) throws TaskStopTriggerException {
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
        if (null != bucket && null != bucket.getRows() && !bucket.getRows().isEmpty()) {
            printTimeTaken(bucket.getRows().get(0));
        }
        return new ImmutablePair(Boolean.TRUE, affectRow);
    }
}
