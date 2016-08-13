package com.ataulm.chunks;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Entry {

    public static Entry createNew(String value, com.ataulm.chunks.Day day) {
        return new AutoValue_Entry(com.ataulm.chunks.Id.create(), value, day, Optional.<String>absent());
    }

    public static Entry createFrom(com.ataulm.chunks.Id id, String value, com.ataulm.chunks.Day day) {
        return new AutoValue_Entry(id, value, day, Optional.<String>absent());
    }

    public static Entry createFrom(com.ataulm.chunks.Id id, String value, com.ataulm.chunks.Day day, @Nullable String completedTimestamp) {
        return new AutoValue_Entry(id, value, day, Optional.fromNullable(completedTimestamp));
    }

    public static Entry completed(Entry entry, String completedTimestamp) {
        return new AutoValue_Entry(entry.id(), entry.value(), entry.day(), Optional.of(completedTimestamp));
    }

    public static Entry edited(Entry entry, String value) {
        return new AutoValue_Entry(entry.id(), value, entry.day(), entry.completedTimestamp());
    }

    public static Entry transitioned(Entry entry, com.ataulm.chunks.Day day) {
        return new AutoValue_Entry(entry.id(), entry.value(), day, entry.completedTimestamp());
    }

    public abstract com.ataulm.chunks.Id id();

    public abstract String value();

    public abstract com.ataulm.chunks.Day day();

    public abstract Optional<String> completedTimestamp();

}

