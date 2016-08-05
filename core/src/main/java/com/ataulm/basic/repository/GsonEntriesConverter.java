package com.ataulm.basic.repository;

import com.ataulm.basic.*;

import java.util.ArrayList;
import java.util.List;

public class GsonEntriesConverter {

    private final GsonEntryConverter entryConverter;

    public GsonEntriesConverter(GsonEntryConverter entryConverter) {
        this.entryConverter = entryConverter;
    }

    public GsonEntries convert(Entries entries) {
        List<GsonEntry> gsonEntriesList = new ArrayList<>(entries.list().size());
        for (Entry entry : entries.list()) {
            GsonEntry gsonEntry = entryConverter.convert(entry);
            gsonEntriesList.add(gsonEntry);
        }
        GsonEntries gsonEntries = new GsonEntries();
        gsonEntries.modifiedTimestamp = entries.modifiedTimestamp();
        gsonEntries.entries = gsonEntriesList;
        return gsonEntries;
    }

    public Entries convert(GsonEntries gsonEntries) {
        List<Entry> entriesList = new ArrayList<>(gsonEntries.entries.size());
        for (GsonEntry gsonEntry : gsonEntries.entries) {
            Entry entry = entryConverter.convert(gsonEntry);
            entriesList.add(entry);
        }
        return Entries.create(gsonEntries.modifiedTimestamp, entriesList);
    }

}
