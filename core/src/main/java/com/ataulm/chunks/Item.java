package com.ataulm.chunks;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Item {

    public static Item createNew(String value) {
        return createFrom(Id.Companion.create(), value);
    }

    public static Item createFrom(Id id, String value) {
        return createFrom(id, value, null);
    }

    public static Item createFrom(Id id, String value, @Nullable String completedTimestamp) {
        return new AutoValue_Item(id, value, Optional.fromNullable(completedTimestamp));
    }

    public Item markCompleted() {
        String completedTimestamp = String.valueOf(System.currentTimeMillis());
        return createFrom(id(), value(), completedTimestamp);
    }

    public Item markNotComplete() {
        return createFrom(id(), value());
    }

    public abstract Id id();

    public abstract String value();

    public abstract Optional<String> completedTimestamp();

    public boolean isCompleted() {
        return completedTimestamp().isPresent();
    }
}

