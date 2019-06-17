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

package cn.vbill.middleware.porter.core.task.statistics;

import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.common.task.consumer.Position;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 单条数据执行结果，用于提交任务进度及性能指标统计
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月13日 14:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月13日 14:53
 */

public class DSubmitStatObject {
    @Setter @Getter private String schema;
    @Setter @Getter private String table;
    @Setter @Getter private MessageAction type;
    @Setter @Getter private boolean affect;
    @Setter @Getter private Position position;
    @Setter @Getter private Date opTime;


    public DSubmitStatObject(String schema, String table, MessageAction type, boolean affect, Position position, Date opTime) {
        this.schema = schema;
        this.table = table;
        this.type = type;
        this.affect = affect;
        this.position = position;
        this.opTime = opTime;
    }
    public DSubmitStatObject(String schema, String table, MessageAction type, int affect, Position position, Date opTime) {
        this(schema, table, type, affect > 0 || affect == -2, position, opTime);
    }
}
