package com.ataulm;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Event<T> {

    public static <T> Event<T> loading() {
        return loading(null);
    }

    public static <T> Event<T> loading(T data) {
        return new AutoValue_Event<>(Type.LOADING, Optional.fromNullable(data), Optional.<Throwable>absent());
    }

    public static <T> Event<T> idle() {
        return idle(null);
    }

    public static <T> Event<T> idle(T data) {
        return new AutoValue_Event<>(Type.IDLE, Optional.fromNullable(data), Optional.<Throwable>absent());
    }

    public static <T> Event<T> error(Throwable error) {
        return error(null, error);
    }

    public static <T> Event<T> error(T data, Throwable error) {
        if (error == null) {
            throw new IllegalArgumentException("Error events must contain an error.");
        }
        return new AutoValue_Event<>(Type.ERROR, Optional.fromNullable(data), Optional.of(error));
    }

    public abstract Type getType();

    public abstract Optional<T> getData();

    public abstract Optional<Throwable> getError();

    Event() {
        // instantiate AutoValue generated class
    }

    public enum Type {

        LOADING,
        ERROR,
        IDLE

    }

}
