package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

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

    public Chunks remove(Entry entry) {
        if (yesterday().contains(entry)) {
            return create(lastShuffledTimestamp(), yesterday().remove(entry), today(), tomorrow());
        }

        if (today().contains(entry)) {
            return create(lastShuffledTimestamp(), yesterday(), today().remove(entry), tomorrow());
        }

        if (tomorrow().contains(entry)) {
            return create(lastShuffledTimestamp(), yesterday(), today(), tomorrow().remove(entry));
        }

        throw new IllegalArgumentException("entry not found: " + entry);
    }

    public Chunks update(Entry entry) {
        if (yesterday().contains(entry)) {
            return create(lastShuffledTimestamp(), yesterday().update(entry), today(), tomorrow());
        }

        if (today().contains(entry)) {
            return create(lastShuffledTimestamp(), yesterday(), today().update(entry), tomorrow());
        }

        if (tomorrow().contains(entry)) {
            return create(lastShuffledTimestamp(), yesterday(), today(), tomorrow().update(entry));
        }

        throw new IllegalArgumentException("entry not found: " + entry);
    }

    public boolean isEmpty() {
        return yesterday().isEmpty() && today().isEmpty() && tomorrow().isEmpty();
    }

}

