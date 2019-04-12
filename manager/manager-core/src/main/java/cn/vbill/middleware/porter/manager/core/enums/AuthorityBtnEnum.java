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

package cn.vbill.middleware.porter.manager.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuthorityBtnEnum {

    TURNOVER("TURNOVER", "移交"),
    SHARE("SHARE", "共享"),
    WAIVE("WAIVE", "放弃");

    private final String code;

    private final String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
