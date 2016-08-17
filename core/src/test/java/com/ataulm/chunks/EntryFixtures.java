package com.ataulm.chunks;

public final class EntryFixtures {

    private Id id;
    private String value;
    private String completedTimestamp;

    public static EntryFixtures anEntry() {
        return new EntryFixtures(
                Id.create(),
                "todo item",
                null
        );
    }

    private EntryFixtures(Id id, String value, String completedTimestamp) {
        this.id = id;
        this.value = value;
        this.completedTimestamp = completedTimestamp;
    }

    public EntryFixtures withId(Id id) {
        this.id = id;
        return this;
    }

    public EntryFixtures withValue(String value) {
        this.value = value;
        return this;
    }

    public EntryFixtures withCompletedTimestamp(String completedTimestamp) {
        this.completedTimestamp = completedTimestamp;
        return this;
    }

    public Entry get() {
        return Entry.createFrom(id, value, completedTimestamp);
    }

}
