package com.ataulm.chunks;

interface ChunkEntryUserInteractions {

    void onUserMarkComplete(Entry entry);

    void onUserMarkNotComplete(Entry entry);

    void onUserTransitionEntry(Entry entry, Day day);

    void onUserEdit(Entry entry);

    void onUserRemove(Entry entry);

}
