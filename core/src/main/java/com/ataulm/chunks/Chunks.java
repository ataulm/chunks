package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Chunks {

    public static Chunks create(String modifiedTimestamp, Chunk yesterday, Chunk today, Chunk tomorrow) {
        return new AutoValue_Chunks(modifiedTimestamp, yesterday, today, tomorrow);
    }

    public static Chunks empty() {
        return create(generateModifiedTimestamp(), Chunk.empty(), Chunk.empty(), Chunk.empty());
    }

    private static String generateModifiedTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    protected Chunks() {
        // use static factory
    }

    public abstract String modifiedTimestamp();

    public abstract Chunk yesterday();

    public abstract Chunk today();

    public abstract Chunk tomorrow();

    public Chunks add(Entry entry) {
        switch (entry.day()) {
            case YESTERDAY:
                return create(generateModifiedTimestamp(), yesterday().add(entry), today(), tomorrow());
            case TODAY:
                return create(generateModifiedTimestamp(), yesterday(), today().add(entry), tomorrow());
            case TOMORROW:
                return create(generateModifiedTimestamp(), yesterday(), today(), tomorrow().add(entry));
            default:
                throw new IllegalArgumentException("unsupported day: " + entry.day());
        }
    }

    public Chunks remove(Entry entry) {
        if (yesterday().contains(entry)) {
            return create(generateModifiedTimestamp(), yesterday().remove(entry), today(), tomorrow());
        }

        if (today().contains(entry)) {
            return create(generateModifiedTimestamp(), yesterday(), today().remove(entry), tomorrow());
        }

        if (tomorrow().contains(entry)) {
            return create(generateModifiedTimestamp(), yesterday(), today(), tomorrow().remove(entry));
        }

        throw new IllegalArgumentException("entry not found: " + entry);
    }

    public Chunks update(Entry entry) {
        if (yesterday().contains(entry)) {
            return create(generateModifiedTimestamp(), yesterday().update(entry), today(), tomorrow());
        }

        if (today().contains(entry)) {
            return create(generateModifiedTimestamp(), yesterday(), today().update(entry), tomorrow());
        }

        if (tomorrow().contains(entry)) {
            return create(generateModifiedTimestamp(), yesterday(), today(), tomorrow().update(entry));
        }

        throw new IllegalArgumentException("entry not found: " + entry);
    }

    public boolean isEmpty() {
        return yesterday().isEmpty() && today().isEmpty() && tomorrow().isEmpty();
    }

}

