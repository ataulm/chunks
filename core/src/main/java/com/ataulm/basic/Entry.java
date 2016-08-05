package com.ataulm.basic;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Entry {

    public static Entry createNew(String value, Day day) {
        return new AutoValue_Entry(Id.create(), value, day, Optional.<String>absent());
    }

    public static Entry createFrom(Id id, String value, Day day) {
        return new AutoValue_Entry(id, value, day, Optional.<String>absent());
    }

    public static Entry createFrom(Id id, String value, Day day, @Nullable String completedTimestamp) {
        return new AutoValue_Entry(id, value, day, Optional.fromNullable(completedTimestamp));
    }

    public static Entry completed(Entry entry, String completedTimestamp) {
        return new AutoValue_Entry(entry.id(), entry.value(), entry.day(), Optional.of(completedTimestamp));
    }

    public static Entry edited(Entry entry, String value) {
        return new AutoValue_Entry(entry.id(), value, entry.day(), entry.completedTimestamp());
    }

    public static Entry transitioned(Entry entry, Day day) {
        return new AutoValue_Entry(entry.id(), entry.value(), day, entry.completedTimestamp());
    }

    public abstract Id id();

    public abstract String value();

    public abstract Day day();

    public abstract Optional<String> completedTimestamp();

}

