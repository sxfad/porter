package com.suixingpay.datas.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.entity.OggTables;
import com.suixingpay.datas.manager.core.mapper.OggTablesMapper;
import com.suixingpay.datas.manager.service.OggTablesService;
import com.suixingpay.datas.manager.web.page.Page;

 /**  
 * ogg表数据信息 服务实现类
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
    public Page<OggTables> selectPage(Page<OggTables> page, String other) {
        Integer total = oggTablesMapper.pageAll(other);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(oggTablesMapper.page(page, other));
        }
        return page;
    }

    @Override
    public Integer delete(Long id) {
        Integer i = oggTablesMapper.delete(id);
        return i;
    }

}
