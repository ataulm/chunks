package com.ataulm;

import rx.Observer;

public class LoggingObserver<T> implements Observer<T> {

    private final Log log;

    public LoggingObserver(Log log) {
        this.log = log;
    }

    @Override
    public void onCompleted() {
        log.verbose(getTag(), "onCompleted()");
    }

    @Override
    public void onError(Throwable e) {
        log.error(getTag(), "onError(): " + e.getMessage(), e);
    }

    @Override
    public void onNext(T t) {
        log.verbose(getTag(), "onNext(t): " + t);
    }

    private String getTag() {
        return "!!! " + getClass().getCanonicalName();
    }

}
