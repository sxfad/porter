package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.common.statistics.TaskPerformance;
import cn.vbill.middleware.porter.manager.core.entity.MrNodesMonitor;
import cn.vbill.middleware.porter.manager.core.icon.MrNodeMonitor;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 节点任务实时监控表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrNodesMonitorService {

    Integer insert(MrNodesMonitor mrNodesMonitor);

    Integer update(Long id, MrNodesMonitor mrNodesMonitor);

    Integer delete(Long id);

    MrNodesMonitor selectById(Long id);

    Page<MrNodesMonitor> page(Page<MrNodesMonitor> page);

    /** 节点区间数据统计. */
    void dealTaskPerformance(TaskPerformance performance);

    MrNodeMonitor obNodeMonitor(String nodeId, Long intervalTime, Long intervalCount);

}
