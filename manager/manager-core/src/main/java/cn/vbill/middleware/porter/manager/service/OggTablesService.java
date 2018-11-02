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

package cn.vbill.middleware.porter.manager.service;

import java.util.List;

import cn.vbill.middleware.porter.manager.core.entity.OggTables;
import cn.vbill.middleware.porter.manager.web.page.Page;

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
    void accept(String hearthead, String ip, String tables);

    /**
     * 查询数据
     * 
     * @return
     */
    List<OggTables> ipTables(String ipAddress, String tableValue);

    /**
     * 新增(插入非空字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer insert(OggTables oggTables);

    /**
     * 新增(插入全部字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer insertAllColumn(OggTables oggTables);

    /**
     * 修改(修改非空字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer update(Long id, OggTables oggTables);

    /**
     * 修改(修改全部字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer updateAllColumn(Long id, OggTables oggTables);

    /**
     * 根据主键查找实体
     * 
     * @param id
     * @return OggTables
     */
    OggTables selectById(Long id);

    /**
     * 分頁
     * 
     * @param page
     * @param other
     * @return Page
     */
    Page<OggTables> selectPage(Page<OggTables> page, String ipAddress, String tableValue);

    /**
     * 刪除
     * 
     * @param id
     * @return Integer
     */
    Integer delete(Long id);

}
