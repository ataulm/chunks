package com.ataulm.chunks;

class AndroidItemUserInteractions implements ItemUserInteractions {

    private final ChunksService chunksService;

    AndroidItemUserInteractions(ChunksService chunksService) {
        this.chunksService = chunksService;
    }

    @Override
    public void onUserMarkComplete(Item item) {
        Item updatedItem = item.markCompleted();
        chunksService.updateEntry(updatedItem);
    }

    @Override
    public void onUserMarkNotComplete(Item item) {
        Item updatedItem = item.markNotComplete();
        chunksService.updateEntry(updatedItem);
    }

    @Override
    public void onUserTransitionItem(Item item, Day day) {
        chunksService.removeEntry(item);
        chunksService.createEntry(item, day);
    }

    @Override
    public void onUserEdit(Item item) {
        chunksService.editEntry(item);
    }

    @Override
    public void onUserRemove(Item item) {
        chunksService.removeEntry(item);
    }

    @Override
    public void onUserMove(Item item, int position) {
        chunksService.moveEntry(item, position);
    }

}
