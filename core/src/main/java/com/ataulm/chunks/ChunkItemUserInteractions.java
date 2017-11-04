package com.ataulm.chunks;

interface ChunkItemUserInteractions {

    void onUserMarkComplete(Item item);

    void onUserMarkNotComplete(Item item);

    void onUserTransitionItem(Item item, Day day);

    void onUserEdit(Item item);

    void onUserRemove(Item item);

    void onUserMove(Item item, int position);

}
