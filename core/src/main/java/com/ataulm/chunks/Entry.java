package com.ataulm.chunks;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Entry {

    public static Entry createNew(String value) {
        return createFrom(Id.create(), value);
    }

    public static Entry createFrom(Id id, String value) {
        return createFrom(id, value, null);
    }

    public static Entry createFrom(Id id, String value, @Nullable String completedTimestamp) {
        return new AutoValue_Entry(id, value, Optional.fromNullable(completedTimestamp));
    }

    public Entry markCompleted() {
        String completedTimestamp = String.valueOf(System.currentTimeMillis());
        return createFrom(id(), value(), completedTimestamp);
    }

    public Entry markNotComplete() {
        return createFrom(id(), value());
    }

    public abstract Id id();

    public abstract String value();

    public abstract Optional<String> completedTimestamp();

    public boolean isCompleted() {
        return completedTimestamp().isPresent();
    }

}

