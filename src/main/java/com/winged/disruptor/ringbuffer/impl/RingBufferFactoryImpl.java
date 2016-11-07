package com.winged.disruptor.ringbuffer.impl;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.winged.disruptor.ringbuffer.EventHandler;
import com.winged.disruptor.ringbuffer.RingBufferFactory;
import com.winged.disruptor.ringbuffer.spring.DisposableBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;

/**
 * Created by wingedFish on 2016/9/1.
 */
public class RingBufferFactoryImpl implements RingBufferFactory, DisposableBean {
    private final static Logger log = LogManager.getLogger(RingBufferFactoryImpl.class);
    //每个TaskType的消费者数量
    private static final int CONSUMER_COUNT = 3;
    //RingBuffer大小,必须是2的倍数,进行移位操作
    private static final int BUFFER_SIZE = 1024 * 16;

    private static final ConcurrentHashMap<String, RingBuffer> ringBufferConcurrentHashMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Disruptor> disruptorConcurrentHashMap = new ConcurrentHashMap<>();

    private final EventFactory<StringEvent> stringEventFactory = () -> new StringEvent();

    private EventHandler eventHandler;

    /**
     * Gets ring buffer.
     *
     * @param type the type
     * @return the ring buffer
     */
    public RingBuffer<StringEvent> getRingBuffer(String type) {

        RingBuffer<StringEvent> disruptor = ringBufferConcurrentHashMap.get(type);
        if (disruptor == null) {
            //并发安全交由ConcurrentHashMap管理
            ringBufferConcurrentHashMap.putIfAbsent(type, createRingBuffer(type));
        }
        return ringBufferConcurrentHashMap.get(type);

    }

    private RingBuffer<StringEvent> createRingBuffer(String type) {

        // Construct the Disruptor
        Disruptor<StringEvent> disruptor = new Disruptor<StringEvent>(stringEventFactory
                , BUFFER_SIZE
//                , new DisruptorThreadFactory(type)
                , new RingBufferFactory()
                , ProducerType.MULTI    //多生产者
                , new YieldingWaitStrategy());//在多次循环尝试不成功后，选择让出CPU，等待下次调。平衡了延迟和CPU资源占用
        //get EventHandler
        EventHandler eventHandler = getEventHandler();
        //set handler
        disruptor.handleEventsWithWorkerPool(getWorkHandlers(eventHandler));
        // Start the Disruptor, starts all threads running
        disruptor.start();
        //putIfAbsent
        disruptorConcurrentHashMap.putIfAbsent(type, disruptor);
        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<StringEvent> ringBuffer = disruptor.getRingBuffer();

        return ringBuffer;
    }

    private EventHandler getEventHandler() {
        if (eventHandler == null) {
            log.error("RingBufferFactory getEventHandler error !");
        }
        return eventHandler;
    }

    private WorkHandler[] getWorkHandlers(EventHandler eventHandler) {
        WorkHandler<StringEvent>[] workHandlers = new WorkHandler[CONSUMER_COUNT];

        for (int i = 0; i < CONSUMER_COUNT; i++) {
            workHandlers[i] = (event) -> eventHandler.doHandle(event);
        }
        return workHandlers;
    }

    @Override
    public void destroy() throws Exception {
        log.info("shutdown disruptor...");
        disruptorConcurrentHashMap.values().stream()
                .forEach(disruptor -> disruptor.shutdown());
    }

    /**
     * see https://github.com/wingedFish/helloworld/blob/master/afengzi-concurrent/src/main/java/com/afengzi/concurrent/factory/AfThreadFactory.java
     */
    static class RingBufferFactory implements ThreadFactory{

        @Override
        public Thread newThread(Runnable r) {
            return null;
        }
    }
}
