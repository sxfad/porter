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
 * 数据标识字典
 * 
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DataSignEnum {

    JOBTASK("JOBTASK", "任务数据", "job_tasks"), NODE("NODE", "节点数据", "b_nodes"), DATASOURCE("DATASOURCE", "数据源数据",
            "b_data_source"), DATATABLE("DATATABLE", "数据表组数据",
                    "b_data_table"), PUBLICDATASOURCE("PUBLICDATASOURCE", "公共数据源数据", "b_public_data_source");

    private final String code;

    private final String name;

    private final String table;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getTable() {
        return table;
    }

}
