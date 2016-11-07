package com.winged.disruptor.ringbuffer;


import com.winged.disruptor.ringbuffer.impl.StringEvent;

/**
 * Created by lixiuhai on 2016/9/1.
 */
public interface EventHandler {

    /**
     * Do handle.
     *
     * @param event the event
     */
    public void doHandle(StringEvent event);
}
