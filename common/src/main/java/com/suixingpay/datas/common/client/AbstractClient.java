/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:06
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client;

import com.suixingpay.datas.common.client.impl.CanalClient;
import com.suixingpay.datas.common.config.source.CanalConfig;
import com.suixingpay.datas.common.client.impl.EmailClient;
import com.suixingpay.datas.common.client.impl.JDBCClient;
import com.suixingpay.datas.common.client.impl.KafkaClient;
import com.suixingpay.datas.common.client.impl.ZookeeperClient;
import com.suixingpay.datas.common.config.source.EmailConfig;
import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.config.source.JDBCConfig;
import com.suixingpay.datas.common.config.source.KafkaConfig;
import com.suixingpay.datas.common.config.source.NameSourceConfig;
import com.suixingpay.datas.common.config.source.ZookeeperConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ClientMatchException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @param <T>
 * @date: 2018年02月02日 14:06
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:06
 */
public abstract class AbstractClient<T extends SourceConfig> implements Client {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final AtomicBoolean status = new AtomicBoolean(false);
    @Getter @Setter private boolean isPublic = false;
    private final T config;

    public AbstractClient(T config) {
        this.config = config;
    }
    @Override
    public void start() throws Exception {
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

    protected abstract void doStart() throws Exception;
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

    public static Client getClient(SourceConfig config) throws ClientException {
        if (config instanceof NameSourceConfig) {
            return PublicClientContext.INSTANCE.getSource(((NameSourceConfig) config).getSourceName());
        }
        if (config instanceof JDBCConfig) {
            return new JDBCClient((JDBCConfig) config);
        }
        if (config instanceof KafkaConfig) {
            return new KafkaClient((KafkaConfig) config);
        }
        if (config instanceof ZookeeperConfig) {
            return new ZookeeperClient((ZookeeperConfig) config);
        }
        if (config instanceof EmailConfig) {
            return new EmailClient((EmailConfig) config);
        }
        if (config instanceof CanalConfig) {
            return new CanalClient((CanalConfig) config);
        }
        throw  new ClientMatchException();
    }
}
