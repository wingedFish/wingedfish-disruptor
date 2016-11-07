package com.winged.disruptor.ringbuffer.spring;

/**
 * Created by wingedfish on 2016/11/7.
 *
 */
public interface DisposableBean {
    /**
     *
     * @throws Exception
     */
    public void destroy() throws Exception;
}
