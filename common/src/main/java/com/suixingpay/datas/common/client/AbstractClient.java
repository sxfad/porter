/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:06
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client;

import com.alibaba.fastjson.annotation.JSONField;
import com.suixingpay.datas.common.client.impl.*;
import com.suixingpay.datas.common.config.PluginServiceConfig;
import com.suixingpay.datas.common.config.source.*;
import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ClientMatchException;
import com.suixingpay.datas.common.util.compile.JavaFileCompiler;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
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
    //插件服务配置文件
    @JSONField(serialize = false, deserialize = false)
    private static final List<PluginServiceClient> PLUGIN_SERVICE_CLIENTS =
            SpringFactoriesLoader.loadFactories(PluginServiceClient.class, JavaFileCompiler.getInstance());

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
    public void shutdown() throws Exception {
        if (status.compareAndSet(true, false)) {
            LOGGER.info("closing");
            doShutdown();
        } else {
            LOGGER.info("already shutdown!");
        }
    }

    protected abstract void doStart() throws Exception;
    protected abstract void doShutdown() throws Exception;

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
        if (config instanceof KuduConfig) {
            return new KUDUClient((KuduConfig) config);
        }

        //自定义插件配置文件
        if (config instanceof PluginServiceConfig) {
            PluginServiceConfig  pluginConfig = (PluginServiceConfig) config;
            //如果仍不能匹配客户端，尝试从插件服务SPI加载
            for (PluginServiceClient c : PLUGIN_SERVICE_CLIENTS) {
                if (c.isMatch(pluginConfig.getTargetName())) {
                    try {
                        return (Client) c.getClass().getConstructor(config.getClass()).newInstance(config);
                    } catch (Throwable e) {
                        continue;
                    }
                }
            }
        }
        throw  new ClientMatchException();
    }

    @Override
    public String getClientInfo() {
        return null != config && null != config.getProperties() ? config.getProperties().toString() : StringUtils.EMPTY;
    }
}
