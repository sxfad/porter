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

package cn.vbill.middleware.porter.common.warning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 告警策略
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 11:42
 */

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WarningPlugin {

    /**
     * NONE
     */
    NONE("NONE", "无"),

    /**
     * CONSOLEEMAIL 
     * 此种模式下如果porter-boot节点邮件连接测试失败，将通过流转数据进入控制台再次发送邮件
     * 应对运维不给porter-boot节点开放外网的特殊情况
     */
    CONSOLEEMAIL("CONSOLEEMAIL", "集群邮件"),
    /**
     * EMAIL
     */
    EMAIL("EMAIL", "常规邮件"),

    /**
     * MOBILE
     */
    MOBILE("MOBILE", "手机号");

    /**
     * LINKMAP
     */
    public static final Map<String, Object> LINKMAP = new LinkedHashMap<>();

    static {
        LINKMAP.put(EMAIL.code, EMAIL.name);
        LINKMAP.put(CONSOLEEMAIL.code, CONSOLEEMAIL.name);
    }

    @Getter
    private final String code;
    @Getter
    private final String name;

}
