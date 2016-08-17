package com.ataulm.chunks;

public enum Day {

    TODAY("1"),
    TOMORROW("2");

    private final String id;

    Day(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Day fromId(String id) {
        for (Day day : values()) {
            if (String.valueOf(day.id).equals(id)) {
                return day;
            }
        }
        throw new IllegalArgumentException("couldn't find a day with id: " + id);
    }

}

