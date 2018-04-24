/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.common.alarm.CanalAlarmHandler;
import com.alibaba.otter.canal.instance.core.CanalInstance;
import com.alibaba.otter.canal.instance.core.CanalInstanceGenerator;
import com.alibaba.otter.canal.instance.manager.CanalInstanceWithManager;
import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.canal.instance.manager.model.CanalStatus;
import com.alibaba.otter.canal.protocol.ClientIdentity;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.server.embedded.CanalServerWithEmbedded;
import com.alibaba.otter.canal.server.exception.CanalServerException;
import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.consumer.ConsumeClient;
import com.suixingpay.datas.common.config.source.CanalConfig;
import com.suixingpay.datas.common.consumer.Position;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * mysql slave 默认最新位点消费
 * 数据一致性通过上传位点到zookeeper保证
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public class CanalClient extends AbstractClient<CanalConfig> implements ConsumeClient {
    private int perPullSize;
    private long pollTimeOut;
    private final CanalServerWithEmbedded canalServer;
    private ClientIdentity clientId;
    private CountDownLatch canFetch = new CountDownLatch(1);
    /**
     * canal.server是否抛出异常
     */
    private AtomicBoolean hasBroken = new AtomicBoolean(false);
    private volatile Throwable brokenError;

    public CanalClient(CanalConfig config) {
        super(config);
        canalServer = new CanalServerWithEmbedded();
    }

    @Override
    protected void doStart() {
        CanalConfig config = getConfig();
        perPullSize = config.getOncePollSize();
        pollTimeOut = config.getPollTimeOut();
        clientId = new ClientIdentity(config.getDatabase(), config.getSlaveId().shortValue(), config.getFilter());
    }

    @Override
    protected void doShutdown() {
        //重置不能提取数据,避免因fetch调用导致任务停止中断
        canFetch = new CountDownLatch(1);
        try {
            if (null != canalServer) {
                canalServer.stop(getConfig().getDatabase());
            }
        } catch (Throwable e) {
            //https://github.com/alibaba/canal/issues/413
            //导致任务停止终止，无法销毁相关资源
        } finally {
            if (null != canalServer) {
                try {
                    canalServer.stop();
                } catch (Throwable e) {
                }
            }
        }
        //重置错误状态
        hasBroken = new AtomicBoolean(false);
        brokenError = null;
    }

    @Override
    public void  initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException {
        CanalConfig config = getConfig();
        /**
         * 这里的批次提交和后续的处理批次不一致，会导致这里的一个批次被分配到不同的数据库事务执行。
         * 为了保证数据一致性，需要将zookeeper记录的消费同步点回滚后重新执行
         */
        canalServer.setCanalInstanceGenerator(new CanalInstanceGenerator() {
            @Override
            @SneakyThrows(TaskStopTriggerException.class)
            public CanalInstance generate(String destination) {
                Canal canal = new Canal();
                canal.setCanalParameter(new CanalParameter());
                canal.setId(config.getCanalId());
                canal.setName(config.getCanalName());
                //canal启动状态
                canal.setStatus(CanalStatus.START);
                //mysql slaveId 唯一
                canal.getCanalParameter().setSlaveId(config.getSlaveId());
                //是否将ddl单条返回
                canal.getCanalParameter().setDdlIsolation(true);
                canal.getCanalParameter().setFilterTableError(true);
                //数据库ip:port
                canal.getCanalParameter().setMasterAddress(config.getSocketAddress());
                //数据库密码
                canal.getCanalParameter().setDbPassword(config.getPassword());
                //数据库用户名
                canal.getCanalParameter().setDbUsername(config.getUsername());
                //连接数据库
                canal.getCanalParameter().setDefaultDatabaseName(config.getDatabase());
                //消费同步点存储策略
                canal.getCanalParameter().setIndexMode(CanalParameter.IndexMode.MEMORY);
                canal.getCanalParameter().setHaMode(CanalParameter.HAMode.HEARTBEAT);
                //心跳
                canal.getCanalParameter().setHeartbeatHaEnable(false);
                canal.getCanalParameter().setCanalId(canal.getId());
                //源端数据库类型
                canal.getCanalParameter().setSourcingType(config.getSourcingType());
                //消息存储模型
                canal.getCanalParameter().setStorageMode(CanalParameter.StorageMode.MEMORY);
                canal.getCanalParameter().setMemoryStorageBufferSize(32 * 1024);
                //忽略表解析异常
                canal.getCanalParameter().setFilterTableError(true);
                //从上次失败位置开始消费
                if (!StringUtils.isBlank(position)) {
                    CanalPosition canalPosition = CanalPosition.getPosition(position);
                    canal.getCanalParameter().setMasterLogfileName(canalPosition.logfileName);
                    canal.getCanalParameter().setMasterLogfileOffest(canalPosition.offset);
                }

                CanalInstanceWithManager instance = new CanalInstanceWithManager(canal, clientId.getFilter());
                instance.setAlarmHandler(new CanalAlarmHandler()  {
                    private volatile boolean isRun = false;

                    @Override
                    public void sendAlarm(String destination, String msg) {
                        msg = StringUtils.trimToEmpty(msg);
                        //master连接不上
                        if (msg.contains("CanalParseException: java.io.IOException")
                                || msg.contains("java.io.IOException: Received error packet: errno")) {
                            if (hasBroken.compareAndSet(false, true)) {
                                brokenError = new TaskStopTriggerException("【Canal链接建立失败】【" + config.getProperties() + "】" + msg);
                            }
                        }
                    }

                    @Override
                    public void start() {
                        isRun = true;
                    }

                    @Override
                    public void stop() {
                        isRun = false;
                    }

                    @Override
                    public boolean isStart() {
                        return isRun;
                    }
                });
                return instance;
            }
        });
        canalServer.start();
        canalServer.start(clientId.getDestination());
        // 发起一次订阅
        canalServer.subscribe(clientId);
        canFetch.countDown();
    }
    
    @Override
    public <F, O>  List<F> fetch(FetchCallback<F, O> callback) throws TaskStopTriggerException, InterruptedException {
        if (hasBroken.get()) {
            throw null != brokenError ? new TaskStopTriggerException(brokenError) : new TaskStopTriggerException("canal.server因异常中断");
        }

        List<F> msgList = new ArrayList<>();
        if (isStarted()) {
            Message msg = null;
            try {
                msg = canalServer.get(clientId, perPullSize, pollTimeOut < 0 ? null : pollTimeOut, TimeUnit.MILLISECONDS);
            } catch (CanalServerException e) {
                boolean causeByThread = Arrays.stream(e.getThrowables())
                        .filter(error -> error instanceof InterruptedException).count() > 0;
                if (causeByThread) {
                    throw new InterruptedException(e.getMessage());
                } else {
                    throw e;
                }
            }
            /**
            if (isAutoCommitPosition()) {
                msg = canalServer.get(clientId, perPullSize, 5L, TimeUnit.SECONDS);
            } else {
                msg = canalServer.getWithoutAck(clientId, perPullSize, 5L, TimeUnit.SECONDS);
            }
             **/
            if (null != msg && msg.getId() != -1) {
                try {
                    List<F> f = callback.acceptAll(msg);
                    if (null != f && !f.isEmpty()) {
                        msgList.addAll(f);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                //没有要处理的数据时需要直接ack
                /**
                if (!isAutoCommitPosition() && msgList.isEmpty()) {
                    canalServer.ack(clientId, msg.getId());
                }**/
            }
        }
        return msgList;
    }

    @Override
    public <T> List<T> splitSwimlanes() {
        List<T> clients = new ArrayList<>();
        clients.add((T) this);
        return clients;
    }

    @Override
    public boolean isAutoCommitPosition() {
        return true;
    }

    @Override
    public String getSwimlaneId() {
        return getConfig().getDatabase();
    }

    @Override
    public  void commitPosition(Position position) throws TaskStopTriggerException {
        //如果提交方式为手动提交
        /**
        if (!isAutoCommitPosition() && isStarted()) {
            try {
                CanalPosition canalPosition = (CanalPosition) position;
                canalServer.ack(clientId, canalPosition.batchId);
            } catch (Throwable e) {
                throw new TaskStopTriggerException(e);
            }
        }
         **/
    }

    @Override
    protected boolean isAlready() {
        try {
            canFetch.await();
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * canal位点信息
     */
    public static class CanalPosition extends Position {
        @Getter private final long batchId;
        @Getter private final long offset;
        @Getter private final String logfileName;
        private final boolean checksum;
        public CanalPosition(long batchId, long offset, String logfileName) {
            this.batchId = batchId;
            this.offset = offset;
            this.logfileName = logfileName;
            this.checksum = !StringUtils.isBlank(logfileName) && offset > -1 && batchId > -1;
        }
        public CanalPosition(long batchId) {
            this(batchId, -1, "");
        }
        private static CanalPosition getPosition(String position) throws TaskStopTriggerException {
            try {
                JSONObject object = JSONObject.parseObject(position);
                long batchId = object.getLongValue("batchId");
                long offset = object.getLongValue("offset");
                String logfileName = object.getString("logfileName");
                return new CanalPosition(batchId, offset, logfileName);
            } catch (Throwable throwable) {
                throw new TaskStopTriggerException(throwable);
            }
        }

        @Override
        public boolean checksum() {
            return checksum;
        }
    }

}
