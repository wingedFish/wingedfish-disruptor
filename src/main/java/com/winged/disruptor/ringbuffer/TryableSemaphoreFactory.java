package com.winged.disruptor.ringbuffer;


import com.winged.disruptor.ringbuffer.impl.TryableSemaphoreActual;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lixiuhai on 2016/9/1.
 */
public class TryableSemaphoreFactory {
    private static final ConcurrentHashMap<String, TryableSemaphore> semaphoreMap = new ConcurrentHashMap<>();
    private static final int PERMIT = 5;

    /**
     * Gets semaphore.
     *
     * @param type the type
     * @return the semaphore
     */
    public static TryableSemaphore getSemaphore(String type) {
        TryableSemaphore semaphore = semaphoreMap.get(type);
        if (semaphore == null) {
            semaphoreMap.putIfAbsent(type, new TryableSemaphoreActual(PERMIT));
        }
        return semaphoreMap.get(type);
    }
}
