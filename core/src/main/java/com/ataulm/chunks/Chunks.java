package com.ataulm.chunks;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

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

        for (Entry entry : tomorrow()) {
            if (!entry.isCompleted()) {
                updatedTodayEntries.add(entry);
            }
        }

        List<Entry> updatedSometimeEntries = new ArrayList<>();
        for (Entry entry : sometime()) {
            if (!entry.isCompleted()) {
                updatedSometimeEntries.add(entry);
            }
        }

        Chunk updatedToday = Chunk.create(updatedTodayEntries);
        Chunk updatedTomorrow = Chunk.empty();
        Chunk updatedSometime = Chunk.create(updatedSometimeEntries);

        return Chunks.create(todaysDate, updatedToday, updatedTomorrow, updatedSometime);
    }

}

