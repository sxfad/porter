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

package cn.vbill.middleware.porter.core.task.setl;

import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.core.message.MessageEvent;
import com.alibaba.fastjson.JSON;
import cn.vbill.middleware.porter.common.task.consumer.Position;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 11:04
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月26日 11:04
 */
public class ETLBucket {
    private static final Logger LOGGER = LoggerFactory.getLogger(ETLBucket.class);
    /**
     * 由于数据顺序不是通过sequence排序保证，且程序节点持续运行不停机，同时存在Long类型序列号发放殆尽的问题，仅需进程内唯一。
     * 综上，将序列号改为UUID+timestamp组合
     */
    private final String sequence;
    /**
     * 单行LOAD
     */
    private final List<ETLRow> rows;
    /**
     * 拆分为批次处理的行
     */
    private final List<List<ETLRow>> parallelRows;

    /**
     * 在SETL过程中的异常,如果有值，意味着改批次的数据就要回滚
     */
    private Throwable exception = null;

    private final Position position;

    public ETLBucket(String sequence, List<ETLRow> rows, Position position) {
        this.sequence = sequence;
        this.rows = rows;
        this.parallelRows = new ArrayList<>();
        this.position = position;
    }

    public String getSequence() {
        return sequence;
    }

    public List<ETLRow> getRows() {
        return null == rows ? new ArrayList<>() : rows;
    }

    /**
     * 转换数据模型
     * @param events
     * @return
     */
    public static ETLBucket from(Pair<String, List<MessageEvent>> events) {
        List<ETLRow> rows = new ArrayList<>();
        for (MessageEvent event : events.getRight()) {
            LOGGER.debug(JSON.toJSONString(event));
            List<ETLColumn> columns = new ArrayList<>();
            if (null == event.getBefore()) {
                event.setBefore(new HashMap<>());
            }
            if (null == event.getAfter()) {
                event.setAfter(new HashMap<>());
            }
            if (null == event.getPrimaryKeys()) {
                event.setPrimaryKeys(new ArrayList<>());
            }

            Boolean loopAfter = !event.getAfter().isEmpty();
            for (Map.Entry<String, Object> entity : loopAfter ? event.getAfter().entrySet() : event.getBefore().entrySet()) {
                Object newValue = "";
                Object oldValue = "";
                //默认字段都是未丢失的
                boolean beforeMissing = false;
                boolean afterMissing = false;
                if (loopAfter) {
                    newValue = entity.getValue();
                    /**
                     *
                     * 2018-04-03 oracle版本差异导致更新时无变化字段不在before中展示
                     * oracle 版本:11.2.0.4
                     * 处理方法，主键未变化时填充，其他字段没变化时标识字段没变化
                     */
                    //如果存在
                    if (event.getBefore().containsKey(entity.getKey())) {
                        oldValue = event.getBefore().get(entity.getKey());
                    } else {
                        beforeMissing = true;
                        //如果是主键，标识前后值一致
                        if (event.getPrimaryKeys().contains(entity.getKey())) {
                            beforeMissing = false;
                            oldValue = newValue;
                        }
                    }
                } else {
                    if (event.getAfter().containsKey(entity.getKey())) {
                        newValue = event.getAfter().get(entity.getKey());
                    } else {
                        afterMissing = true;
                    }
                    oldValue = entity.getValue();
                }

                Object finalValue = newValue;

                //如果是删除类型时
                if (event.getOpType() == MessageAction.DELETE) {
                    finalValue = oldValue;
                    afterMissing = false;
                }

                String newValueStr = String.valueOf(newValue);
                newValueStr = newValueStr.equals("null") ? null : newValueStr;
                String oldValueStr = String.valueOf(oldValue);
                oldValueStr = oldValueStr.equals("null") ? null : oldValueStr;
                String finalValueStr = String.valueOf(finalValue);
                finalValueStr = finalValueStr.equals("null") ? null : finalValueStr;

                //源数据事件精度损失，转字符串也会有精度损失。后续观察处理
                ETLColumn column = new ETLColumn(beforeMissing, afterMissing, entity.getKey(), newValueStr, oldValueStr, finalValueStr,
                        event.getPrimaryKeys().contains(entity.getKey()));
                columns.add(column);
            }
            ETLRow row = new ETLRow(event.getConsumedTime(), event.getConsumerTime(), event.getSchema(), event.getTable(), event.getOpType(), columns, event.getOpTs(), event.getRowPosition());
            rows.add(row.toUpperCase());
            LOGGER.debug(JSON.toJSONString(row));
        }
        Position position = !events.getRight().isEmpty() ? events.getRight().get(events.getRight().size() - 1).getBucketPosition() : null;
        return new ETLBucket(events.getKey(), rows, position);
    }

    public List<List<ETLRow>> getParallelRows() {
        return parallelRows;
    }

    public Throwable getException() {
        return exception;
    }

    /**
     * tagException
     *
     * @date 2018/8/8 下午5:53
     * @param: [e]
     * @return: void
     */
    public void tagException(Throwable e) {
        this.exception = e;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * markUnUsed
     *
     * @date 2018/8/8 下午5:53
     * @param: []
     * @return: void
     */
    public void markUnUsed() {
        try {
            rows.forEach(r -> {
                r.getExtendsField().clear();
                r.getColumns().clear();
                r.getAdditionalRequired().clear();
            });
            rows.clear();
            parallelRows.clear();
        } catch (Throwable e) {
            LOGGER.warn("标记批次数据不可用出错", e);
        }
    }
}
