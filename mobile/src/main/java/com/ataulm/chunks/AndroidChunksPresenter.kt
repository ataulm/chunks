package com.ataulm.chunks

import com.ataulm.AndroidLog
import com.ataulm.Event
import com.ataulm.Log
import com.ataulm.LoggingObserver

import rx.Subscription

internal class AndroidChunksPresenter(private val chunksService: ChunksService, private val chunksView: ChunksView) : ChunksPresenter {

    private var subscription: Subscription? = null

    override fun startPresenting() {
        safeUnsubscribeFrom(subscription)
        subscription = chunksService.fetchEntries()
                .subscribe(
                        ChunksObserver(
                                AndroidLog(),
                                AndroidItemUserInteractions(chunksService),
                                AndroidItemInputUserInteractions(chunksService)
                        )
                )
    }

    override fun onExternalShareText(text: String) {
        chunksService.createEntry(Item.createNew(text.trim { it <= ' ' }), Day.TODAY)
    }

    override fun stopPresenting() {
        safeUnsubscribeFrom(subscription)
        chunksService.persist()
    }

    private fun safeUnsubscribeFrom(subscription: Subscription?) {
        if (subscription != null && !subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
    }

    private inner class ChunksObserver internal constructor(
            log: Log,
            private val entryViewUserInteractions: ItemUserInteractions,
            private val itemInputUserInteractions: ItemInputUserInteractions
    ) : LoggingObserver<Event<Chunks>>(log) {

        override fun onNext(event: Event<Chunks>) {
            val data = event.data
            if (data != null) {
                display(data)
            } else {
                // TODO other cases like loading (empty, not empty), empty state, error (empty, not empty)
                display(Chunks.empty(ChunkDate.create(SystemClock())))
            }
        }

        private fun display(chunks: Chunks) {
            chunksView.display(chunks, entryViewUserInteractions, itemInputUserInteractions)
        }
    }
}
