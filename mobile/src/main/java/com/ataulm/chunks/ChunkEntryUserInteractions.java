package com.ataulm.chunks;

interface ChunkEntryUserInteractions {

    void onUserMarkComplete(Entry entry);

    void onUserMarkNotComplete(Entry entry);

    void onUserMoveToTomorrow(Entry entry);

    void onUserRemove(Entry entry);

}
