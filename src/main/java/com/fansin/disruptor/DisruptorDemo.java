package com.fansin.disruptor;

import cn.hutool.core.date.TimeInterval;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

/**
 * Created by zhaofeng on 17-5-18.
 */
public class DisruptorDemo {
    /*
     *
     * 1事件
     * 2工厂
     * 3处理者
     * 4生产者
     * */

    public static void main(String[] args) throws InterruptedException {
//        ExecutorService service = Executors.newCachedThreadPool();
        LongEventFactory factory = new LongEventFactory();
        int bufferSize = 1024;
        //默认为多个生产者!下面两个等效
        //Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory,bufferSize,Executors.defaultThreadFactory());
//        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory,bufferSize,Executors.defaultThreadFactory(), ProducerType.MULTI,new BlockingWaitStrategy());
        //单个生产者 策略速度 BlockingWaitStrategy < SleepingWaitStrategy < YieldingWaitStrategy(线程数<cpu线程数) < BusySpinWaitStrategy(线程数<cpu核数)
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize, Executors.defaultThreadFactory(),
                                                                  ProducerType.SINGLE, new BusySpinWaitStrategy());
        /*
         * cpu:双核双线程
         * 1000条
         * BlockingWaitStrategy:
         *   100s
         *   15%cpu
         * SleepingWaitStrategy(yield()和LockSupport.parkNano())
         *   100s
         *   30%cpu
         * YieldingWaitStrategy(yield() sys和cache占用极多,用户态效率不高,说明性能全部耗在了切换用户态和系统态上面去了.)
         *   100s
         *   100%cpu
         * BusySpinWaitStrategy(用户占用极高,正常负载,系统得到充分使用)
         *   100s
         *   100%cpu
         * */
        //TODO clearing 未参透
        //可以定义多个eventHandler
        disruptor.handleEventsWith(new LongEventCustomer())/*.then(new ClearingEventHandler())*/;
        disruptor.handleEventsWithWorkerPool(new LongEventWokerCustomer(), new LongEventWokerCustomer(),
                                             new LongEventWokerCustomer(), new LongEventWokerCustomer());

        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducer producer = new LongEventProducer(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        TimeInterval interval = new TimeInterval();
        for (long l = 0; l < 1000; l++) {
            byteBuffer.putLong(0, l);
            producer.onData(byteBuffer);
            Thread.sleep(100l);
        }
        System.out.println(interval.intervalSecond());
        disruptor.shutdown();
    }

}

@Setter
@Getter
class LongEvent {

    private long value;

}

class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}

//"消费者"
class LongEventCustomer implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        //TODO 业务处理
        System.out.println(
                Thread.currentThread().getId() + ":消费数据:longEvent = " + longEvent.getValue() + " " + l + " " + b);
    }
}

//可以使用workPool
class LongEventWokerCustomer implements WorkHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event) throws Exception {
        //TODO 业务处理
        System.out.println(Thread.currentThread().getId() + ":消费(work)数据:longEvent = " + event.getValue());
    }
}

class ObjectEvent<T> {

    T val;

    void clear() {
        val = null;
    }
}

class ClearingEventHandler<T> implements EventHandler<ObjectEvent<T>> {

    public void onEvent(ObjectEvent<T> event, long sequence, boolean endOfBatch) {
        // Failing to call clear here will result in the
        // object associated with the event to live until
        // it is overwritten once the ring buffer has wrapped
        // around to the beginning.
        System.out.println("clearing............");
        event.clear();
    }
}

class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR = new EventTranslatorOneArg<LongEvent, ByteBuffer>() {

        @Override
        public void translateTo(LongEvent longEvent, long l, ByteBuffer byteBuffer) {
            System.out.println("生产数据....");
            longEvent.setValue(byteBuffer.getLong(0));
        }
    };

    public void onData(ByteBuffer byteBuffer) {
        ringBuffer.publishEvent(TRANSLATOR, byteBuffer);
    }
}
