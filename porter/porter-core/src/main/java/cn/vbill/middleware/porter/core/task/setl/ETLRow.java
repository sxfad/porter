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

import cn.vbill.middleware.porter.common.task.consumer.Position;
import cn.vbill.middleware.porter.core.message.MessageAction;
import lombok.Getter;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


/**
 *
 * 将消息源事件转换为ETLRow
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 18:41
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 18:41
 */
public class ETLRow {
    private final String schema;
    private final String table;
    //操作时间，保留该字段可以在需要的时候计算出与最终执行时间间隔
    private final Date opTime;
    //消息存储到consumer的时间
    @Getter private final long consumerTime;
    @Getter private final long consumedTime;
    //当前消息所在消费源的下标、顺序位置
    private final Position position;
    //操作类型 I U D T
    private final MessageAction opType;



    /**
     * 可修改自定义内容
     */
    //操作类型 I U D T
    private MessageAction finalOpType;
    private String finalSchema;
    private String finalTable;
    private final List<ETLColumn> columns;
    /**
     * 不包含在columns中
     * 源端没有，目标端必填的字段，默认初始化为目标端默认值
     * 载入器根据各自实现选择使用该字段
     * JdbcLoader未使用该字段
     */
    private final List<ETLColumn> additionalRequired = new ArrayList<>();
    /**
     * 扩展字段，不同的载入器插件有不同的值
     */
    private final Map<String, Object> extendsField = new LinkedHashMap<>();

    private boolean isKeyChangedOnUpdate = false;



    public ETLRow(long consumedTime, long consumerTime, String schema, String table, MessageAction opType, List<ETLColumn> columns, Date opTime, Position position) {
        this.schema = schema;
        this.table = table;
        this.opType = opType;
        this.columns = columns;
        this.opTime = opTime;
        this.consumerTime = consumerTime;
        this.consumedTime = consumedTime;
        //数据映射时使用
        this.finalOpType = opType;
        this.finalSchema = schema;
        this.finalTable = table;
        this.position = position;
    }

    /**
     * 大写
     *
     * @date 2018/8/8 下午5:55
     * @param: []
     * @return: cn.vbill.middleware.porter.core.task.entity.message.ETLRow
     */
    public ETLRow toUpperCase() {
        this.finalSchema = finalSchema.toUpperCase();
        this.finalTable = finalTable.toUpperCase();
        columns.forEach(c -> c.toUpperCase());
        return this;
    }

    public List<ETLColumn> getColumns() {
        return columns;
    }

    public Date getOpTime() {
        return opTime;
    }

    public String getFinalSchema() {
        return finalSchema;
    }

    public void setFinalSchema(String finalSchema) {
        this.finalSchema = finalSchema;
    }

    public String getFinalTable() {
        return finalTable;
    }

    public void setFinalTable(String finalTable) {
        this.finalTable = finalTable;
    }

    public Position getPosition() {
        return position;
    }

    public List<ETLColumn> getAdditionalRequired() {
        return additionalRequired;
    }

    public Map<String, Object> getExtendsField() {
        return extendsField;
    }

    public boolean isKeyChangedOnUpdate() {
        return isKeyChangedOnUpdate;
    }

    public void setKeyChangedOnUpdate(boolean keyChangedOnUpdate) {
        isKeyChangedOnUpdate = keyChangedOnUpdate;
    }

    public MessageAction getFinalOpType() {
        return finalOpType;
    }

    public void setFinalOpType(MessageAction finalOpType) {
        this.finalOpType = finalOpType;
    }
}
