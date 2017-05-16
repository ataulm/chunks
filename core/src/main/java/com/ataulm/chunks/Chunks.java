package com.ataulm.chunks;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Chunks {

    public static Chunks empty(ChunkDate todaysDate) {
        return create(todaysDate, Chunk.empty(), Chunk.empty(), Chunk.empty());
    }

    public static Chunks create(ChunkDate todaysDate, Chunk today, Chunk tomorrow, Chunk sometime) {
        return create(todaysDate, today, tomorrow, sometime, null);
    }

    public static Chunks create(ChunkDate todaysDate, Chunk today, Chunk tomorrow, Chunk sometime, @Nullable String input) {
        return new AutoValue_Chunks(todaysDate, today, tomorrow, sometime, Optional.fromNullable(input));
    }

    protected Chunks() {
        // use static factory
    }

    public abstract ChunkDate todaysDate();

    public abstract Chunk today();

    public abstract Chunk tomorrow();

    public abstract Chunk sometime();

    public abstract Optional<String> input();

}

