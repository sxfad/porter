/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.manager.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import cn.vbill.middleware.porter.manager.core.dto.RoleDataControl;
import cn.vbill.middleware.porter.manager.core.entity.MrNodesSchedule;
import cn.vbill.middleware.porter.manager.core.mapper.MrNodesScheduleMapper;
import cn.vbill.middleware.porter.manager.service.MrNodesScheduleService;
import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;
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
        RoleDataControl roleDataControl = RoleCheckContext.getUserIdHolder();
        Integer total = mrNodesScheduleMapper.pageAll(1, ipAddress, computerName, roleDataControl);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(mrNodesScheduleMapper.page(page, 1, ipAddress, computerName, roleDataControl));
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

    /**
     * dealDNodeSync
     * @param nodeId
     * @param mrNodesSchedule
     */
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
