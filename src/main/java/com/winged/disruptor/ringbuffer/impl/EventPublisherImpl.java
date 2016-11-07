package com.winged.disruptor.ringbuffer.impl;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.winged.disruptor.ringbuffer.EventPublisher;
import com.winged.disruptor.ringbuffer.RingBufferFactory;

/**
 * Created by lixiuhai on 2016/9/1.
 */
public class EventPublisherImpl implements EventPublisher {

    private static final EventTranslatorOneArg<StringEvent, String> translator = (event, sequence, value) -> event.setValue(value);
    private RingBufferFactory ringBufferFactory;

    public void publish(String message,String isolateType) {
        RingBuffer<StringEvent> ringBuffer = ringBufferFactory.getRingBuffer(isolateType);
        ringBuffer.publishEvent(translator,message);
    }

}
