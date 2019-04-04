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

package cn.vbill.middleware.porter.common.task.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 19:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 19:07
 */
@NoArgsConstructor
public class TableMapperConfig {
    public TableMapperConfig(String[] schema, String[] table, Map<String, String> column, Boolean ignoreTargetCase,
            Boolean forceMatched) {
        this.schema = schema;
        this.table = table;
        this.column = column;
        this.ignoreTargetCase = ignoreTargetCase;
        this.forceMatched = forceMatched;
    }

    @Getter
    @Setter
    private String[] schema;

    @Getter
    @Setter
    private String[] table;

    @Getter
    @Setter
    private String[] updateDate;

    @Getter
    @Setter
    private Map<String, String> column;
    // 忽略目标端大小写
    @Getter
    @Setter
    private boolean ignoreTargetCase = true;
    /**
     * 字段映射后，强制目标端字段和源端字段一致，否则任务抛出异常停止
     */
    @Getter
    @Setter
    private boolean forceMatched = true;
}
