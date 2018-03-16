/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月15日 11:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.alibaba.otter.canal.instance.manager.CanalInstanceWithManager;
import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.canal.instance.manager.model.CanalStatus;
import com.alibaba.otter.canal.protocol.ClientIdentity;
import com.alibaba.otter.canal.server.embedded.CanalServerWithEmbedded;
import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.common.config.source.CanalConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import org.apache.kudu.client.KuduException;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月15日 11:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月15日 11:42
 */
public class CanalEmbedClient  extends AbstractClient<CanalConfig> implements ConsumeClient {
    private final CanalServerWithEmbedded canalServer;
    public CanalEmbedClient(CanalConfig config) {
        super(config);
        canalServer = new CanalServerWithEmbedded();
    }

    @Override
    protected void doStart() throws Exception {
        CanalConfig config = getConfig();
        canalServer.setCanalInstanceGenerator(destination -> {
            Canal canal = new Canal();
            canal.setCanalParameter(new CanalParameter());
            canal.setId(System.nanoTime());
            canal.setStatus(CanalStatus.START);
            canal.getCanalParameter().setSlaveId(System.nanoTime());
            canal.getCanalParameter().setDdlIsolation(false);
            canal.getCanalParameter().setFilterTableError(true);
            canal.getCanalParameter().setMasterAddress(new InetSocketAddress("45.77.95.200", 8888));
            canal.getCanalParameter().setDbPassword("zhuoluo1018");
            canal.getCanalParameter().setDbUsername("root");
            CanalInstanceWithManager instance = new CanalInstanceWithManager(canal, config.getFilter());
            return instance;
        });
        canalServer.start();
        canalServer.start(config.getDestination());
        canalServer.subscribe(new ClientIdentity(config.getDestination(), Short.valueOf("2017"), config.getFilter()));// 发起一次订阅
    }

    @Override
    protected void doShutdown() throws InterruptedException, KuduException {
        if (null != canalServer) {
            canalServer.stop(getConfig().getDestination());
        }
    }

    @Override
    public void commitPosition(String position) throws TaskStopTriggerException {

    }

    @Override
    public void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException {

    }

    @Override
    public <T> List<T> splitSwimlanes() throws ClientException {
        List<T> clients = new ArrayList<>();
        clients.add((T) this);
        return clients;
    }

    @Override
    public boolean isAutoCommitPosition() {
        return getConfig().isAutoCommit();
    }

    @Override
    public String getSwimlaneId() {
        return getConfig().getDestination();
    }

    @Override
    public <F, O> List<F> fetch(FetchCallback<F, O> callback) {
        return null;
    }
}
