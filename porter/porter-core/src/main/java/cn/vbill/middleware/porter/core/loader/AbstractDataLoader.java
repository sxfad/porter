/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 15:08
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.core.loader;

import cn.vbill.middleware.porter.common.client.LoadClient;
import cn.vbill.middleware.porter.common.client.MetaQueryClient;
import cn.vbill.middleware.porter.common.db.meta.TableSchema;
import cn.vbill.middleware.porter.core.event.etl.ETLRow;
import org.apache.commons.lang3.time.DateFormatUtils;
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
    protected static final String TIME_TAKEN_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    private  volatile LoadClient loadClient;
    private  volatile MetaQueryClient metaQueryClient;
    //更新转插入策略开关
    private volatile boolean insertOnUpdateError = true;

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

    public boolean isInsertOnUpdateError() {
        return insertOnUpdateError;
    }

    public void setInsertOnUpdateError(boolean insertOnUpdateError) {
        this.insertOnUpdateError = insertOnUpdateError;
    }

    @Override
    public String getClientInfo() {
        StringBuffer clientInfo = new StringBuffer();
        if (null != metaQueryClient && metaQueryClient != loadClient) {
            clientInfo.append("元数据->").append(metaQueryClient.getClientInfo()).append(System.lineSeparator()).append("\t");
        }
        clientInfo.append("载入源->").append(loadClient.getClientInfo());
        return clientInfo.toString();
    }

    protected void printTimeTaken(ETLRow row) {
        try {
            LOGGER.info("消息处理耗时->trail操作:{},存储kafka:{},kafka消费:{},数据库载入:{}",
                    DateFormatUtils.format(row.getOpTime(), TIME_TAKEN_FORMAT),
                    DateFormatUtils.format(row.getConsumerTime(), TIME_TAKEN_FORMAT),
                    DateFormatUtils.format(row.getConsumedTime(), TIME_TAKEN_FORMAT),
                    DateFormatUtils.format(System.currentTimeMillis(), TIME_TAKEN_FORMAT));
        } catch (Throwable e) {

        }
    }
}
