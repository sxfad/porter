package cn.vbill.middleware.porter.manager.core.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 付紫钲
 * @date: 2018/4/25
 * @copyright: ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
public interface MonitorScheduledMapper {

    /**
     * 备份前天数据到新表，并删除源表数据
     *
     * @param date
     * @param oldTableName
     * @param newTableName
     * @param newDate
     */
    void transferData(@Param("date") String date,
                      @Param("oldTableName") String oldTableName,
                      @Param("newTableName") String newTableName,
                      @Param("newDate") String newDate);

    /**
     * 删除存在30天的表
     *
     * @param newTableName
     */
    void dropTable(@Param("newTableName") String newTableName);
}
