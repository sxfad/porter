/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster;

import com.suixingpay.datas.common.cluster.command.ClusterCommand;
import com.suixingpay.datas.common.task.TaskEventListener;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.function.Consumer;

/**
 * 集群提供者
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:32
 */
public abstract class ClusterProvider {
    private static ClusterProvider CLUSTER_PROVIDER;
    protected abstract void addListener(ClusterListener listener);
    public abstract void addTaskEventListener(TaskEventListener listener);
    public abstract void removeTaskEventListener(TaskEventListener listener);
    protected abstract void doInitialize(ClusterDriver driver);
    protected abstract void start();
    protected abstract void stop();
    protected abstract void distributeCommand(ClusterCommand command) throws Exception;
    private void initialize(ClusterDriver driver) {
        doInitialize(driver);
        afterInitialize();
    }
    private void afterInitialize(){
        //通过spring框架的SPI loader加载服务
        List<ClusterListener> listeners = SpringFactoriesLoader.loadFactories(ClusterListener.class, null);
        //添加SPI到监听器
        listeners.forEach(new Consumer<ClusterListener>() {
            @Override
            public void accept(ClusterListener listener) {
                addListener(listener);
            }
        });
    }


    public static final void load(ClusterDriver driver) {
        //集群组件初始化
        List<String> providers = SpringFactoriesLoader.loadFactoryNames(ClusterProvider.class, null);
        for (String providerName : providers) {
            try {
                ClusterProvider tempProvider = null;
                if (driver.getType().matches(providerName)) {
                    Class<ClusterProvider> clazzProvider = (Class<ClusterProvider>) Class.forName(providerName);
                    tempProvider = clazzProvider.getDeclaredConstructor().newInstance();
                }
                if (null != tempProvider){
                    tempProvider.initialize(driver);
                    tempProvider.start();
                    CLUSTER_PROVIDER = tempProvider;
                    break;
                }
            } catch (Exception e) {
                continue;
            }
        }
    }
    public static final void unload() {
        //退出群聊需在业务代码执行之后才能执行,进程退出Hook
        CLUSTER_PROVIDER.stop();
    }
    public static final void sendCommand (ClusterCommand command) throws Exception {
        CLUSTER_PROVIDER.distributeCommand(command);
    }
    public static final void sendCommand (List<ClusterCommand> command) throws Exception {
        for (ClusterCommand c : command) {
            sendCommand(c);
        }
    }
    public static final void addTaskListener (TaskEventListener listener){
        CLUSTER_PROVIDER.addTaskEventListener(listener);
    }
    public static final void removeTaskListener (TaskEventListener listener){
        CLUSTER_PROVIDER.removeTaskEventListener(listener);
    }
}
