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

package cn.vbill.middleware.porter.common.dic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 任务状态
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 10:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 10:32
 */

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskStatusType {

    /**
     * 新建
     */
    NEW("NEW", "新建"),

    /**
     * 已停止
     */
    STOPPED("STOPPED", "已停止"),

    /**
     * 工作中
     */
    WORKING("WORKING", "工作中"),

    /**
     * 已删除
     */
    DELETED("DELETED", "已删除");

    /**
     * LINKMAP
     */
    public static final HashMap<String, Object> LINKMAP = new LinkedHashMap<>();

    static {
        LINKMAP.put(NEW.code, NEW.name);
        LINKMAP.put(WORKING.code, WORKING.name);
        LINKMAP.put(STOPPED.code, STOPPED.name);
    }

    @Getter
    private final String code;
    @Getter
    private final String name;

    public boolean isStopped() {
        return this == STOPPED;
    }

    public boolean isWorking() {
        return this == WORKING;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }

}
