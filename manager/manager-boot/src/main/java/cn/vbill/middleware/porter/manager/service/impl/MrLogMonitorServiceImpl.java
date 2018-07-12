/**
 *
 */
package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.entity.MrLogMonitor;
import cn.vbill.middleware.porter.manager.core.mapper.MrLogMonitorMapper;
import cn.vbill.middleware.porter.manager.service.MrLogMonitorService;
import cn.vbill.middleware.porter.common.statistics.NodeLog;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志监控信息表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class MrLogMonitorServiceImpl implements MrLogMonitorService {

    @Autowired
    private MrLogMonitorMapper mrLogMonitorMapper;

    @Override
    public Integer insert(MrLogMonitor mrLogMonitor) {
        return mrLogMonitorMapper.insert(mrLogMonitor);
    }

    @Override
    public Integer update(Long id, MrLogMonitor mrLogMonitor) {
        return mrLogMonitorMapper.update(id, mrLogMonitor);
    }

    @Override
    public Integer delete(Long id) {
        return mrLogMonitorMapper.delete(id);
    }

    @Override
    public MrLogMonitor selectById(Long id) {
        return mrLogMonitorMapper.selectById(id);
    }

    @Override
    public Page<MrLogMonitor> page(Page<MrLogMonitor> page, String ipAddress, Integer state, String beginTime, String endTime) {
        Integer total = mrLogMonitorMapper.pageAll(ipAddress, state, beginTime, endTime);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(mrLogMonitorMapper.page(page, ipAddress, state, beginTime, endTime));
        }
        return page;
    }

    @Override
    public void dealNodeLog(NodeLog log) {
        MrLogMonitor mrLogMonitor = new MrLogMonitor(log);
        mrLogMonitorMapper.insert(mrLogMonitor);
    }

}
