/**
 *
 */
package cn.vbill.middleware.porter.manager.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import cn.vbill.middleware.porter.manager.core.entity.MrNodesSchedule;
import cn.vbill.middleware.porter.manager.core.mapper.MrNodesScheduleMapper;
import cn.vbill.middleware.porter.manager.service.MrNodesScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 节点任务监控表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class MrNodesScheduleServiceImpl implements MrNodesScheduleService {

    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(128);

    @Autowired
    private MrNodesScheduleMapper mrNodesScheduleMapper;

    @Override
    public Integer insert(MrNodesSchedule mrNodesSchedule) {
        return mrNodesScheduleMapper.insert(mrNodesSchedule);
    }

    @Override
    public Integer update(Long id, MrNodesSchedule mrNodesSchedule) {
        return mrNodesScheduleMapper.update(id, mrNodesSchedule);
    }

    @Override
    public Integer delete(Long id) {
        return mrNodesScheduleMapper.delete(id);
    }

    @Override
    public MrNodesSchedule selectById(Long id) {
        return mrNodesScheduleMapper.selectById(id);
    }

    @Override
    public Page<MrNodesSchedule> page(Page<MrNodesSchedule> page, String ipAddress, String computerName) {
        Integer total = mrNodesScheduleMapper.pageAll(1, ipAddress, computerName);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(mrNodesScheduleMapper.page(page, 1, ipAddress, computerName));
        }
        return page;
    }

    @Override
    public void dealDNode(DNode node) {
        MrNodesSchedule mrNodesSchedule = new MrNodesSchedule(node);
        String nodeId = mrNodesSchedule.getNodeId();
        String key = nodeId;
        Object lock = map.get(key);
        if (null == lock) {
            Object tmp = new Object();
            Object old = map.putIfAbsent(key, tmp);
            if (null != old) {
                lock = old;
            } else {
                lock = tmp;
            }
        }
        try {
            synchronized (lock) {
                dealDNodeSync(nodeId, mrNodesSchedule);
            }
        } finally {
            map.remove(key);
        }
    }

    private void dealDNodeSync(String nodeId, MrNodesSchedule mrNodesSchedule) {
        MrNodesSchedule old = mrNodesScheduleMapper.selectByNodeId(nodeId);
        if (old == null || old.getId() == null) {
            mrNodesScheduleMapper.insert(mrNodesSchedule);
        } else {
            mrNodesSchedule.setId(old.getId());
            mrNodesScheduleMapper.updateSelective(old.getId(), mrNodesSchedule);
        }
    }
}
