package com.ataulm.chunks;

public final class ItemFixtures {

    private Id id;
    private String value;
    private String completedTimestamp;

    public static ItemFixtures anItem() {
        return new ItemFixtures(
                Id.Companion.create(),
                "todo item",
                null
        );
    }

    private ItemFixtures(Id id, String value, String completedTimestamp) {
        this.id = id;
        this.value = value;
        this.completedTimestamp = completedTimestamp;
    }

    public ItemFixtures withId(Id id) {
        this.id = id;
        return this;
    }

    public ItemFixtures withValue(String value) {
        this.value = value;
        return this;
    }

    public ItemFixtures withCompletedTimestamp(String completedTimestamp) {
        this.completedTimestamp = completedTimestamp;
        return this;
    }

    public Item get() {
        return Item.Companion.createFrom(id, value, completedTimestamp);
    }

}
