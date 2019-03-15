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

import cn.vbill.middleware.porter.manager.core.dto.RoleDataControl;
import cn.vbill.middleware.porter.manager.core.icon.HomeBlockResult;
import cn.vbill.middleware.porter.manager.core.mapper.HomeMapper;
import cn.vbill.middleware.porter.manager.service.HomeService;
import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author: 付紫钲
 * @date: 2018/4/26
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Override
    public HomeBlockResult bolck() {
        //数据权限
        RoleDataControl roleDataControl = RoleCheckContext.getUserIdHolder();
        //获取当前表名
        String newTableName = getTableName();

        HomeBlockResult homeBlockResult = homeMapper.block(roleDataControl, newTableName);
        return homeBlockResult;
    }

    private String getTableName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String newDate = sdf.format(new Date());
        return "mr_log_monitor_" + newDate;
    }
}
