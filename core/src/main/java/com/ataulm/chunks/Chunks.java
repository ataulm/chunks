package com.ataulm.chunks;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Chunks {

    public static Chunks empty(ChunkDate todaysDate) {
        return create(todaysDate, Items.empty(), Items.empty(), Items.empty());
    }

    public static Chunks create(ChunkDate todaysDate, Items today, Items tomorrow, Items sometime) {
        return create(todaysDate, today, tomorrow, sometime, null);
    }

    public static Chunks create(ChunkDate todaysDate, Items today, Items tomorrow, Items sometime, @Nullable String input) {
        return new AutoValue_Chunks(todaysDate, today, tomorrow, sometime, Optional.fromNullable(input));
    }

    protected Chunks() {
        // use static factory
    }

    public abstract ChunkDate todaysDate();

    public abstract Items today();

    public abstract Items tomorrow();

    public abstract Items sometime();

    public abstract Optional<String> input();

}

