package com.ataulm.chunks;

import java.util.ArrayList;
import java.util.List;

public class ChunksEditor {

    public Chunk add(Chunk chunk, Item item) {
        assertNoEntryWithSameId(chunk, item);

        List<Item> entries = new ArrayList<>(chunk.size() + 1);
        entries.addAll(chunk.entries());
        entries.add(item);
        return Chunk.create(entries);
    }

    public Chunk add(Chunk chunk, List<Item> entries) {
        for (Item item : entries) {
            assertNoEntryWithSameId(chunk, item);
        }

        List<Item> updatedEntries = new ArrayList<>(chunk.size() + entries.size());
        updatedEntries.addAll(chunk.entries());
        updatedEntries.addAll(entries);
        return Chunk.create(updatedEntries);
    }

    public Chunk remove(Chunk chunk, Id id) {
        List<Item> updatedEntries = new ArrayList<>(chunk.size());
        for (Item value : chunk.entries()) {
            if (!value.id().equals(id)) {
                updatedEntries.add(value);
            }
        }
        return Chunk.create(updatedEntries);
    }

    public Chunk remove(Chunk chunk, List<Item> entries) {
        List<Item> updatedEntries = new ArrayList<>(chunk.entries());
        updatedEntries.removeAll(entries);
        return Chunk.create(updatedEntries);
    }

    public Chunk update(Chunk chunk, Item item) {
        List<Item> updatedEntries = new ArrayList<>(chunk.size());
        for (Item existingValue : chunk.entries()) {
            if (existingValue.id().equals(item.id())) {
                updatedEntries.add(item);
            } else {
                updatedEntries.add(existingValue);
            }
        }
        return Chunk.create(updatedEntries);
    }

    public Chunks add(Chunks chunks, Day day, Item item) {
        assertNoEntriesWithSameId(chunks, item);

        switch (day) {
            case TODAY:
                Chunk updatedToday = add(chunks.today(), item);
                return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
            case TOMORROW:
                Chunk updatedTomorrow = add(chunks.tomorrow(), item);
                return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
            case SOMETIME:
                Chunk updatedSometime = add(chunks.sometime(), item);
                return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks add(Chunks chunks, Day day, List<Item> entries) {
        for (Item item : entries) {
            assertNoEntriesWithSameId(chunks, item);
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
            Item item = chunks.today().findEntryWith(id);
            Chunk updatedToday = remove(chunks.today(), id);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime(), item.value());
        }

        if (chunks.tomorrow().containsEntryWith(id)) {
            Item item = chunks.tomorrow().findEntryWith(id);
            Chunk updatedTomorrow = remove(chunks.tomorrow(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime(), item.value());
        }

        if (chunks.sometime().containsEntryWith(id)) {
            Item item = chunks.sometime().findEntryWith(id);
            Chunk updatedSometime = remove(chunks.sometime(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime, item.value());
        }

        return chunks;
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

    public Chunks update(Chunks chunks, Item item) {
        if (chunks.today().containsEntryWith(item.id())) {
            Chunk updatedToday = update(chunks.today(), item);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
        }

        if (chunks.tomorrow().containsEntryWith(item.id())) {
            Chunk updatedTomorrow = update(chunks.tomorrow(), item);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
        }

        if (chunks.sometime().containsEntryWith(item.id())) {
            Chunk updatedSometime = update(chunks.sometime(), item);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
        }

        throw new IllegalArgumentException("no entries with id found: " + item.id());
    }

    public Chunks move(Chunks chunks, Item item, int newEntryPosition) {
        if (chunks.today().containsEntryWith(item.id())) {
            Chunk updatedToday = move(chunks.today(), item, newEntryPosition);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
        }

        if (chunks.tomorrow().containsEntryWith(item.id())) {
            Chunk updatedTomorrow = move(chunks.tomorrow(), item, newEntryPosition);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
        }

        if (chunks.sometime().containsEntryWith(item.id())) {
            Chunk updatedSometime = move(chunks.sometime(), item, newEntryPosition);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
        }

        throw new IllegalArgumentException("no entries with id found: " + item.id());
    }

    public Chunk move(Chunk chunk, Item item, int newEntryPosition) {
        if (newEntryPosition < 0 || newEntryPosition >= chunk.size()) {
            throw new IllegalArgumentException("newEntryPosition is out of bounds: " + newEntryPosition);
        }

        int originalEntryPosition = chunk.entries().indexOf(item);
        if (originalEntryPosition == -1 || originalEntryPosition == newEntryPosition) {
            return chunk;
        }

        List<Item> updatedEntries = new ArrayList<>(chunk.entries());
        if (originalEntryPosition > newEntryPosition) {
            updatedEntries.add(newEntryPosition, item);
            updatedEntries.remove(originalEntryPosition + 1);
        } else {
            updatedEntries.add(newEntryPosition + 1, item);
            updatedEntries.remove(originalEntryPosition);
        }

        return Chunk.create(updatedEntries);
    }

    public Chunks shuffleAlong(Chunks chunks, ChunkDate todaysDate) {
        if (chunks.todaysDate().isSameDayAs(todaysDate)) {
            return chunks;
        }

        List<Item> updatedTodayEntries = new ArrayList<>();
        for (Item item : chunks.today()) {
            if (!item.isCompleted()) {
                updatedTodayEntries.add(item);
            }
        }

        for (Item item : chunks.tomorrow()) {
            if (!item.isCompleted()) {
                updatedTodayEntries.add(item);
            }
        }

        List<Item> updatedSometimeEntries = new ArrayList<>();
        for (Item item : chunks.sometime()) {
            if (!item.isCompleted()) {
                updatedSometimeEntries.add(item);
            }
        }

        Chunk updatedToday = Chunk.create(updatedTodayEntries);
        Chunk updatedTomorrow = Chunk.empty();
        Chunk updatedSometime = Chunk.create(updatedSometimeEntries);

        return Chunks.create(todaysDate, updatedToday, updatedTomorrow, updatedSometime);
    }

    private static void assertNoEntriesWithSameId(Chunks chunks, Item item) {
        assertNoEntryWithSameId(chunks.today(), item);
        assertNoEntryWithSameId(chunks.tomorrow(), item);
        assertNoEntryWithSameId(chunks.sometime(), item);
    }

    private static void assertNoEntryWithSameId(Chunk chunk, Item item) {
        if (chunk.containsEntryWith(item.id())) {
            throw new IllegalArgumentException("chunk already contains item with id: " + item.id());
        }
    }

}
