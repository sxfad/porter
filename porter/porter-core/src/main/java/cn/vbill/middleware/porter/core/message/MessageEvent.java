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

package cn.vbill.middleware.porter.core.message;


import cn.vbill.middleware.porter.common.task.consumer.Position;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 消息事件
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月13日 18:22
 */
public class MessageEvent {
    //行同步位点
    @Getter @Setter private Position rowPosition;
    //当前行所在批次位点
    @Getter @Setter private Position bucketPosition;
    @Getter @Setter private String schema;
    @Getter @Setter private String table;
    //操作类型 I U D T
    @Getter @Setter private MessageAction opType;
    //操作时间
    @Getter @Setter private Date opTs;
    //解析事件的时间
    @Getter @Setter private Date currentTs;
    @Getter @Setter private long consumerTime;
    //解析事件的时间
    @Getter @Setter private long consumedTime;
    //修改之后的值
    @Getter @Setter private Map<String, Object> after = new HashMap<>();
    //修改之前的值
    @Getter @Setter private Map<String, Object> before = new HashMap<>();
    @Getter @Setter private List<String> primaryKeys = new ArrayList<>();
}
