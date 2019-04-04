/**
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

import cn.vbill.middleware.porter.manager.core.dto.RoleDataControl;
import cn.vbill.middleware.porter.manager.core.entity.MrLogMonitor;
import cn.vbill.middleware.porter.manager.core.mapper.MrLogMonitorMapper;
import cn.vbill.middleware.porter.manager.service.MrLogMonitorService;
import cn.vbill.middleware.porter.common.node.statistics.NodeLog;
import cn.vbill.middleware.porter.manager.web.page.Page;
import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        return mrLogMonitorMapper.insert(mrLogMonitor, null);
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
    public Page<MrLogMonitor> page(Page<MrLogMonitor> page, String ipAddress, Integer state, String date) {
        //数据权限
        RoleDataControl roleDataControl = RoleCheckContext.getUserIdHolder();
        //拼接表名
        String nowTableName = getTableName(date);

        Integer total = mrLogMonitorMapper.pageAll(ipAddress, state, roleDataControl, nowTableName);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(mrLogMonitorMapper.page(page, ipAddress, state, roleDataControl, nowTableName));
        }
        return page;
    }

    @Override
    public void dealNodeLog(NodeLog log) {
        MrLogMonitor mrLogMonitor = new MrLogMonitor(log);
        //拼接当日表名
        String nowTableName = getTableName(null);
        mrLogMonitorMapper.insert(mrLogMonitor, nowTableName);
    }

    /**
     * 表名生成器
     *
     * @author FuZizheng
     * @date 2019-03-15 11:17
     * @param: [date]
     * @return: java.lang.String
     */
    private String getTableName(String date) {
        String newDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        newDate = (date != null ? date.replace("-", "") : sdf.format(new Date()));
        if (date != null) {
            newDate = date.replace("-", "");
        } else {
            newDate = sdf.format(new Date());
        }
        // 日志信息表实时表
        return "mr_log_monitor_" + newDate;
    }
}
