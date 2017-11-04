package com.ataulm.chunks;

import java.util.ArrayList;
import java.util.List;

public class ChunksEditor {

    public Items add(Items items, Item item) {
        assertNoEntryWithSameId(items, item);

        List<Item> entries = new ArrayList<>(items.size() + 1);
        entries.addAll(items.entries());
        entries.add(item);
        return Items.create(entries);
    }

    public Items add(Items items, List<Item> entries) {
        for (Item item : entries) {
            assertNoEntryWithSameId(items, item);
        }

        List<Item> updatedEntries = new ArrayList<>(items.size() + entries.size());
        updatedEntries.addAll(items.entries());
        updatedEntries.addAll(entries);
        return Items.create(updatedEntries);
    }

    public Items remove(Items items, Id id) {
        List<Item> updatedEntries = new ArrayList<>(items.size());
        for (Item value : items.entries()) {
            if (!value.id().equals(id)) {
                updatedEntries.add(value);
            }
        }
        return Items.create(updatedEntries);
    }

    public Items remove(Items items, List<Item> entries) {
        List<Item> updatedEntries = new ArrayList<>(items.entries());
        updatedEntries.removeAll(entries);
        return Items.create(updatedEntries);
    }

    public Items update(Items items, Item item) {
        List<Item> updatedEntries = new ArrayList<>(items.size());
        for (Item existingValue : items.entries()) {
            if (existingValue.id().equals(item.id())) {
                updatedEntries.add(item);
            } else {
                updatedEntries.add(existingValue);
            }
        }
        return Items.create(updatedEntries);
    }

    public Chunks add(Chunks chunks, Day day, Item item) {
        assertNoEntriesWithSameId(chunks, item);

        switch (day) {
            case TODAY:
                Items updatedToday = add(chunks.today(), item);
                return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
            case TOMORROW:
                Items updatedTomorrow = add(chunks.tomorrow(), item);
                return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
            case SOMETIME:
                Items updatedSometime = add(chunks.sometime(), item);
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
                Items updatedToday = add(chunks.today(), entries);
                return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
            case TOMORROW:
                Items updatedTomorrow = add(chunks.tomorrow(), entries);
                return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
            case SOMETIME:
                Items updatedSometime = add(chunks.sometime(), entries);
                return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks edit(Chunks chunks, Id id) {
        if (chunks.today().containsEntryWith(id)) {
            Item item = chunks.today().findEntryWith(id);
            Items updatedToday = remove(chunks.today(), id);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime(), item.value());
        }

        if (chunks.tomorrow().containsEntryWith(id)) {
            Item item = chunks.tomorrow().findEntryWith(id);
            Items updatedTomorrow = remove(chunks.tomorrow(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime(), item.value());
        }

        if (chunks.sometime().containsEntryWith(id)) {
            Item item = chunks.sometime().findEntryWith(id);
            Items updatedSometime = remove(chunks.sometime(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime, item.value());
        }

        return chunks;
    }

    public Chunks remove(Chunks chunks, Id id) {
        if (chunks.today().containsEntryWith(id)) {
            Items updatedToday = remove(chunks.today(), id);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
        }

        if (chunks.tomorrow().containsEntryWith(id)) {
            Items updatedTomorrow = remove(chunks.tomorrow(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
        }

        if (chunks.sometime().containsEntryWith(id)) {
            Items updatedSometime = remove(chunks.sometime(), id);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
        }

        return chunks;
    }

    public Chunks update(Chunks chunks, Item item) {
        if (chunks.today().containsEntryWith(item.id())) {
            Items updatedToday = update(chunks.today(), item);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
        }

        if (chunks.tomorrow().containsEntryWith(item.id())) {
            Items updatedTomorrow = update(chunks.tomorrow(), item);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
        }

        if (chunks.sometime().containsEntryWith(item.id())) {
            Items updatedSometime = update(chunks.sometime(), item);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
        }

        throw new IllegalArgumentException("no entries with id found: " + item.id());
    }

    public Chunks move(Chunks chunks, Item item, int newEntryPosition) {
        if (chunks.today().containsEntryWith(item.id())) {
            Items updatedToday = move(chunks.today(), item, newEntryPosition);
            return Chunks.create(chunks.todaysDate(), updatedToday, chunks.tomorrow(), chunks.sometime());
        }

        if (chunks.tomorrow().containsEntryWith(item.id())) {
            Items updatedTomorrow = move(chunks.tomorrow(), item, newEntryPosition);
            return Chunks.create(chunks.todaysDate(), chunks.today(), updatedTomorrow, chunks.sometime());
        }

        if (chunks.sometime().containsEntryWith(item.id())) {
            Items updatedSometime = move(chunks.sometime(), item, newEntryPosition);
            return Chunks.create(chunks.todaysDate(), chunks.today(), chunks.tomorrow(), updatedSometime);
        }

        throw new IllegalArgumentException("no entries with id found: " + item.id());
    }

    public Items move(Items items, Item item, int newEntryPosition) {
        if (newEntryPosition < 0 || newEntryPosition >= items.size()) {
            throw new IllegalArgumentException("newEntryPosition is out of bounds: " + newEntryPosition);
        }

        int originalEntryPosition = items.entries().indexOf(item);
        if (originalEntryPosition == -1 || originalEntryPosition == newEntryPosition) {
            return items;
        }

        List<Item> updatedEntries = new ArrayList<>(items.entries());
        if (originalEntryPosition > newEntryPosition) {
            updatedEntries.add(newEntryPosition, item);
            updatedEntries.remove(originalEntryPosition + 1);
        } else {
            updatedEntries.add(newEntryPosition + 1, item);
            updatedEntries.remove(originalEntryPosition);
        }

        return Items.create(updatedEntries);
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

        Items updatedToday = Items.create(updatedTodayEntries);
        Items updatedTomorrow = Items.empty();
        Items updatedSometime = Items.create(updatedSometimeEntries);

        return Chunks.create(todaysDate, updatedToday, updatedTomorrow, updatedSometime);
    }

    private static void assertNoEntriesWithSameId(Chunks chunks, Item item) {
        assertNoEntryWithSameId(chunks.today(), item);
        assertNoEntryWithSameId(chunks.tomorrow(), item);
        assertNoEntryWithSameId(chunks.sometime(), item);
    }

    private static void assertNoEntryWithSameId(Items items, Item item) {
        if (items.containsEntryWith(item.id())) {
            throw new IllegalArgumentException("items already contains item with id: " + item.id());
        }
    }

}
