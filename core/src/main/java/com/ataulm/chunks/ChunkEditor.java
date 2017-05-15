package com.ataulm.chunks;

import java.util.ArrayList;
import java.util.List;

public class ChunkEditor {

    public Chunk add(Chunk chunk, Entry entry) {
        if (chunk.containsEntryWith(entry.id())) {
            throw new IllegalArgumentException("chunk already contains entry with id: " + entry.id());
        }

        List<Entry> entries = new ArrayList<>(chunk.size() + 1);
        entries.addAll(chunk.entries());
        entries.add(entry);
        return Chunk.create(entries);
    }

    public Chunk add(Chunk chunk, List<Entry> entries) {
        for (Entry entry : entries) {
            if (chunk.containsEntryWith(entry.id())) {
                throw new IllegalArgumentException("chunk already contains entry with id: " + entry.id());
            }
        }

        List<Entry> updatedEntries = new ArrayList<>(chunk.size() + entries.size());
        updatedEntries.addAll(chunk.entries());
        updatedEntries.addAll(entries);
        return Chunk.create(updatedEntries);
    }

    public Chunk remove(Chunk chunk, Id id) {
        List<Entry> updatedEntries = new ArrayList<>(chunk.size());
        for (Entry value : chunk.entries()) {
            if (!value.id().equals(id)) {
                updatedEntries.add(value);
            }
        }
        return Chunk.create(updatedEntries);
    }

    public Chunk remove(Chunk chunk, List<Entry> entries) {
        List<Entry> updatedEntries = new ArrayList<>(chunk.entries());
        updatedEntries.removeAll(entries);
        return Chunk.create(updatedEntries);
    }

    public Chunk update(Chunk chunk, Entry entry) {
        List<Entry> updatedEntries = new ArrayList<>(chunk.size());
        for (Entry existingValue : chunk.entries()) {
            if (existingValue.id().equals(entry.id())) {
                updatedEntries.add(entry);
            } else {
                updatedEntries.add(existingValue);
            }
        }
        return Chunk.create(updatedEntries);
    }

}
