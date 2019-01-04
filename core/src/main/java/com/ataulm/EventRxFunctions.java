package com.ataulm;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public final class EventRxFunctions {

    private EventRxFunctions() {
        // utility class
    }

    /**
     * Starts with an empty Event of type LOADING, with each
     * onNext<T> being delivered as an Event of type IDLE; i.e.
     * no partial loading. Errors will be delivered as Event of
     * type ERROR.
     */
    public static <T> Observable.Transformer<T, Event<T>> asEvents() {
        return new Observable.Transformer<T, Event<T>>() {

            @Override
            public Observable<Event<T>> call(Observable<T> data) {
                return data
                        .map(EventRxFunctions.<T>asIdleEventWithData())
                        .startWith(EventRxFunctions.<T>loadingEvent())
                        .onErrorReturn(EventRxFunctions.<T>errorEvent());
            }

        };
    }

    private static <T> Event<T> loadingEvent() {
        return Event.Companion.loading();
    }

    private static <T> Func1<Throwable, Event<T>> errorEvent() {
        return new Func1<Throwable, Event<T>>() {

            @Override
            public Event<T> call(Throwable throwable) {
                return Event.Companion.error(throwable);
            }

        };
    }

    private static <T> Func1<T, Event<T>> asIdleEventWithData() {
        return new Func1<T, Event<T>>() {

            @Override
            public Event<T> call(T t) {
                return Event.Companion.idle(t);

            }

        };
    }

    public static <T> Observable.Operator<T, T> swallowOnCompleteEvents() {
        return new Observable.Operator<T, T>() {

            @Override
            public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
                return new Subscriber<T>() {

                    @Override
                    public void onCompleted() {
                        // Swallow
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(T edition) {
                        subscriber.onNext(edition);
                    }

                };
            }

        };
    }

}
