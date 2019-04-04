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

package cn.vbill.middleware.porter.task.transform.transformer;

import cn.vbill.middleware.porter.core.message.MessageAction;
import com.alibaba.fastjson.JSON;
import cn.vbill.middleware.porter.common.util.db.meta.TableColumn;
import cn.vbill.middleware.porter.common.util.db.meta.TableSchema;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.core.task.setl.ETLColumn;
import cn.vbill.middleware.porter.core.task.setl.ETLRow;
import cn.vbill.middleware.porter.core.task.loader.DataLoader;
import cn.vbill.middleware.porter.core.task.entity.TableMapper;
import cn.vbill.middleware.porter.task.worker.TaskWork;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 13:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月28日 13:38
 */
public class ETLRowTransformer implements Transformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ETLRowTransformer.class);

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void transform(ETLBucket bucket, TaskWork work) throws Exception {
        LOGGER.debug("start tranforming bucket:{},size:{}", bucket.getSequence(), bucket.getRows().size());
        for (ETLRow row : bucket.getRows()) {
            LOGGER.debug("try tranform row:{},{}", row.getPosition().render(), JSON.toJSONString(row));
            //表映射
            TableMapper tableMapper = work.getTableMapper(row.getFinalSchema(), row.getFinalTable());
            mappingRowData(tableMapper, row);
            //目标端表结构元数据
            TableSchema table = findTable(work.getDataLoader(), row.getFinalSchema(), row.getFinalTable());
            if (null != tableMapper && tableMapper.isIgnoreTargetCase() && null != table) {
                table.toUpperCase();
            }

            /**
             * 数据库元数据正反向映射
             * 先根据table是否为null执行remedyColumns方法判断是否发生字段数量变更，
             * 最后根据tableMapper配置判断是否要求源端与目标端字段强一致
             */
            if (null != table && remedyColumns(table, row) && (null == tableMapper || tableMapper.isForceMatched())) {
                throw new TaskStopTriggerException("目标端与源端表结构不一致。"
                        + "映射表:" + JSON.toJSONString(tableMapper) + ",目标端表:" + JSON.toJSONString(table));
            }

            /**
             * 为了减少表结构造成的数据问题，增加人工介入机会。
             * 默认TableMapper为绝对正确的输入，当前ETLRow数据类型为Insert时，如果有不存在预配置字段项的映射，任务停止，人工介入
             */
            if (row.getFinalOpType() == MessageAction.INSERT && null != tableMapper && null != tableMapper.getColumn()
                    && !tableMapper.getColumn().isEmpty()) {
                for (String columnName : tableMapper.getColumn().values()) {
                    //最终字段与映射表匹配数量
                    long matchCount = row.getColumns().stream().filter(c -> c.getFinalName().equalsIgnoreCase(columnName)).count();
                    if (matchCount < 1) {
                        throw new TaskStopTriggerException("映射表与目标端表结构不一致。映射表:"
                                + JSON.toJSONString(tableMapper) + ",目标端表结构:" + JSON.toJSONString(table));
                    }
                }
            }


            //当是更新时，判断主键是否变更
            if (row.getFinalOpType() == MessageAction.UPDATE) {
                boolean isChanged = !row.getColumns().stream().filter(c -> c.isKey()
                        && !StringUtils.trimToEmpty(c.getFinalOldValue()).equals(StringUtils.trimToEmpty(c.getFinalValue())))
                        .collect(Collectors.toList()).isEmpty();
                row.setKeyChangedOnUpdate(isChanged);
            }

            //DataLoader自定义处理
            work.getDataLoader().mouldRow(row);

            LOGGER.debug("after tranform row:{},{}", row.getPosition().render(), JSON.toJSONString(row));
        }
    }

    /**
     * mappingRowData
     *
     * @date 2018/8/9 下午2:12
     * @param: [mapper, row]
     * @return: void
     */
    private void mappingRowData(TableMapper mapper, ETLRow row) {
        if (null != mapper) {
            //替换schema和表名
            if (null != mapper.getSchema() && mapper.getSchema().length == 2) {
                row.setFinalSchema(mapper.getSchema()[1]);
            }
            if (null != mapper.getTable() && mapper.getTable().length == 2) {
                row.setFinalTable(mapper.getTable()[1]);
            }
            if (null != row.getColumns() && null != mapper.getColumn()) {
                for (ETLColumn c : row.getColumns()) {
                    //替换字段名
                    c.setFinalName(mapper.getColumn().getOrDefault(c.getFinalName(), c.getFinalName()));
                }
            }
        }
    }

    /**
     * remedyColumns
     *
     * @date 2018/8/9 下午2:12
     * @param: [table, row]
     * @return: boolean
     */
    private boolean remedyColumns(TableSchema table, ETLRow row) {
        List<ETLColumn> removeables = new ArrayList<>();
        //正向查找
        for (ETLColumn c : row.getColumns()) {
            TableColumn column = table.findColumn(c.getFinalName());
            if (null != column) {
                c.setFinalType(column.getTypeCode());
                /**
                 * 如果目标端没有查出来主键，则默认按照源端主键
                 * 2018.05.25
                 */
                if (!table.isNoPrimaryKey()) {
                    c.setKey(column.isPrimaryKey());
                    /**
                     * 如果此字段在目标端是主键在源端不是主键，并且更新前值不存在，将原值设置为目标端的值
                     * 例如目标端DT_CTE、UUID为主键
                     * {"table":"DCM_OWNER.T_MSP_ORDER","op_type":"U","primary_keys":["UUID"],"before":{},
                     * "after":{"UUID":"3fd9e39aead54fc2bdd385bdf29cdc08","DT_CTE":"20180506",}}
                     * 2018.05.25 23:25
                     */
                    if (column.isPrimaryKey() && c.isFinalBeforeMissing() && row.getFinalOpType() == MessageAction.UPDATE) {
                        c.setFinalOldValue(c.getFinalValue());
                        c.setFinalBeforeMissing(false);
                    }
                }
                c.setRequired(column.isRequired());

                //如果是更新且字段必填，更新前的值不存在
                //if (row.getOpType() == MessageAction.UPDATE && column.isRequired() && StringUtils.isBlank(c.getOldValue())) {
                //    c.setOldValue(c.getNewValue());
                //}

            } else {
                removeables.add(c);
            }
        }

        //反向查找
        List<String> inColumnNames = row.getColumns().stream().map(p -> p.getFinalName()).collect(Collectors.toList());

        /**
         * 如果目标库表中有新增必填的字段的话需要补充上去
         */
        //table.getColumns().stream().filter(p -> !inColumnNames.contains(p.getName()) && p.isRequired() && null == p.getDefaultValue())
        List<TableColumn> targetNew = table.getColumns().stream().filter(p -> !inColumnNames.contains(p.getName()) && p.isRequired())
                .collect(Collectors.toList());
        targetNew.forEach(p -> {
            ETLColumn column = new ETLColumn(false, false, p.getName(), p.getDefaultValue(),
                    p.getDefaultValue(), p.getDefaultValue(), p.isPrimaryKey(), p.isRequired(), p.getTypeCode());
            row.getAdditionalRequired().add(column);
        });

        row.getColumns().removeAll(removeables);
        LOGGER.info("remove columns:{}", removeables.stream().map(p -> JSONObject.toJSONString(p)).reduce((p, n) -> p + "," + n).orElse(""));
        return !removeables.isEmpty();
    }

    /**
     * findTable
     *
     * @date 2018/8/9 下午2:13
     * @param: [loader, finalSchema, finalTable]
     * @return: TableSchema
     */
    private TableSchema findTable(DataLoader loader, String finalSchema, String finalTable)
            throws TaskStopTriggerException, InterruptedException {
        TableSchema table = null;
        try {
            table = loader.findTable(finalSchema, finalTable);
        } catch (InterruptedException e) {
            throw e;
        } catch (Throwable e) {
            String error = "查询不到目标仓库表结构" + finalSchema + ". " + finalTable;
            LOGGER.error(error, e);
            throw new TaskStopTriggerException(error + ";error:" + e.getMessage());
        }
        return table;
    }
}
