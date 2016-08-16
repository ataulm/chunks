package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Chunks {

    public static Chunks create(String lastShuffledTimestamp, Chunk yesterday, Chunk today, Chunk tomorrow) {
        return new AutoValue_Chunks(lastShuffledTimestamp, yesterday, today, tomorrow);
    }

    public static Chunks empty() {
        return create(generateLastShuffledTimestamp(), Chunk.empty(), Chunk.empty(), Chunk.empty());
    }

    private static String generateLastShuffledTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    protected Chunks() {
        // use static factory
    }

    public abstract String lastShuffledTimestamp();

    public abstract Chunk yesterday();

    public abstract Chunk today();

    public abstract Chunk tomorrow();

    public Chunks add(Entry entry, Day day) {
        switch (day) {
            case YESTERDAY:
                return create(lastShuffledTimestamp(), yesterday().add(entry), today(), tomorrow());
            case TODAY:
                return create(lastShuffledTimestamp(), yesterday(), today().add(entry), tomorrow());
            case TOMORROW:
                return create(lastShuffledTimestamp(), yesterday(), today(), tomorrow().add(entry));
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks add(List<Entry> entries, Day day) {
        switch (day) {
            case YESTERDAY:
                return create(lastShuffledTimestamp(), yesterday().add(entries), today(), tomorrow());
            case TODAY:
                return create(lastShuffledTimestamp(), yesterday(), today().add(entries), tomorrow());
            case TOMORROW:
                return create(lastShuffledTimestamp(), yesterday(), today(), tomorrow().add(entries));
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks remove(Id id) {
        if (yesterday().containsEntryWith(id)) {
            return create(lastShuffledTimestamp(), yesterday().remove(id), today(), tomorrow());
        }

        if (today().containsEntryWith(id)) {
            return create(lastShuffledTimestamp(), yesterday(), today().remove(id), tomorrow());
        }

        if (tomorrow().containsEntryWith(id)) {
            return create(lastShuffledTimestamp(), yesterday(), today(), tomorrow().remove(id));
        }

        throw new IllegalArgumentException("no entries with id found: " + id);
    }

    public boolean isEmpty() {
        return yesterday().isEmpty() && today().isEmpty() && tomorrow().isEmpty();
    }

    public Chunks transition(List<Entry> entries, Day day) {
        Chunk updatedYesterday = yesterday().remove(entries);
        Chunk updatedToday = today().remove(entries);
        Chunk updatedTomorrow = tomorrow().remove(entries);

        return create(lastShuffledTimestamp(), updatedYesterday, updatedToday, updatedTomorrow).add(entries, day);
    }

    public Chunks update(Entry entry) {
        if (yesterday().containsEntryWith(entry.id())) {
            return create(lastShuffledTimestamp(), yesterday().update(entry), today(), tomorrow());
        }

        if (today().containsEntryWith(entry.id())) {
            return create(lastShuffledTimestamp(), yesterday(), today().update(entry), tomorrow());
        }

        if (tomorrow().containsEntryWith(entry.id())) {
            return create(lastShuffledTimestamp(), yesterday(), today(), tomorrow().update(entry));
        }

        throw new IllegalArgumentException("no entries with id found: " + entry.id());
    }
}

