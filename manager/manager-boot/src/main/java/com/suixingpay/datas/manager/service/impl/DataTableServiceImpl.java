/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.DataTable;
import com.suixingpay.datas.manager.core.mapper.DataTableMapper;
import com.suixingpay.datas.manager.service.DataTableService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据表信息表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class DataTableServiceImpl implements DataTableService {

    @Autowired
    private DataTableMapper dataTableMapper;

    @Override
    public Integer insert(DataTable dataTable) {
        return dataTableMapper.insertSelective(dataTable);
    }

    @Override
    public DataTable selectById(Long id) {
        return dataTableMapper.selectById(id);
    }

    @Override
    public Page<DataTable> page(Page<DataTable> page, String bankName, String beginTime, String endTime) {
        Integer total = dataTableMapper.pageAll(1, bankName, beginTime, endTime);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataTableMapper.page(page, 1, bankName, beginTime, endTime));
        }
        return page;
    }

    @Override
    public Integer delete(Long id) {
        return dataTableMapper.delete(id);
    }
}
