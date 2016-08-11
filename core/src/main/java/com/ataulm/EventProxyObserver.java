package com.ataulm;

import rx.Observer;

public class EventProxyObserver<T> extends LoggingObserver<Event<T>> {

    private final Observer<Event<T>> observer;

    public EventProxyObserver(Observer<Event<T>> observer, Log log) {
        super(log);
        this.observer = observer;
    }

    @Override
    public void onNext(Event<T> event) {
        super.onNext(event);
        observer.onNext(event);
    }

}
