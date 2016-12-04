package com.ataulm.chunks.repository;

import com.ataulm.chunks.Chunk;
import com.ataulm.chunks.Entry;

import java.util.ArrayList;
import java.util.List;

public class GsonChunkConverter {

    private final GsonEntryConverter entryConverter;

    public GsonChunkConverter(GsonEntryConverter entryConverter) {
        this.entryConverter = entryConverter;
    }

    public GsonChunk convert(Chunk chunk) {
        GsonChunk gsonChunk = new GsonChunk();
        gsonChunk.entries = new ArrayList<>(chunk.size());
        for (Entry entry : chunk) {
            gsonChunk.entries.add(entryConverter.convert(entry));
        }
        return gsonChunk;
    }

    public Chunk convert(GsonChunk gsonChunk) {
        if (gsonChunk == null) {
            return Chunk.empty();
        }

        List<Entry> entries = new ArrayList<>(gsonChunk.entries.size());
        for (GsonEntry gsonEntry : gsonChunk.entries) {
            Entry entry = entryConverter.convert(gsonEntry);
            entries.add(entry);
        }
        return Chunk.create(entries);
    }

}
