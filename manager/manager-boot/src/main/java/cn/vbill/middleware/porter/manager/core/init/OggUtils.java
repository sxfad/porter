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

package cn.vbill.middleware.porter.manager.core.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.service.OggTablesService;
import cn.vbill.middleware.porter.manager.service.impl.OggTablesServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vbill.middleware.porter.manager.core.entity.OggTables;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class OggUtils {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public static Map<String, Long> OGG_TABLE = new HashMap<>();

    public static OggUtils INSTANCE;

    public static OggUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OggUtils();
        }
        return INSTANCE;
    }

    public void init() {
        logger.info("OggUtils init");
        this.loadOggTables();
    }

    private void loadOggTables() {
        OggTablesService oggTablesService = ApplicationContextUtil.getBean(OggTablesServiceImpl.class);
        List<OggTables> list = oggTablesService.ipTables(null, null);
        for (OggTables oggTable : list) {
            String key = oggTable.getIpAddress().toLowerCase() + "-" + oggTable.getTableValue().toLowerCase();
            OGG_TABLE.put(key, oggTable.getId());
        }
    }

    public static Boolean existOggTables(String ip, String table) {
        String key = ip.toLowerCase() + "-" + table.toLowerCase();
        Boolean rekey = OGG_TABLE.containsKey(key);
        if (!rekey) {
            OggTablesService oggTablesService = ApplicationContextUtil.getBean(OggTablesServiceImpl.class);
            List<OggTables> list = oggTablesService.ipTables(ip, table);
            if (list.size() == 1 && list.get(0) != null) {
                rekey = true;
                OggTables oggTable = list.get(0);
                OGG_TABLE.put(oggTable.getIpAddress().toLowerCase() + "-" + oggTable.getTableValue().toLowerCase(),
                        oggTable.getId());
            }
        }
        return rekey;
    }

    public static Long getOggTableId(String ip, String table) {
        String key = ip.toLowerCase() + "-" + table.toLowerCase();
        return OGG_TABLE.get(key);
    }
}
