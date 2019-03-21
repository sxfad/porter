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

import org.apache.ibatis.annotations.Param;

/**
 *
 * @author: 付紫钲
 * @date: 2018/4/25
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

    /**
     * 创建后天的表
     *
     * @param newTable
     * @param oldTable
     */
    void createTable(@Param("newTable") String newTable, @Param("oldTable") String oldTable);

    /**
     * 判断明天表是否被创建
     *
     * @param tomorrowTable
     * @return
     */
    String checkTomorrowTable(@Param("tomorrowTable") String tomorrowTable);

    /**
     * 创建明天表
     *
     * @param tomorrowTable
     * @param oldTable
     */
    void createTomorrowTable(@Param("tomorrowTable") String tomorrowTable, @Param("oldTable") String oldTable);

}