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

import cn.vbill.middleware.porter.common.util.db.SqlTemplate;
import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.core.task.setl.ETLRow;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年04月23日 14:24
 * @version: V1.0
 * @review: zkevin/2019年04月23日 14:24
 */
public class SqlBuilder {
    private final SqlTemplate template;
    private final boolean isInsertOnUpdateError;

    public SqlBuilder(SqlTemplate template, boolean isInsertOnUpdateError) {
        this.template = template;
        this.isInsertOnUpdateError = isInsertOnUpdateError;
    }

    /**
     * 生成Load SQL
     * @param row
     * @return
     */
    protected List<Pair<String, Object[]>> build(ETLRow row) {
        //build sql
        List<Pair<String, Object[]>> sqlList = new ArrayList<>();

        //获取自定义字段
        Map<String, Pair<Object, Object>> sqlKeys = BaseJdbcLoader.CustomETLRowField.getSqlKeys(row);
        //数组组建类型
        Class strArrayComponent = new String[0].getClass().getComponentType();

        Map<String, Object> oldColumns = BaseJdbcLoader.CustomETLRowField.getOldColumns(row);
        Map<String, Object> newColumns = BaseJdbcLoader.CustomETLRowField.getNewColumns(row);

        //主键名
        String[] keyNames = sqlKeys.keySet().toArray(new String[]{});
        //主键新值
        Object[] keyNewValues = sqlKeys.values().stream().map(p -> p.getRight()).toArray();
        //主键旧值
        Object[] keyOldValues = sqlKeys.values().stream().map(p -> p.getLeft()).toArray();

        if (row.getFinalOpType() == MessageAction.DELETE) {
            //主键删除
            if (keyNames.length > 0) {
                sqlList.add(new ImmutablePair<>(template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), keyNames), keyNewValues));
            }

            //全字段删除
            //1.数组条件
            String[] allColumnNames = addArray(strArrayComponent, keyNames, newColumns.keySet().toArray(new String[0]));
            //所有字段新值
            Object[] allNewValues = addArray(keyNewValues, newColumns.values().toArray());
            //拼接sql
            sqlList.add(new ImmutablePair<>(template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames), allNewValues));
        } else if (row.getFinalOpType() == MessageAction.INSERT) {
            //1.数组条件
            String[] allColumnNames = addArray(strArrayComponent, keyNames, newColumns.keySet().toArray(new String[0]));
            //所有字段新值
            Object[] allNewValues = addArray(keyNewValues, newColumns.values().toArray());
            //插入sql
            sqlList.add(new ImmutablePair<>(template.getInsertSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames), allNewValues));
        } else if (row.getFinalOpType() == MessageAction.UPDATE) {
            String[] columnNames = newColumns.keySet().toArray(new String[0]);
            Object[] columnValues = newColumns.values().toArray();
            //存在主键，主键值没变，根据主键更新
            if (!row.isKeyChangedOnUpdate() && keyNames.length > 0 && null != columnNames && columnNames.length > 0) {
                sqlList.add(new ImmutablePair<>(template.getUpdateSql(row.getFinalSchema(), row.getFinalTable(), keyNames, columnNames),
                        addArray(columnValues, keyOldValues)));
            }
            //全字段更新
            Object[] oldColumnValues = oldColumns.values().toArray();
            String[] oldColumnNames = oldColumns.keySet().toArray(new String[0]);
            sqlList.add(new ImmutablePair<>(template.getUpdateSql(row.getFinalSchema(), row.getFinalTable(),
                    addArray(strArrayComponent, keyNames, oldColumnNames), addArray(strArrayComponent, keyNames, columnNames)),
                    addArray(keyNewValues, columnValues, keyOldValues, oldColumnValues)));

            //更新转插入
            if (isInsertOnUpdateError) {
                sqlList.add(new ImmutablePair<>(template.getInsertSql(row.getFinalSchema(), row.getFinalTable(),
                        addArray(strArrayComponent, keyNames, columnNames)), addArray(keyNewValues, columnValues)));
            }
        } else if (row.getFinalOpType() == MessageAction.TRUNCATE) {
            sqlList.add(new ImmutablePair<>(template.getTruncateSql(row.getFinalSchema(), row.getFinalTable()), new Object[]{}));
        }
        return sqlList;
    }

    private <T> T[] addArray(T[]... array) {
        return addArray(Object.class, array);
    }

    private <T> T[] addArray(Class componentType, T[]... array) {
        List<T> newArray = new ArrayList<>();
        Arrays.stream(array).forEach(a -> {
            if (null != a && a.length > 0) {
                newArray.addAll(Arrays.stream(a).collect(Collectors.toList()));
            }
        });
        return newArray.toArray((T[]) Array.newInstance(componentType, 0));
    }
}
