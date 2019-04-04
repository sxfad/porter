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

package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.common.cluster.dic.ClusterPlugin;
import cn.vbill.middleware.porter.common.dic.*;
import cn.vbill.middleware.porter.common.node.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.task.dic.TaskStatusType;
import cn.vbill.middleware.porter.common.warning.entity.WarningPlugin;
import cn.vbill.middleware.porter.manager.core.enums.*;
import cn.vbill.middleware.porter.manager.core.enums.SourceType;
import cn.vbill.middleware.porter.manager.service.DictService;
import cn.vbill.middleware.porter.manager.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@Service
public class DictServiceImpl implements DictService {

    /**
     * dictionaryService
     */
    @Autowired
    public DictionaryService dictionaryService;

    @Override
    public Map<String, Map<String, Object>> dictAll() {
        Map<String, Map<String, Object>> map = new HashMap<>();
        map.put("AlertPlugin", WarningPlugin.LINKMAP); // WarningPlugin - 告警策略
        map.put("ClusterPlugin", ClusterPlugin.LINKMAP); //ClusterPlugin - 集群方案
        map.put("ConsumeConverterPlugin", ConsumeConverterPlugin.LINKMAP); //ConsumeConverterPlugin 转换器插件
        map.put("ConsumerPlugin", ConsumerPlugin.LINKMAP); //ConsumerPlugin 消费器插件
        map.put("DbType", DbType.LINKMAP); //DbType 关系数据库类型
        map.put("EnableType", EnableType.LINKMAP); //开关类型
        map.put("LoaderPlugin", LoaderPlugin.LINKMAP); // LoaderPlugin 载入器插件
        map.put("NodeStatusType", NodeStatusType.LINKMAP); //NodeStatusType 节点状态
        map.put("SourceType", SourceType.LINKMAP); //SourceType 数据源类型
        map.put("TaskStatusType", TaskStatusType.LINKMAP); //TaskStatusType 任务状态
        map.put("InputTypeEnum", InputTypeEnum.LINKMAP); //InputTypeEnum 页面输入框类型
        map.put("LogLevelEnum", LogLevelEnum.LINKMAP); //LogLevelEnum 日志级别
        map.putAll(dictionaryService.selectMap());
        return map;
    }

    @Override
    public Map<String, Object> dictByType(String type) {
        return dictAll().get(type);
    }
}