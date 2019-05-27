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

package cn.vbill.middleware.porter.plugin.connector.jdbc.config;

import cn.vbill.middleware.porter.plugin.connector.jdbc.JdbcConnectorConst;
import cn.vbill.middleware.porter.plugin.connector.jdbc.client.JdbcConsumeClient;
import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class JdbcConsumerConfig extends JdbcConfig {
    @Setter @Getter
    private TableFetchConfig[] table;
    //最大抽取任务数
    @Setter @Getter
    private int tasksMax = 1;

    public static class TableFetchConfig {
        @Setter @Getter
        private String table;
        @Setter @Getter
        private String incrementColumn;
        @Setter @Getter
        private long incrementColumnSpan = 500;
        @Setter @Getter
        private String timestampColumn;
        @Setter @Getter
        private String timestampColumnCast;
        @Setter @Getter
        private long timestampSpan = 1000 * 60 * 5;
    }

    @Override
    protected void childStuff() {
        Map<String, Map<String, String>> settings = new HashMap<>();
        getProperties().entrySet().stream().filter(k -> k.getKey().startsWith("table.")).forEach(c -> {
            String[] key = c.getKey().split("\\.");
            String index = key[1];
            String column = key[2];
            settings.computeIfAbsent(index, k -> {
                Map<String, String> item = new HashMap<>();
                item.put(column, c.getValue());
                return item;
            });
            settings.computeIfPresent(index, (k, v) -> {
                v.put(column, c.getValue());
                return v;
            });
        });
        table = JSONArray.parseArray(JSONArray.toJSONString(settings.values()), TableFetchConfig.class).toArray(new TableFetchConfig[0]);
    }

    @Override
    protected String[] childStuffColumns() {
        List<String> columns = new ArrayList<>(Arrays.asList(super.childStuffColumns()));
        columns.add("table");
        return columns.toArray(new String[0]);
    }

    @Override
    public Map<String, Class> getInstance() {
        return new HashMap<String, Class>() {
            {
                put(JdbcConnectorConst.CONSUME_SOURCE_TYPE_NAME.getCode(), JdbcConsumeClient.class);
            }
        };
    }

    @Override
    public String getSwimlaneId() {
        return Arrays.stream(table).map(t -> t.table).reduce((p, n) -> p + "_" + n).get();
    }
    public Map<String, TableFetchConfig> getTables() {
        Map<String, TableFetchConfig> meta = new ConcurrentHashMap<>(table.length);
        Arrays.stream(table).forEach(m -> meta.put(m.getTable().toUpperCase(), m));
        return meta;
    }

    @Override
    protected boolean doCheck() {
        return super.doCheck() && null != table && table.length > 0;
    }
}