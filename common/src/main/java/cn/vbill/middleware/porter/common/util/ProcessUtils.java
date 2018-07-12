/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月18日 10:25
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.common.util;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月18日 10:25
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月18日 10:25
 */
public class ProcessUtils {
    /**
     * 保持线程不退出
     */
    public static void keepRunning() {
        //默认不是守护进程
        Thread keepingThread = new DefaultNamedThreadFactory("ProcessKeepRunning").newThread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        keepingThread.start();
    }
}
