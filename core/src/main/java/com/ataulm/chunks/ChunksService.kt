package com.ataulm.chunks

import com.ataulm.Event
import com.ataulm.EventProxyObserver
import com.ataulm.EventRxFunctions
import com.ataulm.Log
import com.ataulm.Optional
import com.ataulm.chunks.repository.ChunksRepository
import rx.Observable
import rx.functions.Action0
import rx.functions.Func1
import rx.subjects.BehaviorSubject

internal class ChunksService(private val chunksRepository: ChunksRepository, private val chunksEditor: ChunksEditor, private val clock: Clock, private val log: Log) {

    private val eventsSubject: BehaviorSubject<Event<Chunks>>

    private var currentlyFetching: Boolean = false

    private val inMemoryChunks: Optional<Chunks>
        get() {
            val event = eventsSubject.value
            return event.data
        }

    init {
        this.eventsSubject = BehaviorSubject.create(Event.idle())
    }

    fun fetchEntries(): Observable<Event<Chunks>> {
        /*
         Every time this method is called, I want to shuffle stuff along
         Problem is that although we return the shuffled along version, eventsSubject isn't updated
         */
        return eventsSubject.doOnSubscribe(loadEventsIntoSubject())
                .map(shuffleAlong())
    }

    private fun loadEventsIntoSubject(): Action0 {
        return Action0 {
            if (currentlyFetching) {
                return@Action0
            }

            createFetchChunksObservable()
                    .flatMap { chunks ->
                        if (chunks.isPresent) {
                            val today = ChunkDate.create(clock)
                            val shuffledAlongChunks = chunksEditor.shuffleAlong(chunks.get(), today)
                            Observable.just(shuffledAlongChunks)
                        } else {
                            Observable.empty()
                        }
                    }
                    .doOnSubscribe(setCurrentlyLoadingFlag(true))
                    .doOnTerminate(setCurrentlyLoadingFlag(false))
                    .compose(EventRxFunctions.asEvents())
                    .subscribe(EventProxyObserver(eventsSubject, log))
        }
    }

    private fun setCurrentlyLoadingFlag(currentlyFetching: Boolean): Action0 {
        return Action0 { this@ChunksService.currentlyFetching = currentlyFetching }
    }

    private fun createFetchChunksObservable(): Observable<Optional<Chunks>> {
        return Observable.fromCallable { chunksRepository.chunks }
    }

    private fun shuffleAlong(): Func1<Event<Chunks>, Event<Chunks>> {
        return Func1 { chunksEvent ->
            val chunks = chunksEvent.data
            if (chunks.isPresent) {
                val today = ChunkDate.create(clock)
                val updatedChunks = chunksEditor.shuffleAlong(chunks.get(), today)
                chunksEvent.updateData(updatedChunks)
            } else {
                chunksEvent
            }
        }
    }

    fun createEntry(item: Item, day: Day) {
        val chunks = inMemoryChunks.or(Chunks.empty(ChunkDate.create(clock)))
        val updatedChunks = chunksEditor.add(chunks, day, item)
        eventsSubject.onNext(Event.idle(updatedChunks))
    }

    fun updateEntry(item: Item) {
        val chunks = inMemoryChunks
        if (chunks.isPresent) {
            val updatedChunks = chunksEditor.update(chunks.get(), item)
            eventsSubject.onNext(Event.idle(updatedChunks))
        }
    }

    fun editEntry(item: Item) {
        val chunks = inMemoryChunks
        if (chunks.isPresent) {
            val updatedChunks = chunksEditor.edit(chunks.get(), item.id)
            eventsSubject.onNext(Event.idle(updatedChunks))
        }
    }

    fun removeEntry(item: Item) {
        val chunks = inMemoryChunks
        if (chunks.isPresent) {
            val updatedChunks = chunksEditor.remove(chunks.get(), item.id)
            eventsSubject.onNext(Event.idle(updatedChunks))
        }
    }

    fun moveEntry(item: Item, newPosition: Int) {
        val chunks = inMemoryChunks
        if (chunks.isPresent) {
            val updatedChunks = chunksEditor.move(chunks.get(), item, newPosition)
            eventsSubject.onNext(Event.idle(updatedChunks))
        }
    }

    fun persist() {
        val chunks = inMemoryChunks
        if (chunks.isPresent) {
            chunksRepository.persist(chunks.get())
        }
    }
}
