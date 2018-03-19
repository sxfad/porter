package com.suixingpay.datas.manager.service;

import java.util.List;

import com.suixingpay.datas.manager.core.entity.DataTable;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 数据表信息表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataTableService {

    Integer insert(DataTable dataTable);

    Integer delete(Long id);

    DataTable selectById(Long id);

    Page<DataTable> page(Page<DataTable> page, String bankName, String beginTime, String endTime);

    List<String> prefixList(Long sourceId);

    Page<Object> tableList(Page<Object> page, Long sourceId, String prefix, String tableName);
}
