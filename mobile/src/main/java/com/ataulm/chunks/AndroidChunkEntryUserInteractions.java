package com.ataulm.chunks;

class AndroidChunkEntryUserInteractions implements ChunkEntryUserInteractions {

    private final ChunksService chunksService;

    AndroidChunkEntryUserInteractions(ChunksService chunksService) {
        this.chunksService = chunksService;
    }

    @Override
    public void onUserMarkComplete(Entry entry) {
        Entry updatedEntry = entry.markCompleted();
        chunksService.updateEntry(updatedEntry);
    }

    @Override
    public void onUserMarkNotComplete(Entry entry) {
        Entry updatedEntry = entry.markNotComplete();
        chunksService.updateEntry(updatedEntry);
    }

    @Override
    public void onUserTransitionEntry(Entry entry, Day day) {
        chunksService.removeEntry(entry);
        chunksService.createEntry(entry, day);
    }

    @Override
    public void onUserEdit(Entry entry) {
        chunksService.editEntry(entry);
    }

    @Override
    public void onUserRemove(Entry entry) {
        chunksService.removeEntry(entry);
    }
}
