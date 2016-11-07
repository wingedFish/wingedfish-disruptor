package com.winged.disruptor.ringbuffer;

/**
 * Created by wingedFish on 2016/9/1.
 */
public interface TryableSemaphore {
    /**
     * Use like this:
     * <pre>
     * if (s.tryAcquire()) {
     * try {
     * // do work that is protected by 's'
     * } finally {
     * s.release();
     * }
     * }
     * </pre>
     *
     * @return boolean boolean
     */
    boolean tryAcquire();

    /**
     * ONLY call release if tryAcquire returned true.
     * <pre>
     * if (s.tryAcquire()) {
     * try {
     * // do work that is protected by 's'
     * } finally {
     * s.release();
     * }
     * }
     * </pre>
     */
    void release();

    /**
     * Gets number of permits used.
     *
     * @return the number of permits used
     */
    int getNumberOfPermitsUsed();
}
