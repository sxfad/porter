/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:06
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client;

import com.suixingpay.datas.common.client.impl.JDBCClient;
import com.suixingpay.datas.common.client.impl.KafkaClient;
import com.suixingpay.datas.common.client.impl.ZookeeperClient;
import com.suixingpay.datas.common.config.Config;
import com.suixingpay.datas.common.config.source.*;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ClientMatchException;
import com.suixingpay.datas.common.exception.ConfigParseException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:06
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:06
 */
public abstract class AbstractClient<T extends Config> implements Client {

    protected final Logger LOGGER = LoggerFactory.getLogger(AbstractClient.class);
    private final AtomicBoolean status = new AtomicBoolean(false);
    @Getter @Setter private boolean isPublic = false;
    private final T config;

    public AbstractClient(T config) {
        this.config = config;
    }
    @Override
    public void start() throws IOException {
        if (status.compareAndSet(false, true)) {
            LOGGER.info("starting");
            doStart();
        } else {
            LOGGER.info("already start!");
        }
    }

    @Override
    public void shutdown() throws InterruptedException {
        if (status.compareAndSet(true, false)) {
            LOGGER.info("closing");
            doShutdown();
        } else {
            LOGGER.info("already shutdown!");
        }
    }

    protected abstract void doStart() throws IOException;
    protected abstract void doShutdown() throws InterruptedException;

    @Override
    public boolean isStarted() {
        return status.get() && isAlready();
    }



    @Override
    public T getConfig() {
        return config;
    }

    /**
     * 默认为准备好的。如果客户端连接初始化费时，需在客户端实现中设置
     * @return
     */
    protected boolean isAlready() {
        return true;
    }

    public static Client getClient(Config config) throws ClientException {
        if (config instanceof SourceConfig) {
            return PublicClientContext.INSTANCE.getSource(((SourceConfig) config).getSourceName());
        }
        try {
            switch (config.getConfigType()) {
                case KAFKA:
                    return new KafkaClient((KafkaConfig) config);
                case JDBC:
                    return new JDBCClient((JDBCConfig) config);
                case ZOOKEEPER:
                    return new ZookeeperClient((ZookeeperConfig) config);
                default:
                    throw  new ClientMatchException();
            }
        } catch (Exception e) {
            throw  new ClientException(e.getMessage());
        }
    }

    @Override
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public boolean isPublic() {
        return isPublic;
    }
}
