package com.ataulm.chunks.repository;

import com.ataulm.chunks.Day;
import com.ataulm.chunks.Id;

public class GsonEntryConverter {

    public GsonEntry convert(com.ataulm.chunks.Entry entry) {
        GsonEntry gsonEntry = new GsonEntry();
        gsonEntry.id = String.valueOf(entry.id().value());
        gsonEntry.value = entry.value();
        gsonEntry.day = entry.day().getId();
        gsonEntry.completedTimestamp = entry.completedTimestamp().isPresent() ? entry.completedTimestamp().get() : null;
        return gsonEntry;
    }

    public com.ataulm.chunks.Entry convert(GsonEntry gsonEntry) {
        return com.ataulm.chunks.Entry.createFrom(
                Id.createFrom(gsonEntry.id),
                gsonEntry.value,
                Day.fromId(gsonEntry.day),
                gsonEntry.completedTimestamp
        );
    }

}
