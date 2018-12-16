package com.ataulm.chunks;

class AndroidItemInputUserInteractions implements ItemInputUserInteractions {

    private final ChunksService chunksService;

    AndroidItemInputUserInteractions(ChunksService chunksService) {
        this.chunksService = chunksService;
    }

    @Override
    public void onUserAddItem(String value, Day day) {
        Item item = Item.Companion.createNew(value);
        chunksService.createEntry(item, day);
    }

}
