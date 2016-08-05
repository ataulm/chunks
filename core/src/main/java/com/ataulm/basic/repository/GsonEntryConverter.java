package com.ataulm.basic.repository;

import com.ataulm.basic.Day;
import com.ataulm.basic.Entry;
import com.ataulm.basic.GsonEntry;
import com.ataulm.basic.Id;

class GsonEntryConverter {

    public GsonEntry convert(Entry entry) {
        GsonEntry gsonEntry = new GsonEntry();
        gsonEntry.id = String.valueOf(entry.id().value());
        gsonEntry.value = entry.value();
        gsonEntry.day = entry.day().getId();
        gsonEntry.completedTimestamp = entry.completedTimestamp().isPresent() ? entry.completedTimestamp().get() : null;
        return gsonEntry;
    }

    public Entry convert(GsonEntry gsonEntry) {
        return Entry.createFrom(
                Id.createFrom(gsonEntry.id),
                gsonEntry.value,
                Day.fromId(gsonEntry.day),
                gsonEntry.completedTimestamp
        );
    }

}
