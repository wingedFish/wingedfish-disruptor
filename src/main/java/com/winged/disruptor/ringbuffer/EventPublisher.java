package com.winged.disruptor.ringbuffer;

/**
 * Created by lixiuhai on 2016/9/1.
 */
public interface EventPublisher {

    /**
     * 发布消息
     * @param message
     * @param isolateType
     */
    public void publish(String message, String isolateType);
}
