package com.ataulm.chunks;

class AndroidEntryInputUserInteractions implements EntryInputUserInteractions {

    private final ChunksService chunksService;

    AndroidEntryInputUserInteractions(ChunksService chunksService) {
        this.chunksService = chunksService;
    }

    @Override
    public void onUserAddEntry(String value, Day day) {
        Entry entry = Entry.createNew(value);
        chunksService.createEntry(entry, day);
    }

}
