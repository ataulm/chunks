package com.ataulm.basic;

import com.ataulm.Event;
import com.ataulm.EventProxyObserver;
import com.ataulm.EventRxFunctions;
import com.ataulm.Log;
import com.ataulm.basic.repository.ChunksRepository;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.BehaviorSubject;

public class ChunksService {

    private final ChunksRepository chunksRepository;
    private final Log log;
    private final BehaviorSubject<Event<Chunks>> eventsSubject;

    private boolean currentlyFetching;

    public ChunksService(ChunksRepository chunksRepository, Log log) {
        this.chunksRepository = chunksRepository;
        this.log = log;
        this.eventsSubject = BehaviorSubject.create(Event.<Chunks>idle());
    }

    public Observable<Event<Chunks>> fetchEntries() {
        return eventsSubject.doOnSubscribe(loadEventsIntoSubject());
    }

    private Action0 loadEventsIntoSubject() {
        return new Action0() {
            @Override
            public void call() {
                if (eventsHaveBeenLoadedOrAreBeingLoading()) {
                    return;
                }
                createFetchEntriesObservable()
                        .compose(EventRxFunctions.<Chunks>asEvents())
                        .doOnSubscribe(setCurrentlyLoadingFlag(true))
                        .doOnTerminate(setCurrentlyLoadingFlag(false))
                        .subscribe(new EventProxyObserver<>(eventsSubject, log));
            }
        };
    }

    private Action0 setCurrentlyLoadingFlag(final boolean currentlyFetching) {
        return new Action0() {
            @Override
            public void call() {
                ChunksService.this.currentlyFetching = currentlyFetching;
            }
        };
    }

    private boolean eventsHaveBeenLoadedOrAreBeingLoading() {
        return eventsSubject.hasValue() || currentlyFetching;
    }

    private Observable<Chunks> createFetchEntriesObservable() {
        return Observable.fromCallable(
                new Callable<Chunks>() {
                    @Override
                    public Chunks call() throws Exception {
                        return chunksRepository.getChunks();
                    }
                }
        );
    }

    public void createEntry(Entry entry) {
        Event<Chunks> event = eventsSubject.getValue();
        Chunks chunks = event.getData().or(Chunks.empty());
        Chunks updatedChunks = chunks.add(entry);
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

    public void updateEntry(Entry entry) {
        Event<Chunks> event = eventsSubject.getValue();
        Chunks chunks = event.getData().or(Chunks.empty());
        Chunks updatedChunks = chunks.update(entry);
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

    public void removeEntry(Entry entry) {
        Event<Chunks> event = eventsSubject.getValue();
        Chunks chunks = event.getData().or(Chunks.empty());
        Chunks updatedChunks = chunks.remove(entry);
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

}
