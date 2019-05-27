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

package cn.vbill.middleware.porter.plugin.connector.jdbc.client;

import cn.vbill.middleware.porter.common.task.consumer.ConsumeClient;
import cn.vbill.middleware.porter.common.task.consumer.Position;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.util.DefaultNamedThreadFactory;
import cn.vbill.middleware.porter.common.util.db.SqlTimestampConverter;
import cn.vbill.middleware.porter.plugin.connector.jdbc.config.JdbcConsumerConfig;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 */
public class JdbcConsumeClient  extends JdbcClient implements ConsumeClient {
    private static final ConvertUtilsBean TIME_TO_TIMESTAMP = new ConvertUtilsBean();
    static {
        TIME_TO_TIMESTAMP.register(SqlTimestampConverter.SQL_TIMESTAMP, java.sql.Date.class);
        TIME_TO_TIMESTAMP.register(SqlTimestampConverter.SQL_TIMESTAMP, java.sql.Time.class);
        TIME_TO_TIMESTAMP.register(SqlTimestampConverter.SQL_TIMESTAMP, java.sql.Timestamp.class);
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConsumeClient.class);
    private final CountDownLatch canFetch = new CountDownLatch(1);
    private final List<JdbcPosition> positions = new CopyOnWriteArrayList<>();
    private final int fetchPoolSize;
    private volatile ExecutorService fetchPool;
    private final Map<String, JdbcConsumerConfig.TableFetchConfig> fetchMeta;
    public JdbcConsumeClient(JdbcConsumerConfig config) {
        super(config);
        fetchPoolSize = config.getTasksMax();
        fetchMeta = config.getTables();

    }

    @Override
    protected void doShutdown() throws SQLException {
        fetchPool.shutdownNow();
        super.doShutdown();
    }

    @Override
    public boolean isAutoCommitPosition() {
        return false;
    }

    @Override
    public long commitPosition(Position position) {
        return 0;
    }


    @Override
    public void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException {
        positions.addAll(initiateConsumePosition(position));
        fetchPool = new ThreadPoolExecutor(1, fetchPoolSize, 1L, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(), new DefaultNamedThreadFactory("JdbcConsumeClient-fetch-pool"), new ThreadPoolExecutor.CallerRunsPolicy());
        canFetch.countDown();
    }

    @Override
    public String getSwimlaneId() {
        return getConfig().getSwimlaneId();
    }

    @Override
    public <F, O> List<F> fetch(FetchCallback<F, O> callback) throws TaskStopTriggerException, InterruptedException {
        List<F> msgs = new ArrayList<>();
        if (isStarted()) {
            List<RowInfo> naiveRows = new ArrayList<>();
            List<Pair<JdbcPosition, Future<List<RowInfo>>>> futures = new ArrayList<>(positions.size());
            //并行查询
            for (JdbcPosition p : positions) {
                Future<List<RowInfo>> future = fetchPool.submit(() -> {
                    Pair<String, List<Object>> sql = p.buildSql(fetchMeta.get(p.table));
                    LOGGER.debug("sql:{}, params:{}", sql.getLeft(), StringUtils.join(sql.getValue(), ","));
                    return multiValueQuery(sql.getLeft(), sql.getValue().toArray());
                });
                futures.add(new ImmutablePair<>(p, future));
            }
            //更新position
            for (Pair<JdbcPosition, Future<List<RowInfo>>> future : futures) {
                try {
                    List<RowInfo> results = future.getValue().get();
                    JdbcPosition position = future.getKey();
                    if (!results.isEmpty()) {
                        //汇总行
                        naiveRows.addAll(results);

                        //更新position

                        RowInfo row = results.get(results.size() - 1);
                        row.getColumns().forEach(c -> {
                            if (c.getColumnName().equalsIgnoreCase(position.incrementColumn)) {
                                position.changeIncrementColumnValue((Long) c.getValue());
                            }
                            if (c.getColumnName().equalsIgnoreCase(position.timestampColumn)) {
                                try {
                                    position.changeTimestampValue((Long) c.getValue());
                                } catch (Throwable e) {
                                    position.changeTimestampValue((Long) TIME_TO_TIMESTAMP.convert(c.getValue(), Long.class));
                                }
                            }
                        });
                        position.clearQueryTimes();
                    } else {
                        position.increaseQueryTimes();
                    }
                } catch (ExecutionException e) {
                    throw new TaskStopTriggerException(e);
                }
            }
            if (!naiveRows.isEmpty()) {
                msgs.addAll(callback.acceptAll(new ImmutablePair(JdbcPositions.snapshot(positions), naiveRows)));
            }
        }
        return msgs;
    }


    private List<JdbcPosition> initiateConsumePosition(String offset) throws TaskStopTriggerException {
        Map<String, JdbcPosition> initiatePosition = new HashMap<>();
        if (StringUtils.isNotBlank(offset)) {
            JSONArray array = null;
            try {
                array = JSONArray.parseArray(offset);
            } catch (Throwable e) {
                array = new JSONArray(0);
            }
            //任务配置传入进度
            for (int i = 0; i < array.size(); i++) {
                JSONObject o = array.getJSONObject(i);
                String tableKey = o.getString(JdbcPosition.TABLE_KEY).toUpperCase();
                if (fetchMeta.containsKey(tableKey)) {
                    initiatePosition.put(tableKey, JdbcPosition.getPosition(o));
                }
            }
        }

        //初始化配置
        fetchMeta.values().stream().filter(c -> !initiatePosition.containsKey(c.getTable().toUpperCase()))
                .forEach(config -> {
                    initiatePosition.put(config.getTable().toUpperCase(), new JdbcPosition(config.getTable(), config.getIncrementColumn(), null, config.getTimestampColumn(), null));
                });
        return new ArrayList<>(initiatePosition.values());
    }

    @Override
    public String getInitiatePosition(String offset) throws TaskStopTriggerException {
        List<JdbcPosition> positions = initiateConsumePosition(offset);
        return JSONArray.toJSONString(positions);
    }


    @Override
    protected boolean isAlready() throws InterruptedException {
        canFetch.await();
        return true;
    }

    public static class JdbcPositions extends Position {
        private JdbcPosition[] positions;

        public static JdbcPositions snapshot(List<JdbcPosition> positions) {
            return snapshot(positions.toArray(new JdbcPosition[0]));
        }
        public static JdbcPositions snapshot(JdbcPosition[] positions) {
            List<JdbcPosition> copyPosition = JSONArray.parseArray(JSONArray.toJSONString(positions), JdbcPosition.class);
            JdbcPositions p = new JdbcPositions();
            p.positions = copyPosition.toArray(new JdbcPosition[0]);
            return p;
        }
        public String render() {
            JSONArray array = new JSONArray();
            Arrays.stream(positions).forEach(p -> array.add(p.render()));
            return array.toJSONString();
        }

        @Override
        public boolean checksum() {
            return null != positions && positions.length > 0;
        }
    }

    /**
     * jdbc位点信息
     */
    public static class JdbcPosition extends Position {
        private static String TABLE_KEY = "table";
        private static String INCREMENTCOLUMN_KEY = "incrementColumn";
        private static String INCREMENTCOLUMNVALUE_KEY = "incrementColumnValue";
        private static String TIMESTAMPCOLUMN_KEY = "timestampColumn";
        private static String TIMESTAMPVALUE_KEY = "timestampValue";

        @Getter @Setter
        private String table;
        @Getter @Setter
        private String incrementColumn;
        @Getter @Setter
        private Long incrementColumnValue;
        @Getter @Setter
        private String timestampColumn;
        @Getter @Setter
        private Long timestampValue;

        @JSONField(serialize=false)
        private long queryTimes = 1;
        public JdbcPosition() {
        }
        public JdbcPosition(String table, String incrementColumn, Long incrementColumnValue, String timestampColumn, Long timestampValue) {
            this.table = table.toUpperCase();
            this.incrementColumn = null != incrementColumn ? incrementColumn.toUpperCase() : "";
            this.timestampColumn = null != timestampColumn ? timestampColumn.toUpperCase() : "";
            this.incrementColumnValue = incrementColumnValue;
            this.timestampValue = timestampValue;
        }

        public JdbcPosition changeIncrementColumnValue(Long incrementColumnValue) {
            this.incrementColumnValue = incrementColumnValue;
            return this;
        }
        public JdbcPosition changeTimestampValue(Long timestampValue) {
            this.timestampValue = timestampValue;
            return this;
        }
        /**
         * getPosition
         *
         * @param position
         * @return
         * @throws TaskStopTriggerException
         */
        private static JdbcPosition getPosition(JSONObject position) throws TaskStopTriggerException {
            try {
                String table = position.getString(TABLE_KEY);
                String incrementColumn = position.getString(INCREMENTCOLUMN_KEY);
                Long incrementColumnValue = position.getLongValue(INCREMENTCOLUMNVALUE_KEY);
                String timestampColumn = position.getString(TIMESTAMPCOLUMN_KEY);
                Long timestampValue = position.getLong(TIMESTAMPVALUE_KEY);
                return new JdbcPosition(table, incrementColumn, incrementColumnValue, timestampColumn, timestampValue);
            } catch (Throwable throwable) {
                throw new TaskStopTriggerException(throwable);
            }
        }

        private static JdbcPosition getPosition(String position) throws TaskStopTriggerException {
            return getPosition(JSONObject.parseObject(position));
        }

        @Override
        public boolean checksum() {
            return StringUtils.isBlank(table) || (StringUtils.isBlank(incrementColumn) && StringUtils.isBlank(timestampColumn));
        }

        private Pair<String, List<Object>> buildSql(JdbcConsumerConfig.TableFetchConfig meta) {
            List<Object> args = new ArrayList<>();
            StringBuilder sb = new StringBuilder("select distinct * from ");
            sb.append(table);
            sb.append(" where ");
            boolean incrementColumnCondition = StringUtils.isNotBlank(incrementColumn);
            boolean timestampColumnCondition = StringUtils.isNotBlank(timestampColumn);
            if (incrementColumnCondition) {
                incrementColumnValue = null == incrementColumnValue ? -1 : incrementColumnValue;
                sb.append(timestampColumnCondition ? " (" : "");
                sb.append(" ").append(incrementColumn).append(" > ? and ").append(incrementColumn).append("  <= ?");
                args.add(incrementColumnValue);
                args.add(incrementColumnValue + meta.getIncrementColumnSpan() * queryTimes);
                if (timestampColumnCondition) sb.append(" )");
            }

            if (timestampColumnCondition) {
                sb.append(incrementColumnCondition ? " or ( " : " ");
                String columnCast = StringUtils.isNotBlank(meta.getTimestampColumnCast()) ? meta.getTimestampColumnCast() : timestampColumn;
                timestampValue = null == timestampValue ? -1 : timestampValue;
                sb.append(columnCast).append(" > ? and ").append(columnCast).append(" <= ?");
                args.add(timestampValue);
                args.add(timestampValue + meta.getTimestampSpan() * queryTimes);
                if (incrementColumnCondition)  sb.append(" )");
            }

            //order by
            sb.append(" order by ");
            if (StringUtils.isNotBlank(timestampColumn)) {
                sb.append(timestampColumn);
            }
            if (StringUtils.isNotBlank(incrementColumn)) {
                if (StringUtils.isNotBlank(timestampColumn)) sb.append(",");
                sb.append(incrementColumn);
            }
            sb.append(" asc");
            return new ImmutablePair<>(sb.toString(), args);
        }


        public void  increaseQueryTimes() {
            try {
                queryTimes += 1;
            } catch (Throwable e) {
                queryTimes = 1;
            }
        }

        public void  clearQueryTimes() {
            queryTimes = 1;
        }
    }
}