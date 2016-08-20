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

public class ChunksService {

    private final ChunksRepository chunksRepository;
    private final Log log;
    private final BehaviorSubject<Event<Chunks>> eventsSubject;
    private final SystemClock clock = new SystemClock();

    private boolean currentlyFetching;

    public ChunksService(ChunksRepository chunksRepository, Log log) {
        this.chunksRepository = chunksRepository;
        this.log = log;
        this.eventsSubject = BehaviorSubject.create(Event.<Chunks>idle());
    }

    public Observable<Event<Chunks>> fetchEntries() {
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
                        .map(new Func1<Chunks, Chunks>() {
                            @Override
                            public Chunks call(Chunks chunks) {
                                ChunkDate today = ChunkDate.create(clock);
                                return chunks.shuffleAlong(today);
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

    private Observable<Chunks> createFetchChunksObservable() {
        return Observable.fromCallable(
                new Callable<Chunks>() {
                    @Override
                    public Chunks call() throws Exception {
                        return chunksRepository.getChunks();
                    }
                }
        );
    }

    private Func1<Event<Chunks>, Event<Chunks>> shuffleAlong() {
        return new Func1<Event<Chunks>, Event<Chunks>>() {
            @Override
            public Event<Chunks> call(Event<Chunks> chunksEvent) {
                log.debug("!!!", "shuffleAlong()");
                Optional<Chunks> data = chunksEvent.getData();
                if (data.isPresent()) {
                    ChunkDate today = ChunkDate.create(clock);
                    Chunks updatedChunks = data.get().shuffleAlong(today);
                    return chunksEvent.updateData(updatedChunks);
                } else {
                    return chunksEvent;
                }
            }

        };
    }

    public void createEntry(Entry entry, Day day) {
        Chunks chunks = getInMemoryChunksOrEmpty();
        Chunks updatedChunks = chunks.add(entry, day);
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

    public void updateEntry(Entry entry) {
        Chunks chunks = getInMemoryChunksOrEmpty();
        Chunks updatedChunks = chunks.update(entry);
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

    public void removeEntry(Entry entry) {
        Chunks chunks = getInMemoryChunksOrEmpty();
        Chunks updatedChunks = chunks.remove(entry.id());
        eventsSubject.onNext(Event.idle(updatedChunks));
    }

    private Chunks getInMemoryChunksOrEmpty() {
        Event<Chunks> event = eventsSubject.getValue();
        return event.getData().or(Chunks.empty(ChunkDate.create(clock)));
    }

    public void persist() {
        Chunks chunks = getInMemoryChunksOrEmpty();
        chunksRepository.persist(chunks);
    }

}
