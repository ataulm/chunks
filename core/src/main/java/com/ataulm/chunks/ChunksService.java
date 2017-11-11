package com.ataulm.chunks;

import com.ataulm.Event;
import com.ataulm.EventProxyObserver;
import com.ataulm.EventRxFunctions;
import com.ataulm.Log;
import com.ataulm.Optional;
import com.ataulm.chunks.repository.ChunksRepository;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

class ChunksService {

    private final ChunksRepository chunksRepository;
    private final ChunksEditor chunksEditor;
    private final Clock clock;
    private final Log log;

    private final BehaviorSubject<Event<Chunks>> eventsSubject;

    private boolean currentlyFetching;

    ChunksService(ChunksRepository chunksRepository, ChunksEditor chunksEditor, Clock clock, Log log) {
        this.chunksRepository = chunksRepository;
        this.chunksEditor = chunksEditor;
        this.clock = clock;
        this.log = log;
        this.eventsSubject = BehaviorSubject.create(Event.<Chunks>idle());
    }

    Observable<Event<Chunks>> fetchEntries() {
        /*
         Every time this method is called, I want to shuffle stuff along
         Problem is that although we return the shuffled along version, eventsSubject isn't updated
         */
        return eventsSubject.doOnSubscribe(loadEventsIntoSubject())
                .map(shuffleAlong());
    }

    private Action0 loadEventsIntoSubject() {
        return new Action0() {
            @Override
            public void call() {
                if (currentlyFetching) {
                    return;
                }

                createFetchChunksObservable()
                        .flatMap(new Func1<Optional<Chunks>, Observable<Chunks>>() {
                            @Override
                            public Observable<Chunks> call(Optional<Chunks> chunks) {
                                if (chunks.isPresent()) {
                                    ChunkDate today = ChunkDate.create(clock);
                                    Chunks shuffledAlongChunks = chunksEditor.shuffleAlong(chunks.get(), today);
                                    return Observable.just(shuffledAlongChunks);
                                } else {
                                    return Observable.empty();
                                }
                            }
                        })
                        .doOnSubscribe(setCurrentlyLoadingFlag(true))
                        .doOnTerminate(setCurrentlyLoadingFlag(false))
                        .compose(EventRxFunctions.<Chunks>asEvents())
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

    private Observable<Optional<Chunks>> createFetchChunksObservable() {
        return Observable.fromCallable(
                new Callable<Optional<Chunks>>() {
                    @Override
                    public Optional<Chunks> call() throws Exception {
                        return chunksRepository.getChunks();
                    }
                }
        );
    }

    private Func1<Event<Chunks>, Event<Chunks>> shuffleAlong() {
        return new Func1<Event<Chunks>, Event<Chunks>>() {
            @Override
            public Event<Chunks> call(Event<Chunks> chunksEvent) {
                Optional<Chunks> chunks = chunksEvent.getData();
                if (chunks.isPresent()) {
                    ChunkDate today = ChunkDate.create(clock);
                    Chunks updatedChunks = chunksEditor.shuffleAlong(chunks.get(), today);
                    return chunksEvent.updateData(updatedChunks);
                } else {
                    return chunksEvent;
                }
            }

        };
    }

    void createEntry(Item item, Day day) {
        Chunks chunks = getInMemoryChunks().or(Chunks.empty(ChunkDate.create(clock)));
        Chunks updatedChunks = chunksEditor.add(chunks, day, item);
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

    void updateEntry(Item item) {
        Optional<Chunks> chunks = getInMemoryChunks();
        if (chunks.isPresent()) {
            Chunks updatedChunks = chunksEditor.update(chunks.get(), item);
            eventsSubject.onNext(Event.idle(updatedChunks));
        }
    }

    void editEntry(Item item) {
        Optional<Chunks> chunks = getInMemoryChunks();
        if (chunks.isPresent()) {
            Chunks updatedChunks = chunksEditor.edit(chunks.get(), item.id());
            eventsSubject.onNext(Event.idle(updatedChunks));
        }
    }

    void removeEntry(Item item) {
        Optional<Chunks> chunks = getInMemoryChunks();
        if (chunks.isPresent()) {
            Chunks updatedChunks = chunksEditor.remove(chunks.get(), item.id());
            eventsSubject.onNext(Event.idle(updatedChunks));
        }
    }

    void moveEntry(Item item, int newPosition) {
        Optional<Chunks> chunks = getInMemoryChunks();
        if (chunks.isPresent()) {
            Chunks updatedChunks = chunksEditor.move(chunks.get(), item, newPosition);
            eventsSubject.onNext(Event.idle(updatedChunks));
        }
    }

    private Optional<Chunks> getInMemoryChunks() {
        Event<Chunks> event = eventsSubject.getValue();
        return event.getData();
    }

    void persist() {
        Optional<Chunks> chunks = getInMemoryChunks();
        if (chunks.isPresent()) {
            chunksRepository.persist(chunks.get());
        }
    }
}
