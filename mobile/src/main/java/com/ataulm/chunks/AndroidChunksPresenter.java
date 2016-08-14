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
                .subscribe(new ChunksObserver(new AndroidLog()));

        ///
//        entryInputView.bind(
//                new EntryInputView.Listener() {
//
//                    @Override
//                    public void onClickAddEntry(String value) {
//                        int currentItem = viewPager.getCurrentItem();
//                        Entry entry = Entry.createNew(value, getDayFor(currentItem));
//                        entryInputView.setEnabled(false);
//                        chunksService.createEntry(entry);
//                    }
//
//                }
//        );
    }

    @Override
    public void stopPresenting() {
        safeUnsubscribeFrom(subscription);
    }

    private static void safeUnsubscribeFrom(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private class ChunksObserver extends LoggingObserver<Event<Chunks>> {

        public ChunksObserver(Log log) {
            super(log);
        }

        @Override
        public void onNext(Event<Chunks> event) {
            Optional<Chunks> data = event.getData();
            if (data.isPresent()) { // TODO other cases
                Chunks chunks = data.get();
                chunksView.display(chunks);
            } else {
            }
        }

    }

}
