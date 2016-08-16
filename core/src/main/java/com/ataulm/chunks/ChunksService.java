package com.ataulm.chunks;

import com.ataulm.Event;
import com.ataulm.EventProxyObserver;
import com.ataulm.EventRxFunctions;
import com.ataulm.Log;
import com.ataulm.chunks.repository.ChunksRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
                        .map(new Func1<Chunks, Chunks>() {
                            @Override
                            public Chunks call(Chunks chunks) {
                                return shuffleDaysAlong(chunks);
                            }
                        })
                        .compose(EventRxFunctions.<Chunks>asEvents())
                        .doOnSubscribe(setCurrentlyLoadingFlag(true))
                        .doOnTerminate(setCurrentlyLoadingFlag(false))
                        .subscribe(new EventProxyObserver<>(eventsSubject, log));
            }
        };
    }

    private Chunks shuffleDaysAlong(Chunks chunks) {
        if (chunks.isEmpty() || todayIsStillLastShuffledDay(chunks)) {
            return chunks;
        }
        Chunks updatedChunks = moveCompletedItemsFromTodayToYesterday(chunks);
        return moveAllItemsFromTomorrowToToday(updatedChunks);
    }

    private Chunks moveCompletedItemsFromTodayToYesterday(Chunks chunks) {
        List<Entry> completedEntries = new ArrayList<>();
        for (Entry entry : chunks.today()) {
            if (entry.completedTimestamp().isPresent()) {
                completedEntries.add(entry);
            }
        }
        return chunks.transition(completedEntries, Day.YESTERDAY);
    }

    private Chunks moveAllItemsFromTomorrowToToday(Chunks chunks) {
        return chunks.transition(chunks.tomorrow().values(), Day.TODAY);
    }

    private boolean todayIsStillLastShuffledDay(Chunks chunks) {
        long timestamp = Long.parseLong(chunks.lastShuffledTimestamp());
        Calendar lastShuffled = Calendar.getInstance();
        lastShuffled.setTime(new Date(timestamp));

        Calendar now = Calendar.getInstance();

        return lastShuffled.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
                && lastShuffled.get(Calendar.YEAR) == now.get(Calendar.YEAR);
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
