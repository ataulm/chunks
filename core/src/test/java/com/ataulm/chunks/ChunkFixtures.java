package com.ataulm.chunks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ChunkFixtures {

    private List<Entry> entries;

    public static ChunkFixtures aChunk() {
        return new ChunkFixtures(
                Collections.<Entry>emptyList()
        );
    }

    private ChunkFixtures(List<Entry> entries) {
        this.entries = entries;
    }

    public ChunkFixtures with(Entry entry) {
        entries = new ArrayList<>();
        entries.add(entry);
        return this;
    }

    public ChunkFixtures with(List<Entry> entries) {
        this.entries = entries;
        return this;
    }

    public Chunk get() {
        return Chunk.create(entries);
    }

}
