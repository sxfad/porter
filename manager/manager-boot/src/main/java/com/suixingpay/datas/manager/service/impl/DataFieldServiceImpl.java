/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2018-03-07 13:40:30  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.DataField;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.mapper.DataFieldMapper;
import com.suixingpay.datas.manager.service.DataFieldService;

/**
 * 数据字段对应表 服务实现类
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class DataFieldServiceImpl implements DataFieldService {

    @Autowired
    private DataFieldMapper dataFieldMapper;

    @Override
    public Integer insert(DataField dataField) {
        return dataFieldMapper.insert(dataField);
    }

    @Override
    public Integer update(Long id, DataField dataField) {
        return dataFieldMapper.update(id, dataField);
    }

    @Override
    public Integer delete(Long id) {
        return dataFieldMapper.delete(id);
    }

    @Override
    public DataField selectById(Long id) {
        return dataFieldMapper.selectById(id);
    }

    @Override
    public Page<DataField> page(Page<DataField> page) {
        Integer total = dataFieldMapper.pageAll(1);
        if(total>0) {
            page.setTotalItems(total);
            page.setResult(dataFieldMapper.page(page, 1));
        }
        return page;
    }

}
