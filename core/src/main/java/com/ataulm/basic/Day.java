package com.ataulm.basic;

public enum Day {

    YESTERDAY(0),
    TODAY(1),
    TOMORROW(2);

    private final int id;

    Day(int id) {
        this.id = id;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public static Day fromId(String id) {
        for (Day day : values()) {
            if (String.valueOf(day.id) == id) {
                return day;
            }
        }
        throw new IllegalArgumentException("couldn't find a day with id: " + id);
    }

}

