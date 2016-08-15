package com.ataulm.chunks;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Entry {

    public static Entry createNew(String value) {
        return new AutoValue_Entry(Id.create(), value, Optional.<String>absent());
    }

    public static Entry createFrom(Id id, String value) {
        return new AutoValue_Entry(id, value, Optional.<String>absent());
    }

    public static Entry createFrom(Id id, String value, @Nullable String completedTimestamp) {
        return new AutoValue_Entry(id, value, Optional.fromNullable(completedTimestamp));
    }

    public static Entry completed(Entry entry) {
        String completedTimestamp = String.valueOf(System.currentTimeMillis());
        return new AutoValue_Entry(entry.id(), entry.value(), Optional.of(completedTimestamp));
    }

    public static Entry notCompleted(Entry entry) {
        return new AutoValue_Entry(entry.id(), entry.value(), Optional.<String>absent());
    }

    public static Entry edited(Entry entry, String value) {
        return new AutoValue_Entry(entry.id(), value, entry.completedTimestamp());
    }

    public abstract Id id();

    public abstract String value();

    public abstract Optional<String> completedTimestamp();

}

