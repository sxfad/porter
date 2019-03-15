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

package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.dto.RoleDataControl;
import cn.vbill.middleware.porter.manager.core.entity.MrLogMonitor;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日志监控信息表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrLogMonitorMapper {

    /**
     * 新增
     *
     * @param mrLogMonitor
     * @param nowTableName
     */
    Integer insert(@Param("mrLogMonitor") MrLogMonitor mrLogMonitor, @Param("nowTableName") String nowTableName);

    /**
     * 修改
     *
     * @param mrLogMonitor
     */
    Integer update(@Param("id") Long id, @Param("mrLogMonitor") MrLogMonitor mrLogMonitor);

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
    MrLogMonitor selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<MrLogMonitor> page(@Param("page") Page<MrLogMonitor> page, @Param("ipAddress") String ipAddress,
                            @Param("state") Integer state, @Param("roleDataControl") RoleDataControl roleDataControl,
                            @Param("nowTableName") String nowTableName);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("ipAddress") String ipAddress, @Param("state") Integer state,
                    @Param("roleDataControl") RoleDataControl roleDataControl, @Param("nowTableName") String nowTableName);

}