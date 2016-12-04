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
                                new AndroidChunkEntryUserInteractions(chunksService),
                                new AndroidEntryInputUserInteractions(chunksService)
                        )
                );
    }

    @Override
    public void onExternalShareText(String text) {
        chunksService.createEntry(Entry.createNew(text.trim()), Day.TODAY);
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

        private final ChunkEntryUserInteractions entryViewUserInteractions;
        private final EntryInputUserInteractions entryInputUserInteractions;

        public ChunksObserver(
                Log log,
                ChunkEntryUserInteractions entryViewUserInteractions,
                EntryInputUserInteractions entryInputUserInteractions
        ) {
            super(log);
            this.entryViewUserInteractions = entryViewUserInteractions;
            this.entryInputUserInteractions = entryInputUserInteractions;
        }

        @Override
        public void onNext(Event<Chunks> event) {
            Optional<Chunks> data = event.getData();
            if (data.isPresent()) { // TODO other cases like loading (empty, not empty), empty state, error (empty, not empty)
                Chunks chunks = data.get();
                chunksView.display(chunks, entryViewUserInteractions, entryInputUserInteractions);
            } else {
            }
        }

    }

}
