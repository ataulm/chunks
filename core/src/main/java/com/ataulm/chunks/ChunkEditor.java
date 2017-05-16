package com.ataulm.chunks;

import java.util.ArrayList;
import java.util.List;

public class ChunkEditor {

    public Chunk add(Chunk chunk, Entry entry) {
        assertNoEntryWithSameId(chunk, entry);

        List<Entry> entries = new ArrayList<>(chunk.size() + 1);
        entries.addAll(chunk.entries());
        entries.add(entry);
        return Chunk.create(entries);
    }

    public Chunk add(Chunk chunk, List<Entry> entries) {
        for (Entry entry : entries) {
            assertNoEntryWithSameId(chunk, entry);
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

    public Chunks add(Chunks chunks, Day day, Entry entry) {
        assertNoEntriesWithSameId(chunks, entry);

        switch (day) {
            case TODAY:
                Chunk updatedToday = add(chunks.today(), entry);
                return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
            case TOMORROW:
                Chunk updatedTomorrow = add(chunks.tomorrow(), entry);
                return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
            case SOMETIME:
                Chunk updatedSometime = add(chunks.sometime(), entry);
                return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks add(Chunks chunks, Day day, List<Entry> entries) {
        for (Entry entry : entries) {
            assertNoEntriesWithSameId(chunks, entry);
        }

        switch (day) {
            case TODAY:
                Chunk updatedToday = add(chunks.today(), entries);
                return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
            case TOMORROW:
                Chunk updatedTomorrow = add(chunks.tomorrow(), entries);
                return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
            case SOMETIME:
                Chunk updatedSometime = add(chunks.sometime(), entries);
                return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks edit(Chunks chunks, Id id) {
        if (chunks.today().containsEntryWith(id)) {
            Entry entry = chunks.today().findEntryWith(id);
            Chunk updatedToday = remove(chunks.today(), id);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime(), entry.value());
        }

        if (chunks.tomorrow().containsEntryWith(id)) {
            Entry entry = chunks.tomorrow().findEntryWith(id);
            Chunk updatedTomorrow = remove(chunks.tomorrow(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime(), entry.value());
        }

        if (chunks.sometime().containsEntryWith(id)) {
            Entry entry = chunks.sometime().findEntryWith(id);
            Chunk updatedSometime = remove(chunks.sometime(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime, entry.value());
        }

        return chunks;
    }

    private static void assertNoEntriesWithSameId(Chunks chunks, Entry entry) {
        assertNoEntryWithSameId(chunks.today(), entry);
        assertNoEntryWithSameId(chunks.tomorrow(), entry);
        assertNoEntryWithSameId(chunks.sometime(), entry);
    }

    private static void assertNoEntryWithSameId(Chunk chunk, Entry entry) {
        if (chunk.containsEntryWith(entry.id())) {
            throw new IllegalArgumentException("chunk already contains entry with id: " + entry.id());
        }
    }

    public Chunks remove(Chunks chunks, Id id) {
        if (chunks.today().containsEntryWith(id)) {
            Chunk updatedToday = remove(chunks.today(), id);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
        }

        if (chunks.tomorrow().containsEntryWith(id)) {
            Chunk updatedTomorrow = remove(chunks.tomorrow(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
        }

        if (chunks.sometime().containsEntryWith(id)) {
            Chunk updatedSometime = remove(chunks.sometime(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
        }

        return chunks;
    }

    public Chunks update(Chunks chunks, Entry entry) {
        if (chunks.today().containsEntryWith(entry.id())) {
            Chunk updatedToday = update(chunks.today(), entry);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
        }

        if (chunks.tomorrow().containsEntryWith(entry.id())) {
            Chunk updatedTomorrow = update(chunks.tomorrow(), entry);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
        }

        if (chunks.sometime().containsEntryWith(entry.id())) {
            Chunk updatedSometime = update(chunks.sometime(), entry);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
        }

        throw new IllegalArgumentException("no entries with id found: " + entry.id());
    }

}