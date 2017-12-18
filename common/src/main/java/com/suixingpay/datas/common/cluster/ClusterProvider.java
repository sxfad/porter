package com.suixingpay.datas.common.cluster;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.function.Consumer;

/**
 * 集群提供者
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 16:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 16:32
 */
public abstract class ClusterProvider {
    protected abstract void addListener(ClusterListener listener);
    protected abstract void initialize(ClusterDriver driver);
    protected abstract void start();
    protected final void afterInitialize(){
        //获取ClusterListener SPI
        ServiceLoader<ClusterListener> listeners = ServiceLoader.load(ClusterListener.class);
        //添加SPI到监听器
        listeners.forEach(new Consumer<ClusterListener>() {
            @Override
            public void accept(ClusterListener listener) {
                addListener(listener);
            }
        });
    }

    protected abstract void stop();

    public static final void load(ClusterDriver driver) {
        //集群组件初始化, 一般情况下只有一个.
        final ServiceLoader<ClusterProvider> providers = ServiceLoader.load(ClusterProvider.class);
        providers.forEach(new Consumer<ClusterProvider>() {
            @Override
            public void accept(ClusterProvider clusterProvider) {
                clusterProvider.initialize(driver);
                clusterProvider.start();
                //进程退出Hook
                Runtime.getRuntime().addShutdownHook(new Thread("datas-ClusterProviderShutdownHook-"+clusterProvider.hashCode()){
                    @Override
                    public void run() {
                        clusterProvider.stop();
                    }
                });
            }
        });
    }
}
