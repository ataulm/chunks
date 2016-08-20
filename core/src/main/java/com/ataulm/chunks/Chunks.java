package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoValue
public abstract class Chunks {

    public static Chunks create(ChunkDate todaysDate, Chunk today, Chunk tomorrow) {
        return new AutoValue_Chunks(todaysDate, today, tomorrow);
    }

    public static Chunks empty(ChunkDate todaysDate) {
        return create(todaysDate, Chunk.empty(), Chunk.empty());
    }

    protected Chunks() {
        // use static factory
    }

    public abstract ChunkDate todaysDate();

    public abstract Chunk today();

    public abstract Chunk tomorrow();

    public Chunks add(Entry entry, Day day) {
        switch (day) {
            case TODAY:
                return create(todaysDate(), today().add(entry), tomorrow());
            case TOMORROW:
                return create(todaysDate(), today(), tomorrow().add(entry));
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks add(List<Entry> entries, Day day) {
        switch (day) {
            case TODAY:
                return create(todaysDate(), today().add(entries), tomorrow());
            case TOMORROW:
                return create(todaysDate(), today(), tomorrow().add(entries));
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks remove(Id id) {
        if (today().containsEntryWith(id)) {
            return create(todaysDate(), today().remove(id), tomorrow());
        }

        if (tomorrow().containsEntryWith(id)) {
            return create(todaysDate(), today(), tomorrow().remove(id));
        }

        throw new IllegalArgumentException("no entries with id found: " + id);
    }

    public Chunks update(Entry entry) {
        if (today().containsEntryWith(entry.id())) {
            return create(todaysDate(), today().update(entry), tomorrow());
        }

        if (tomorrow().containsEntryWith(entry.id())) {
            return create(todaysDate(), today(), tomorrow().update(entry));
        }

        throw new IllegalArgumentException("no entries with id found: " + entry.id());
    }

    public Chunks shuffleAlong(ChunkDate todaysDate) {
        if (todaysDate.isSameDayAs(todaysDate())) {
            return this;
        }

        List<Entry> updatedTodayEntries = new ArrayList<>();
        for (Entry entry : today()) {
            if (!entry.isCompleted()) {
                updatedTodayEntries.add(entry);
            }
        }
        updatedTodayEntries.addAll(tomorrow().entries());

        Chunk updatedToday = Chunk.create(updatedTodayEntries);
        Chunk updatedTomorrow = Chunk.empty();

        return Chunks.create(todaysDate, updatedToday, updatedTomorrow);
    }

    public boolean isEmpty() {
        return today().isEmpty() && tomorrow().isEmpty();
    }

}

