package com.ataulm.chunks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ChunkFixtures {

    private List<Item> entries;

    public static ChunkFixtures aChunk() {
        return new ChunkFixtures(
                Collections.<Item>emptyList()
        );
    }

    private ChunkFixtures(List<Item> entries) {
        this.entries = entries;
    }

    public ChunkFixtures with(Item item) {
        entries = new ArrayList<>();
        entries.add(item);
        return this;
    }

    public ChunkFixtures with(List<Item> entries) {
        this.entries = entries;
        return this;
    }

    public Chunk get() {
        return Chunk.create(entries);
    }

}
