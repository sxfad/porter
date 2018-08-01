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

package cn.vbill.middleware.porter.common.db.meta;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 *
 */
public abstract class DdlUtilsFilter {

    /**
     * 返回要获取 {@linkplain DatabaseMetaData} 的 {@linkplain Connection}，不能返回null
     * 
     * @param con
     * @return
     */
    public Connection filterConnection(Connection con) throws Exception {
        return con;
    }

    /**
     * 对 databaseMetaData 做一些过滤,返回 {@linkplain DatabaseMetaData}，不能为 null
     * 
     * @param databaseMetaData
     * @return
     */
    public DatabaseMetaData filterDataBaseMetaData(JdbcTemplate jdbcTemplate, Connection con,
                                                   DatabaseMetaData databaseMetaData) throws Exception {
        return databaseMetaData;
    }

}
