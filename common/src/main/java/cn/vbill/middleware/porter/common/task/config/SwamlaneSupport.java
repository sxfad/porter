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


import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 对于可消费数据源，实现泳道拆分，最大限度减少任务消费端数据集
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月29日 15:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月29日 15:09
 */
public interface SwamlaneSupport {

    /**
     * swamlanes
     * @param <T>
     * @return
     * @throws ConfigParseException
     */
    <T extends SourceConfig> List<T> swamlanes() throws ConfigParseException;

    /**
     * getSwimlaneId
     * @return
     */
    @JSONField(serialize = false)
    default String getSwimlaneId() {
        throw new UnsupportedOperationException("不支持的方法调用");
    }
}
