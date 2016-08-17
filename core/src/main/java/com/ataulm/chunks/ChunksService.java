package com.ataulm.chunks;

import com.ataulm.Event;
import com.ataulm.EventProxyObserver;
import com.ataulm.EventRxFunctions;
import com.ataulm.Log;
import com.ataulm.Optional;
import com.ataulm.chunks.repository.ChunksRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
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
        return eventsSubject.doOnSubscribe(loadEventsIntoSubject()).map(new Func1<Event<Chunks>, Event<Chunks>>() {
            @Override
            public Event<Chunks> call(Event<Chunks> chunksEvent) {
                Optional<Chunks> data = chunksEvent.getData();
                if (data.isPresent() && shouldShuffleAlong(data.get())) {
                    return chunksEvent.updateData(data.get().shuffleAlong());
                } else {
                    return chunksEvent;
                }
            }

            private boolean shouldShuffleAlong(Chunks chunks) {
                return !chunks.isEmpty() && todayIsDifferentFromLastShuffledDay(chunks);
            }
        });
    }

    private Action0 loadEventsIntoSubject() {
        return new Action0() {
            @Override
            public void call() {
                if (eventsAreBeingOrHaveAlreadyBeenLoaded()) {
                    return;
                }
                createFetchChunksObservable()
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

    private boolean eventsAreBeingOrHaveAlreadyBeenLoaded() {
        return currentlyFetching || eventsSubject.getValue().getData().isPresent();
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

    private boolean todayIsDifferentFromLastShuffledDay(Chunks chunks) {
        long timestamp = Long.parseLong(chunks.lastShuffledTimestamp());
        Calendar lastShuffled = Calendar.getInstance();
        lastShuffled.setTime(new Date(timestamp));

        Calendar now = Calendar.getInstance();

        boolean differentDay = lastShuffled.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR);
        boolean differentYear = lastShuffled.get(Calendar.YEAR) != now.get(Calendar.YEAR);
        return differentDay || differentYear;
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
        return event.getData().or(Chunks.empty());
    }

    public void persist() {
        Chunks chunks = getInMemoryChunksOrEmpty();
        chunksRepository.persist(chunks);
    }

}
