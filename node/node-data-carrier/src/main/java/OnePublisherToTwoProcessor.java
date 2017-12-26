/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 13:35
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 13:35
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 13:35
 */
public class OnePublisherToTwoProcessor {
    public static void main(String[] args) throws Exception {

        //1.配置并获得Disruptor
        ExecutorService  executor = Executors.newCachedThreadPool();
        LongEventFactory factory = new LongEventFactory();
        // 设置RingBuffer大小, 需为2的N次方(能将求模运算转为位运算提高效率 ), 否则影响性能
        int ringBufferSize = 1024 * 1024;

        //创建disruptor, 泛型参数:传递的事件的类型
        // 第一个参数: 产生Event的工厂类, Event封装生成-消费的数据
        // 第二个参数: RingBuffer的缓冲区大小
        // 第三个参数: 线程池
        // 第四个参数: SINGLE单个生产者, MULTI多个生产者
        // 第五个参数: WaitStrategy 当消费者阻塞在SequenceBarrier上, 消费者如何等待的策略.
        //BlockingWaitStrategy 使用锁和条件变量, 效率较低, 但CPU的消耗最小, 在不同部署环境下性能表现比较一致
        //SleepingWaitStrategy 多次循环尝试不成功后, 让出CPU, 等待下次调度; 多次调度后仍不成功, 睡眠纳秒级别的时间再尝试. 平衡了延迟和CPU资源占用, 但延迟不均匀.
        //YieldingWaitStrategy 多次循环尝试不成功后, 让出CPU, 等待下次调度. 平衡了延迟和CPU资源占用, 延迟也比较均匀.
        //BusySpinWaitStrategy 自旋等待，类似自旋锁. 低延迟但同时对CPU资源的占用也多.
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, ringBufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
        // 注册事件消费处理器, 也即消费者. 可传入多个EventHandler ...
        disruptor.handleEventsWith(new LongEventHandler(1,2),new LongEventHandler(0,2));

        // 启动
        disruptor.start();

        //2.将数据装入RingBuffer
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        // 创建生产者, 以下方式一使用原始api, 方式二使用新API
        //LongEventProducer producer = new LongEventProducer(ringBuffer);
        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);

        ByteBuffer byteBuffer = ByteBuffer.allocate(8); // 这里只是笔者实验, 不是必须要用ByteBuffer保存long数据
        for(int i = 0; i < 100; ++i){
            byteBuffer.putLong(0, i);
            producer.produceData(byteBuffer);
        }

        disruptor.shutdown(); //关闭 disruptor  阻塞直至所有事件都得到处理
        executor.shutdown(); // 需关闭 disruptor使用的线程池, 上一步disruptor关闭时不会连带关闭线程池
    }


    // Event封装要传递的数据
    public static class LongEvent {
        private long value;
        public long getValue() {
            return value;
        }
        public void setValue(long value) {
            this.value = value;
        }
    }

    // 产生Event的工厂
    public static class LongEventFactory implements EventFactory {
        @Override
        public Object newInstance() {
            return new LongEvent();
        }
    }

    public static class LongEventHandler implements EventHandler<LongEvent>  {
        private int index;
        private int count;
        public LongEventHandler(int i, int c) {
            this.index = i;
            this.count = c;
        }
        // 消费逻辑
        @Override
        public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
            if (l % count ==index) {
                System.out.println(l+":"+longEvent.getValue());
            }
        }
    }

    //生产者实现一
    public class LongEventProducer {
        // 生产者持有RingBuffer的引用
        private final RingBuffer<LongEvent> ringBuffer;

        public LongEventProducer(RingBuffer<LongEvent> ringBuffer){
            this.ringBuffer = ringBuffer;
        }

        public void produceData(ByteBuffer bb){
            // 获得下一个Event槽的下标
            long sequence = ringBuffer.next();
            try {
                // 给Event填充数据
                LongEvent event = ringBuffer.get(sequence);
                event.setValue(bb.getLong(0));
            } finally {
                // 发布Event, 激活观察者去消费, 将sequence传递给该消费者
                //publish应该放在 finally块中以确保一定会被调用->如果某个事件槽被获取但未提交, 将会堵塞后续的publish动作。
                ringBuffer.publish(sequence);
            }
        }
    }

    //生产者实现二
    public static class LongEventProducerWithTranslator {

        // 使用EventTranslator, 封装 获取Event的过程
        private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
            @Override
            public void translateTo(LongEvent event, long sequeue, ByteBuffer buffer) {
                event.setValue(buffer.getLong(0));
            }
        };

        private final RingBuffer<LongEvent> ringBuffer;

        public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        public void produceData(ByteBuffer buffer){
            // 发布
            ringBuffer.publishEvent(TRANSLATOR, buffer);
        }
    }
}