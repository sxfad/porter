/**
 * 
 */
package com.suixingpay.datas.manager.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.common.dic.AlertPlugin;
import com.suixingpay.datas.common.dic.ClusterPlugin;
import com.suixingpay.datas.common.dic.ConsumeConverterPlugin;
import com.suixingpay.datas.common.dic.ConsumerPlugin;
import com.suixingpay.datas.common.dic.DbType;
import com.suixingpay.datas.common.dic.LoaderPlugin;
import com.suixingpay.datas.common.dic.NodeStatusType;
import com.suixingpay.datas.common.dic.SourceType;
import com.suixingpay.datas.common.dic.TaskStatusType;
import com.suixingpay.datas.manager.service.DictService;
import com.suixingpay.datas.manager.service.DictionaryService;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    public DictionaryService dictionaryService;
    
    @Override
    public Map<String, Map<String, Object>> dictAll() {
        Map<String, Map<String, Object>> map = new HashMap<>();
        map.put("AlertPlugin", AlertPlugin.PLUGMAP);// AlertPlugin - 告警策略
        map.put("ClusterPlugin", ClusterPlugin.PLUGMAP);// - 集群方案
        map.put("ConsumeConverterPlugin", ConsumeConverterPlugin.PLUGMAP);//
        map.put("ConsumerPlugin", ConsumerPlugin.PLUGMAP);//
        map.put("DbType", DbType.PLUGMAP);//
        map.put("LoaderPlugin", LoaderPlugin.PLUGMAP);//
        map.put("NodeStatusType", NodeStatusType.PLUGMAP);//
        map.put("SourceType", SourceType.PLUGMAP);//
        map.put("TaskStatusType", TaskStatusType.PLUGMAP);//
        map.putAll(dictionaryService.selectMap());
        return map;
    }

    @Override
    public Map<String, Object> dictByType(String type) {
        return dictAll().get(type);
    }
}
