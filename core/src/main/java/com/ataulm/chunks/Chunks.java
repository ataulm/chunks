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

    public Chunks add(Entry entry, Day day) {
        switch (day) {
            case TODAY:
                return create(todaysDate(), today().add(entry), tomorrow(), sometime());
            case TOMORROW:
                return create(todaysDate(), today(), tomorrow().add(entry), sometime());
            case SOMETIME:
                return create(todaysDate(), today(), tomorrow(), sometime().add(entry));
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks add(List<Entry> entries, Day day) {
        switch (day) {
            case TODAY:
                return create(todaysDate(), today().add(entries), tomorrow(), sometime());
            case TOMORROW:
                return create(todaysDate(), today(), tomorrow().add(entries), sometime());
            case SOMETIME:
                return create(todaysDate(), today(), tomorrow(), sometime().add(entries));
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    public Chunks edit(Id id) {
        if (today().containsEntryWith(id)) {
            Entry entry = today().findEntryWith(id);
            return create(todaysDate(), today().remove(id), tomorrow(), sometime(), entry.value());
        }

        if (tomorrow().containsEntryWith(id)) {
            Entry entry = tomorrow().findEntryWith(id);
            return create(todaysDate(), today(), tomorrow().remove(id), sometime(), entry.value());
        }

        if (sometime().containsEntryWith(id)) {
            Entry entry = sometime().findEntryWith(id);
            return create(todaysDate(), today(), tomorrow(), sometime().remove(id), entry.value());
        }

        return this;
    }

    public Chunks remove(Id id) {
        if (today().containsEntryWith(id)) {
            return create(todaysDate(), today().remove(id), tomorrow(), sometime());
        }

        if (tomorrow().containsEntryWith(id)) {
            return create(todaysDate(), today(), tomorrow().remove(id), sometime());
        }

        if (sometime().containsEntryWith(id)) {
            return create(todaysDate(), today(), tomorrow(), sometime().remove(id));
        }

        return this;
    }

    public Chunks update(Entry entry) {
        if (today().containsEntryWith(entry.id())) {
            return create(todaysDate(), today().update(entry), tomorrow(), sometime());
        }

        if (tomorrow().containsEntryWith(entry.id())) {
            return create(todaysDate(), today(), tomorrow().update(entry), sometime());
        }

        if (sometime().containsEntryWith(entry.id())) {
            return create(todaysDate(), today(), tomorrow(), sometime().update(entry));
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

