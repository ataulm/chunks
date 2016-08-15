package com.ataulm.chunks;

public final class EntryFixtures {

    private String value;

    public static EntryFixtures anEntry() {
        return new EntryFixtures(
                "todo item"
        );
    }

    private EntryFixtures(String value) {
        this.value = value;
    }

    public EntryFixtures withValue(String value) {
        this.value = value;
        return this;
    }

    public Entry get() {
        return Entry.createNew(value);
    }

}
