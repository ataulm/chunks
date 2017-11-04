package com.ataulm.chunks.repository;

import com.ataulm.chunks.Chunk;
import com.ataulm.chunks.Item;

import java.util.ArrayList;
import java.util.List;

public class GsonChunkConverter {

    private final GsonItemConverter entryConverter;

    public GsonChunkConverter(GsonItemConverter entryConverter) {
        this.entryConverter = entryConverter;
    }

    public GsonChunk convert(Chunk chunk) {
        GsonChunk gsonChunk = new GsonChunk();
        gsonChunk.entries = new ArrayList<>(chunk.size());
        for (Item item : chunk) {
            gsonChunk.entries.add(entryConverter.convert(item));
        }
        return gsonChunk;
    }

    public Chunk convert(GsonChunk gsonChunk) {
        if (gsonChunk == null) {
            return Chunk.empty();
        }

        List<Item> entries = new ArrayList<>(gsonChunk.entries.size());
        for (GsonItem gsonItem : gsonChunk.entries) {
            Item item = entryConverter.convert(gsonItem);
            entries.add(item);
        }
        return Chunk.create(entries);
    }

}
