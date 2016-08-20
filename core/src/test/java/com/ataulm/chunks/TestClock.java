package com.ataulm.chunks;

class TestClock implements Clock {

    private final long millis;

    TestClock(long millis) {
        this.millis = millis;
    }

    @Override
    public long getCurrentTime() {
        return millis;
    }

}
