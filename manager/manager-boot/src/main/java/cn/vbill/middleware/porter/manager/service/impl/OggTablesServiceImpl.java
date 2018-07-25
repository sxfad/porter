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

import java.util.List;

import cn.vbill.middleware.porter.manager.core.mapper.OggTablesMapper;
import cn.vbill.middleware.porter.manager.service.OggTablesService;
import cn.vbill.middleware.porter.manager.core.init.OggUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vbill.middleware.porter.manager.core.entity.OggTables;
import cn.vbill.middleware.porter.manager.web.page.Page;

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
            //检查表关联任务信息
            String[] schemaAndTable = tableName.split("\\.");
            String relatedInfo = oggTablesMapper.relatedTask(schemaAndTable[schemaAndTable.length - 1]);
            if (OggUtils.existOggTables(ip, tableName)) {
                Long id = OggUtils.getOggTableId(ip, tableName);
                OggTables table = new OggTables(id, hearthead);
                table.setRelatedTaskInfo(relatedInfo);
                oggTablesMapper.update(id, table);
            } else {
                OggTables table = new OggTables(ip, tableName, hearthead);
                table.setRelatedTaskInfo(relatedInfo);
                oggTablesMapper.insert(table);
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

//        page.getResult().forEach(l -> {
//            //检查表关联任务信息
//            String[] schemaAndTable = l.getTableValue().split("\\.");
//            String relatedInfo = oggTablesMapper.relatedTask(schemaAndTable[schemaAndTable.length - 1]);
//            l.setRelatedTaskInfo(relatedInfo);
//        });
        return page;
    }

    @Override
    public Integer delete(Long id) {
        Integer i = oggTablesMapper.delete(id);
        return i;
    }
}
