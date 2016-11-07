package com.winged.disruptor.ringbuffer.spring;

/**
 * Created by wingedFish on 2016/11/7.
 *
 */
public interface DisposableBean {
    /**
     *
     * @throws Exception
     */
    public void destroy() throws Exception;
}
