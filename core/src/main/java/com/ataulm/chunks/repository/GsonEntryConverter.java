package com.ataulm.chunks.repository;

import com.ataulm.chunks.Entry;
import com.ataulm.chunks.Id;

public class GsonEntryConverter {

    public GsonEntry convert(Entry entry) {
        GsonEntry gsonEntry = new GsonEntry();
        gsonEntry.id = String.valueOf(entry.id().value());
        gsonEntry.value = entry.value();
        gsonEntry.completedTimestamp = entry.completedTimestamp().isPresent() ? entry.completedTimestamp().get() : null;
        return gsonEntry;
    }

    public com.ataulm.chunks.Entry convert(GsonEntry gsonEntry) {
        return Entry.createFrom(
                Id.createFrom(gsonEntry.id),
                gsonEntry.value,
                gsonEntry.completedTimestamp
        );
    }

}
