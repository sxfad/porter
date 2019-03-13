/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月11日 10:34
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.boot.helper;

import cn.vbill.middleware.porter.common.util.DefaultNamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * GC工具
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月11日 10:34
 * @version: V1.0
 * @review: zkevin/2019年03月11日 10:34
 */
public class GCHelper {
    /**
     * 内存定时清理线程
     */
    public static void run(Integer delayOfMinutes) {
        //最短5分钟执行一次
        delayOfMinutes = null == delayOfMinutes || delayOfMinutes < 5 ? 5 : delayOfMinutes;
        Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("instance-memory-gc"))
                .schedule(() -> System.gc(), delayOfMinutes, TimeUnit.MINUTES);
    }
}
