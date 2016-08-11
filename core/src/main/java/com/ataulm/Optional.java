package com.ataulm;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Optional<T> {

    @SuppressWarnings("unchecked")
    private static final Optional ABSENT = new AutoValue_Optional(null);

    @Nullable
    abstract T data();

    public T get() {
        if (isPresent()) {
            return data();
        } else {
            throw new IllegalStateException("Data not present");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> absent() {
        return ABSENT;
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> fromNullable(@Nullable T data) {
        if (data == null) {
            return ABSENT;
        }
        return new AutoValue_Optional<>(data);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> of(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null. Use Optional.fromNullable(maybeNullData).");
        }
        return new AutoValue_Optional<>(data);
    }

    Optional() {
        // instantiate AutoValue generated class
    }

    public boolean isPresent() {
        return data() != null;
    }

    public T or(T empty) {
        if (isPresent()) {
            return get();
        } else {
            return empty;
        }
    }

}
