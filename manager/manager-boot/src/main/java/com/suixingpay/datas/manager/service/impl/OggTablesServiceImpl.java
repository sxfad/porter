package com.suixingpay.datas.manager.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.entity.OggTables;
import com.suixingpay.datas.manager.core.init.OggUtils;
import com.suixingpay.datas.manager.core.mapper.OggTablesMapper;
import com.suixingpay.datas.manager.service.OggTablesService;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * ogg表数据信息 服务实现类
 * 
 * @author: FairyHood
 * @date: 2018-05-25 16:30:41
 * @version: V1.0-auto
 * @review: FairyHood/2018-05-25 16:30:41
 */
@Service
public class OggTablesServiceImpl implements OggTablesService {

    @Autowired
    private OggTablesMapper oggTablesMapper;

    @Override
    public void accept(String hearthead, String ip, String tables) {
        if (StringUtils.isEmpty(tables)) {
            return;
        }
        String[] tabs = tables.split(",");
        for (String tableName : tabs) {
            if (OggUtils.existOggTables(ip, tableName)) {
                Long id = OggUtils.getOggTableId(ip, tableName);
                oggTablesMapper.update(id, new OggTables(id, hearthead));
            } else {
                oggTablesMapper.insert(new OggTables(ip, tableName, hearthead));
            }
        }
    }

    @Override
    public List<OggTables> ipTables(String ipAddress, String tableValue) {
        return oggTablesMapper.selectList(ipAddress, tableValue);
    }

    @Override
    public Integer insert(OggTables oggTables) {
        Integer i = oggTablesMapper.insert(oggTables);
        return i;
    }

    @Override
    public Integer insertAllColumn(OggTables oggTables) {
        Integer i = oggTablesMapper.insertAllColumn(oggTables);
        return i;
    }

    @Override
    public Integer update(Long id, OggTables oggTables) {
        Integer i = oggTablesMapper.update(id, oggTables);
        return i;
    }

    @Override
    public Integer updateAllColumn(Long id, OggTables oggTables) {
        Integer i = oggTablesMapper.updateAllColumn(id, oggTables);
        return i;
    }

    @Override
    public OggTables selectById(Long id) {
        OggTables oggTables = oggTablesMapper.selectById(id);
        return oggTables;
    }

    @Override
    public Page<OggTables> selectPage(Page<OggTables> page, String ipAddress, String tableValue) {
        Integer total = oggTablesMapper.pageAll(ipAddress, tableValue);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(oggTablesMapper.page(page, ipAddress, tableValue));
        }

        page.getResult().forEach(l -> {
            //检查表关联任务信息
            String[] schemaAndTable = l.getTableValue().split(".");
            String relatedInfo = oggTablesMapper.relatedTask(schemaAndTable[schemaAndTable.length - 1]);
            l.setRelatedTaskInfo(relatedInfo);
        });
        return page;
    }

    @Override
    public Integer delete(Long id) {
        Integer i = oggTablesMapper.delete(id);
        return i;
    }
}
