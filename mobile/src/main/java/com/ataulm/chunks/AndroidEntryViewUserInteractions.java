package com.ataulm.chunks;

class AndroidEntryViewUserInteractions implements EntryView.UserInteractions {

    private final ChunksService chunksService;

    AndroidEntryViewUserInteractions(ChunksService chunksService) {
        this.chunksService = chunksService;
    }

    @Override
    public void onUserMarkComplete(Entry entry) {
        Entry updatedEntry = Entry.completed(entry);
        chunksService.updateEntry(updatedEntry);
    }

    @Override
    public void onUserMarkNotComplete(Entry entry) {
        Entry updatedEntry = Entry.notCompleted(entry);
        chunksService.updateEntry(updatedEntry);
    }

    @Override
    public void onUserMoveToTomorrow(Entry entry) {
        Entry updatedEntry = Entry.transitioned(entry, Day.TOMORROW);
        chunksService.updateEntry(updatedEntry);
    }

    @Override
    public void onUserRemove(Entry entry) {
        chunksService.removeEntry(entry);
    }
}
