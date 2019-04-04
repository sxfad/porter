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

package cn.vbill.middleware.porter.core.task.entity;

import cn.vbill.middleware.porter.common.task.config.TableMapperConfig;

import java.util.Arrays;
import java.util.Map;

/**
 * 原表到目标表的映射
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月28日 16:03
 */
public class TableMapper {
    /**
     * 字段映射后，强制目标端字段和源端字段一致，否则任务抛出异常停止
     */
    private boolean forceMatched = true;
    //[源端(统一大写),目标端schema(区分大小写)]
    private String[] schema;
    //[源端表(统一大写),目标端表(区分大小写)]
    private String[] table;
    //[源端自动更新字段(统一大写),目标端自动更新字段(区分大小写)]
    private String[] updateDate;
    //[源端字段(统一大写),目标端字段(区分大小写)]
    private Map<String, String> column;
    //忽略目标端大小写
    private boolean ignoreTargetCase = true;

    public boolean isForceMatched() {
        return forceMatched;
    }

    public void setForceMatched(boolean forceMatched) {
        this.forceMatched = forceMatched;
    }

    public String[] getSchema() {
        return schema;
    }

    public void setSchema(String[] schema) {
        this.schema = schema;
    }

    public String[] getTable() {
        return table;
    }

    public void setTable(String[] table) {
        this.table = table;
    }

    public Map<String, String> getColumn() {
        return column;
    }

    public void setColumn(Map<String, String> column) {
        this.column = column;
    }

    /**
     * 获取UniqueKey
     *
     * @date 2018/8/9 上午9:51
     * @param: [taskId]
     * @return: java.lang.String
     */
    public String getUniqueKey(String taskId) {
        StringBuffer sb = new StringBuffer();
        sb.append(taskId).append("_");
        if (null != schema && schema.length == 2) {
            sb.append(schema[0]);
        }
        sb.append("_");
        if (null != table && table.length == 2) {
            sb.append(table[0]);
        }
        return sb.toString();
    }

    public String[] getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String[] updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * fromConfig
     *
     * @date 2018/8/9 上午9:53
     * @param: [config]
     * @return: cn.vbill.middleware.porter.core.task.entity.TableMapper
     */
    public static TableMapper fromConfig(TableMapperConfig config) {
        TableMapper mapper = new TableMapper();
        mapper.setColumn(config.getColumn());
        mapper.setSchema(config.getSchema());
        mapper.setTable(config.getTable());
        mapper.setUpdateDate(config.getUpdateDate());
        mapper.ignoreTargetCase = config.isIgnoreTargetCase();
        mapper.setForceMatched(config.isForceMatched());
        return mapper;
    }

    /**
     * toUpperCase
     *
     * @date 2018/8/9 上午9:54
     * @param: []
     * @return: cn.vbill.middleware.porter.core.task.entity.TableMapper
     */
    public TableMapper toUpperCase() {
        if (null != updateDate && updateDate.length == 2) {
            updateDate[0] = updateDate[0].toUpperCase();
            if (ignoreTargetCase) {
                updateDate[1] = updateDate[1].toUpperCase();
            }
        }

        if (null != table && table.length == 2) {
            table[0] = table[0].toUpperCase();
            if (ignoreTargetCase) {
                table[1] = table[1].toUpperCase();
            }
        }

        if (null != schema && schema.length == 2) {
            schema[0] = schema[0].toUpperCase();
            if (ignoreTargetCase) {
                schema[1] = schema[1].toUpperCase();
            }
        }

        if (null != column) {
            Arrays.stream(column.keySet().toArray(new String[0])).forEach(k -> {
                String v = ignoreTargetCase ? column.get(k).toUpperCase() : column.get(k);
                column.remove(k);
                column.put(k.toUpperCase(), v);
            });
        }
        return this;
    }

    public boolean isIgnoreTargetCase() {
        return ignoreTargetCase;
    }
}
