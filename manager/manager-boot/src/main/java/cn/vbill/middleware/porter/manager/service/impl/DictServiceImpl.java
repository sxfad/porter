/**
 *
 */
package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.common.dic.AlertPlugin;
import cn.vbill.middleware.porter.common.dic.ClusterPlugin;
import cn.vbill.middleware.porter.common.dic.ConsumeConverterPlugin;
import cn.vbill.middleware.porter.common.dic.ConsumerPlugin;
import cn.vbill.middleware.porter.common.dic.DbType;
import cn.vbill.middleware.porter.common.dic.LoaderPlugin;
import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.dic.SourceType;
import cn.vbill.middleware.porter.common.dic.TaskStatusType;
import cn.vbill.middleware.porter.manager.core.enums.InputTypeEnum;
import cn.vbill.middleware.porter.manager.core.enums.LogLevelEnum;
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
