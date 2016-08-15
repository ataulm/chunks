package com.ataulm.chunks;

class AndroidChunkEntryUserInteractions implements ChunkEntryUserInteractions {

    private final ChunksService chunksService;

    AndroidChunkEntryUserInteractions(ChunksService chunksService) {
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
        chunksService.removeEntry(entry);
        chunksService.createEntry(entry, Day.TOMORROW);
    }

    @Override
    public void onUserRemove(Entry entry) {
        chunksService.removeEntry(entry);
    }
}
