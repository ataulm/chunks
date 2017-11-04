package com.ataulm.chunks;

import com.ataulm.AndroidLog;
import com.ataulm.Event;
import com.ataulm.Log;
import com.ataulm.LoggingObserver;
import com.ataulm.Optional;

import rx.Subscription;

class AndroidChunksPresenter implements ChunksPresenter {

    private final ChunksService chunksService;
    private final ChunksView chunksView;

    private Subscription subscription;

    AndroidChunksPresenter(ChunksService chunksService, ChunksView chunksView) {
        this.chunksService = chunksService;
        this.chunksView = chunksView;
    }

    @Override
    public void startPresenting() {
        safeUnsubscribeFrom(subscription);
        subscription = chunksService.fetchEntries()
                .subscribe(
                        new ChunksObserver(
                                new AndroidLog(),
                                new AndroidItemUserInteractions(chunksService),
                                new AndroidItemInputUserInteractions(chunksService)
                        )
                );
    }

    @Override
    public void onExternalShareText(String text) {
        chunksService.createEntry(Item.createNew(text.trim()), Day.TODAY);
    }

    @Override
    public void stopPresenting() {
        safeUnsubscribeFrom(subscription);
        chunksService.persist();
    }

    private static void safeUnsubscribeFrom(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private class ChunksObserver extends LoggingObserver<Event<Chunks>> {

        private final ItemUserInteractions entryViewUserInteractions;
        private final ItemInputUserInteractions itemInputUserInteractions;

        public ChunksObserver(
                Log log,
                ItemUserInteractions entryViewUserInteractions,
                ItemInputUserInteractions itemInputUserInteractions
        ) {
            super(log);
            this.entryViewUserInteractions = entryViewUserInteractions;
            this.itemInputUserInteractions = itemInputUserInteractions;
        }

        @Override
        public void onNext(Event<Chunks> event) {
            Optional<Chunks> data = event.getData();
            if (data.isPresent()) {
                Chunks chunks = data.get();
                chunksView.display(chunks, entryViewUserInteractions, itemInputUserInteractions);
            } else {
                // TODO other cases like loading (empty, not empty), empty state, error (empty, not empty)
            }
        }

    }

}
