package com.ataulm.chunks;

public class EntryFixtures {

    private String value;
    private Day day;

    public static EntryFixtures anEntry() {
        return new EntryFixtures(
                "todo item",
                Day.TODAY
        );
    }

    public EntryFixtures(String value, Day day) {
        this.value = value;
        this.day = day;
    }

    public EntryFixtures withValue(String value) {
        this.value = value;
        return this;
    }

    public EntryFixtures with(Day day) {
        this.day = day;
        return this;
    }

    public Entry build() {
        return Entry.createNew(value, day);
    }

}
