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

package cn.vbill.middleware.porter.plugin.connector.jdbc.client;

import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.task.loader.LoadClient;
import cn.vbill.middleware.porter.plugin.connector.jdbc.config.JdbcConfig;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public class JdbcLoadClient extends JdbcClient implements LoadClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcLoadClient.class);

    public JdbcLoadClient(JdbcConfig config) {
        super(config);
    }

    /**
     * batchUpdate
     *
     * @param sqlType
     * @param sql
     * @param batchArgs
     * @return
     * @throws TaskStopTriggerException
     */
    public int[] batchUpdate(String sqlType, String sql, List<Object[]> batchArgs) throws TaskStopTriggerException, InterruptedException {
        int[] affect = getJdbcProxy().batchUpdate(sql, batchArgs);

        if (null == affect || affect.length == 0) {
            List<Integer> affectList = new ArrayList<>();
            //分组执行
            batchErroUpdate(sqlType, 50, sql, batchArgs, 0, affectList);

            affect = Arrays.stream(affectList.toArray(new Integer[]{})).mapToInt(Integer::intValue).toArray();
        }
        LOGGER.debug("sql:{},params:{},affect:{}", sql, JSON.toJSONString(batchArgs), affect);
        return affect;
    }

    /**
     * update
     *
     * @param type
     * @param sql
     * @param args
     * @return
     * @throws TaskStopTriggerException
     */
    public int update(String type, String sql, Object... args) throws TaskStopTriggerException, InterruptedException {
        int affect = getJdbcProxy().update(sql, args);
        if (affect < 1) {
            LOGGER.error("sql:{},params:{},affect:{}", sql, JSON.toJSONString(Arrays.asList(args)), affect);
        } else {
            LOGGER.debug("sql:{},params:{},affect:{}", sql, JSON.toJSONString(Arrays.asList(args)), affect);
        }
        return affect;
    }

    /**
     * batchErroUpdate
     *
     * @param sqlType
     * @param batchSize
     * @param sql
     * @param batchArgs
     * @param from
     * @param affect
     * @throws TaskStopTriggerException
     */
    private void batchErroUpdate(String sqlType, int batchSize, String sql, List<Object[]> batchArgs, int from, List<Integer> affect)
            throws TaskStopTriggerException, InterruptedException {
        int size = batchArgs.size();
        int batchEnd = from + batchSize;
        //获取当前分组
        List<Object[]> subArgs = new ArrayList<>();
        while (from < batchEnd && from < size) {
            subArgs.add(batchArgs.get(from));
            from++;
        }

        //根据当前分组批量插入
        int[] reGroupAffect = getJdbcProxy().batchUpdate(sql, subArgs, true);

        //如果仍然插入失败,改为单条插入
        if (null == reGroupAffect || reGroupAffect.length == 0) {
            for (int i = 0; i < subArgs.size(); i++) {
                affect.add(update(sqlType, sql, subArgs.get(i)));
            }
        } else {
            Arrays.stream(reGroupAffect).boxed().forEach(i -> affect.add(i));
        }
        //递归下次分组
        if (batchEnd < size) {
            batchErroUpdate(sqlType, batchSize, sql, batchArgs, from, affect);
        }
    }
}