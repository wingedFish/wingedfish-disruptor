package com.winged.disruptor.ringbuffer.impl;


import com.winged.disruptor.ringbuffer.TryableSemaphore;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lixiuhai on 2016/9/1.
 */
public class TryableSemaphoreActual implements TryableSemaphore {
    protected final int numberOfPermits;
    private final AtomicInteger count = new AtomicInteger(0);


    /**
     * Instantiates a new Tryable semaphore actual.
     * use CAS instead of lock
     * @param numberOfPermits the number of permits
     */
    public TryableSemaphoreActual(int numberOfPermits) {
        this.numberOfPermits = numberOfPermits;
    }

    @Override
    public boolean tryAcquire() {
        int currentCount = count.incrementAndGet();
        if (currentCount > numberOfPermits) {
            count.decrementAndGet();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void release() {
        count.decrementAndGet();
    }

    @Override
    public int getNumberOfPermitsUsed() {
        return count.get();
    }
}
