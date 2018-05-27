package com.suixingpay.datas.manager.service;

import java.util.List;

import com.suixingpay.datas.manager.core.entity.OggTables;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * ogg表数据信息 服务接口类
 * 
 * @author: FairyHood
 * @date: 2018-05-25 16:30:41
 * @version: V1.0-auto
 * @review: FairyHood/2018-05-25 16:30:41
 */
public interface OggTablesService {

    /**
     * 接收数据
     * 
     * @param hearthead
     * @param ip
     * @param tables
     */
    public void accept(String hearthead, String ip, String tables);

    /**
     * 查询数据
     * 
     * @return
     */
    public List<OggTables> ipTables(String ipAddress, String tableValue);

    /**
     * 新增(插入非空字段)
     * 
     * @param oggTables
     * @return Integer
     */
    public Integer insert(OggTables oggTables);

    /**
     * 新增(插入全部字段)
     * 
     * @param oggTables
     * @return Integer
     */
    public Integer insertAllColumn(OggTables oggTables);

    /**
     * 修改(修改非空字段)
     * 
     * @param oggTables
     * @return Integer
     */
    public Integer update(Long id, OggTables oggTables);

    /**
     * 修改(修改全部字段)
     * 
     * @param oggTables
     * @return Integer
     */
    public Integer updateAllColumn(Long id, OggTables oggTables);

    /**
     * 根据主键查找实体
     * 
     * @param id
     * @return OggTables
     */
    public OggTables selectById(Long id);

    /**
     * 分頁
     * 
     * @param page
     * @param other
     * @return Page
     */
    public Page<OggTables> selectPage(Page<OggTables> page, String ipAddress, String tableValue);

    /**
     * 刪除
     * 
     * @param id
     * @return Integer
     */
    public Integer delete(Long id);

}
