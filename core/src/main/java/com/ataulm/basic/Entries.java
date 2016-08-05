package com.ataulm.basic;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Entries {

    public static Entries create(String modifiedTimestamp, List<Entry> entries) {
        return new AutoValue_Entries(modifiedTimestamp, entries);
    }

    public abstract String modifiedTimestamp();

    public abstract List<Entry> list();

    protected Entries() {
        // use static factory
    }

}

