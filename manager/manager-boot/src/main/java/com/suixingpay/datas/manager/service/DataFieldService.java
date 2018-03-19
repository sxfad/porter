package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.DataField;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 数据字段对应表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataFieldService {

    Integer insert(DataField dataField);

    Integer update(Long id, DataField dataField);

    Integer delete(Long id);

    DataField selectById(Long id);

    Page<DataField> page(Page<DataField> page);
}
