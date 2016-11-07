package com.winged.disruptor.ringbuffer;

import com.lmax.disruptor.RingBuffer;
import com.winged.disruptor.ringbuffer.impl.StringEvent;

/**
 * Created by wingedFish on 2016/9/18.
 */
public interface RingBufferFactory {
    /**
     * 根据类型获取Ring Buffer
     * @param type
     * @return
     */
     RingBuffer<StringEvent> getRingBuffer(String type);
}
