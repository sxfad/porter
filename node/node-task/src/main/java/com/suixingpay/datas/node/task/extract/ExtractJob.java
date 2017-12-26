package com.suixingpay.datas.node.task.extract;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import com.suixingpay.datas.node.core.event.MessageEvent;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.concurrent.*;

/**
 * 完成事件的进一步转换、过滤。多线程执行
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:20
 */
public class ExtractJob extends AbstractStageJob {
    private final TaskWork work;
    private final ExecutorService executorService;
    public ExtractJob(TaskWork work) {
        super(work.getBasicThreadName());
        this.work = work;
        //线程阻塞时，在调用者线程中执行
        executorService = new ThreadPoolExecutor(LOGIC_THREAD_SIZE, LOGIC_THREAD_SIZE,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    protected void doStop() {
        executorService.shutdown();
    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void loopLogic() {
        //只要队列有消息，持续读取
        Pair<Long, List<MessageEvent>> events = null;
        do {
            try {
                events = work.waitEvent(StageType.SELECT);
                final Pair<Long, List<MessageEvent>> inThreadEvents = events;
                Future<Boolean> future = executorService.submit(new Callable<Boolean>(){
                    @Override
                    public Boolean call() throws Exception {
                        Long a = inThreadEvents.getKey();
                        System.out.println(a);
                        return null;
                    }
                });
            } catch (Exception e) {
                LOGGER.error("extract MessageEvent error!", e);
            }
        } while (null != events && ! events.getRight().isEmpty());
    }

    @Override
    public <T> Pair<Long, List<T>> output() {
        return null;
    }
}