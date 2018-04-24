package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.MrNodesMonitor;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点任务实时监控表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrNodesMonitorMapper {

    /**
     * 新增
     *
     * @param mrNodesMonitor
     */
    Integer insert(MrNodesMonitor mrNodesMonitor);

    /**
     * 修改
     *
     * @param mrNodesMonitor
     */
    Integer update(@Param("id") Long id, @Param("mrNodesMonitor") MrNodesMonitor mrNodesMonitor);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    MrNodesMonitor selectById(Long id);

    MrNodesMonitor selectByNodeIdAndTime(@Param("nodeId") String nodeId, @Param("dataTimes") String dataTimes);

    /**
     * 分頁
     *
     * @return
     */
    List<MrNodesMonitor> page(@Param("page") Page<MrNodesMonitor> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * @param nodeId
     * @param startRow
     * @param intervalTime
     * @return
     */
    List<MrNodesMonitor> selectByNodeId(@Param("nodeId") String nodeId,
                                        @Param("startRow") Long startRow,
                                        @Param("intervalTime") Long intervalTime);

    /**
     * 复制表结构和数据到新表
     *
     * @param mrNodesMonitorName
     * @param newDate
     */
    void createTable(@Param("mrNodesMonitorName") String mrNodesMonitorName, @Param("newDate") String newDate);

    /**
     * 删除前天数据
     *
     * @param newDate
     */
    void deleteByDate(@Param("newDate") String newDate);

    /**
     * 删除存在30天的数据迁移表
     *
     * @param mrNodesMonitorName
     */
    void dropTable(@Param("mrNodesMonitorName") String mrNodesMonitorName);
}