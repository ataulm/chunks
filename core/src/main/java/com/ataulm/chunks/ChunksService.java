package com.ataulm.chunks;

import com.ataulm.Event;
import com.ataulm.EventProxyObserver;
import com.ataulm.EventRxFunctions;
import com.ataulm.Log;
import com.ataulm.chunks.repository.ChunksRepository;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.BehaviorSubject;

public class ChunksService {

    private final ChunksRepository chunksRepository;
    private final Log log;
    private final BehaviorSubject<Event<com.ataulm.chunks.Chunks>> eventsSubject;

    private boolean currentlyFetching;

    public ChunksService(ChunksRepository chunksRepository, Log log) {
        this.chunksRepository = chunksRepository;
        this.log = log;
        this.eventsSubject = BehaviorSubject.create(Event.<com.ataulm.chunks.Chunks>idle());
    }

    public Observable<Event<com.ataulm.chunks.Chunks>> fetchEntries() {
        return eventsSubject.doOnSubscribe(loadEventsIntoSubject());
    }

    private Action0 loadEventsIntoSubject() {
        return new Action0() {
            @Override
            public void call() {
                if (eventsAreBeingOrHaveAlreadyBeenLoaded()) {
                    return;
                }
                createFetchEntriesObservable()
                        .compose(EventRxFunctions.<com.ataulm.chunks.Chunks>asEvents())
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

    private boolean eventsAreBeingOrHaveAlreadyBeenLoaded() {
        return currentlyFetching || eventsSubject.getValue().getData().isPresent();
    }

    private Observable<com.ataulm.chunks.Chunks> createFetchEntriesObservable() {
        return Observable.fromCallable(
                new Callable<com.ataulm.chunks.Chunks>() {
                    @Override
                    public com.ataulm.chunks.Chunks call() throws Exception {
                        return chunksRepository.getChunks();
                    }
                }
        );
    }

    public void createEntry(Entry entry) {
        com.ataulm.chunks.Chunks chunks = getInMemoryChunksOrEmpty();
        com.ataulm.chunks.Chunks updatedChunks = chunks.add(entry);
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

    public void updateEntry(Entry entry) {
        com.ataulm.chunks.Chunks chunks = getInMemoryChunksOrEmpty();
        com.ataulm.chunks.Chunks updatedChunks = chunks.update(entry);
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

    public void removeEntry(Entry entry) {
        com.ataulm.chunks.Chunks chunks = getInMemoryChunksOrEmpty();
        com.ataulm.chunks.Chunks updatedChunks = chunks.remove(entry);
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

    private com.ataulm.chunks.Chunks getInMemoryChunksOrEmpty() {
        Event<com.ataulm.chunks.Chunks> event = eventsSubject.getValue();
        return event.getData().or(com.ataulm.chunks.Chunks.empty());
    }

    public void persist() {
        com.ataulm.chunks.Chunks chunks = getInMemoryChunksOrEmpty();
        chunksRepository.persist(chunks);
    }

}
