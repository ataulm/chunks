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
        for (Entry entry : chunk) {
            gsonChunk.add(entryConverter.convert(entry));
        }
        return gsonChunk;
    }

    public Chunk convert(GsonChunk gsonChunk) {
        List<Entry> entries = new ArrayList<>(gsonChunk.size());
        for (GsonEntry gsonEntry : gsonChunk) {
            Entry entry = entryConverter.convert(gsonEntry);
            entries.add(entry);
        }
        return Chunk.create(entries);
    }

}
