/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 15:08
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.loader;

import com.suixingpay.datas.common.client.LoadClient;
import com.suixingpay.datas.common.client.MetaQueryClient;
import com.suixingpay.datas.common.db.meta.TableSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 15:08
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月09日 15:08
 */
public abstract class AbstractDataLoader implements DataLoader {
    protected  final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private LoadClient loadClient;
    private MetaQueryClient metaQueryClient;

    protected abstract String getPluginName();

    @Override
    public void setLoadClient(LoadClient loadClient) {
        this.loadClient = loadClient;
    }

    public <T> T getLoadClient() {
        return (T) loadClient;
    }

    @Override
    public void setMetaQueryClient(MetaQueryClient metaQueryClient) {
        this.metaQueryClient = metaQueryClient;
    }

    @Override
    public boolean isMatch(String loaderName) {
        return getPluginName().equals(loaderName);
    }

    @Override
    public void shutdown() throws Exception {
        if (!loadClient.isPublic()) loadClient.shutdown();
        if (!metaQueryClient.isPublic()) metaQueryClient.shutdown();
    }

    @Override
    public void startup() throws Exception {
        loadClient.start();
        metaQueryClient.start();
    }

    @Override
    public int getDataCount(String schema, String table, String updateDateColumn, Date startTime, Date endTime) {
        return metaQueryClient.getDataCount(schema, table, updateDateColumn, startTime, endTime);
    }

    @Override
    public TableSchema findTable(String finalSchema, String finalTable) throws Exception {
        return metaQueryClient.getTable(finalSchema, finalTable);
    }


}
