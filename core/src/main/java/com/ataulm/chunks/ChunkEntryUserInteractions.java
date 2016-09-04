package com.ataulm.chunks;

interface ChunkEntryUserInteractions {

    void onUserMarkComplete(Entry entry);

    void onUserMarkNotComplete(Entry entry);

    void onUserTransitionEntry(Entry entry, Day day);

    void onUserRemove(Entry entry);

}
