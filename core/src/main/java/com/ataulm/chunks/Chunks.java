package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Chunks {

    public static Chunks create(String lastShuffledTimestamp, Chunk today, Chunk tomorrow) {
        return new AutoValue_Chunks(lastShuffledTimestamp, today, tomorrow);
    }

    public static Chunks empty() {
        return create(generateLastShuffledTimestamp(), Chunk.empty(), Chunk.empty());
    }

    private static String generateLastShuffledTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    protected Chunks() {
        // use static factory
    }

    public abstract String lastShuffledTimestamp();

    public abstract Chunk today();

    public abstract Chunk tomorrow();

    public Chunks add(Entry entry, Day day) {
        switch (day) {
            case TODAY:
                return create(lastShuffledTimestamp(), today().add(entry), tomorrow());
            case TOMORROW:
                return create(lastShuffledTimestamp(), today(), tomorrow().add(entry));
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks add(List<Entry> entries, Day day) {
        switch (day) {
            case TODAY:
                return create(lastShuffledTimestamp(), today().add(entries), tomorrow());
            case TOMORROW:
                return create(lastShuffledTimestamp(), today(), tomorrow().add(entries));
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks remove(Id id) {
        if (today().containsEntryWith(id)) {
            return create(lastShuffledTimestamp(), today().remove(id), tomorrow());
        }

        if (tomorrow().containsEntryWith(id)) {
            return create(lastShuffledTimestamp(), today(), tomorrow().remove(id));
        }

        throw new IllegalArgumentException("no entries with id found: " + id);
    }

    public Chunks update(Entry entry) {
        if (today().containsEntryWith(entry.id())) {
            return create(lastShuffledTimestamp(), today().update(entry), tomorrow());
        }

        if (tomorrow().containsEntryWith(entry.id())) {
            return create(lastShuffledTimestamp(), today(), tomorrow().update(entry));
        }

        throw new IllegalArgumentException("no entries with id found: " + entry.id());
    }

    public Chunks shuffleAlong() {
        List<Entry> entriesCompletedToday = new ArrayList<>();
        for (Entry entry : today()) {
            if (entry.completedTimestamp().isPresent()) {
                entriesCompletedToday.add(entry);
            }
        }
        Chunk updatedToday = today().remove(entriesCompletedToday).add(tomorrow().values());
        Chunk updatedTomorrow = Chunk.empty();
        return Chunks.create(generateLastShuffledTimestamp(), updatedToday, updatedTomorrow);
    }

    public boolean isEmpty() {
        return today().isEmpty() && tomorrow().isEmpty();
    }

}

