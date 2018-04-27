/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.common.dic.AlertPlugin;
import com.suixingpay.datas.common.dic.ClusterPlugin;
import com.suixingpay.datas.common.dic.ConsumeConverterPlugin;
import com.suixingpay.datas.common.dic.ConsumerPlugin;
import com.suixingpay.datas.common.dic.DbType;
import com.suixingpay.datas.common.dic.LoaderPlugin;
import com.suixingpay.datas.common.dic.NodeStatusType;
import com.suixingpay.datas.common.dic.SourceType;
import com.suixingpay.datas.common.dic.TaskStatusType;
import com.suixingpay.datas.manager.core.enums.InputTypeEnum;
import com.suixingpay.datas.manager.core.enums.LogLevelEnum;
import com.suixingpay.datas.manager.service.DictService;
import com.suixingpay.datas.manager.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    public DictionaryService dictionaryService;

    @Override
    public Map<String, Map<String, Object>> dictAll() {
        Map<String, Map<String, Object>> map = new HashMap<>();
        map.put("AlertPlugin", AlertPlugin.LINKMAP); // AlertPlugin - 告警策略
        map.put("ClusterPlugin", ClusterPlugin.LINKMAP); //ClusterPlugin - 集群方案
        map.put("ConsumeConverterPlugin", ConsumeConverterPlugin.LINKMAP); //ConsumeConverterPlugin 转换器插件
        map.put("ConsumerPlugin", ConsumerPlugin.LINKMAP); //ConsumerPlugin 消费器插件
        map.put("DbType", DbType.LINKMAP); //DbType 关系数据库类型
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
