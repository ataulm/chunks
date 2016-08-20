package com.ataulm.chunks;

public class SystemClock implements Clock {

    private static final SystemClock INSTANCE = new SystemClock();

    public static SystemClock get() {
        return INSTANCE;
    }

    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
