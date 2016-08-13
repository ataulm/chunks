package com.ataulm.chunks.repository;

import java.util.ArrayList;
import java.util.List;

public class GsonEntriesConverter {

    private final GsonEntryConverter entryConverter;

    public GsonEntriesConverter(GsonEntryConverter entryConverter) {
        this.entryConverter = entryConverter;
    }

    public com.ataulm.chunks.repository.GsonEntries convert(com.ataulm.chunks.Chunks chunks) {
        List<com.ataulm.chunks.repository.GsonEntry> gsonEntriesList = new ArrayList<>(chunks.yesterday().size() + chunks.today().size() + chunks.tomorrow().size());

        for (com.ataulm.chunks.Entry entry : chunks.yesterday()) {
            com.ataulm.chunks.repository.GsonEntry gsonEntry = entryConverter.convert(entry);
            gsonEntriesList.add(gsonEntry);
        }

        for (com.ataulm.chunks.Entry entry : chunks.today()) {
            com.ataulm.chunks.repository.GsonEntry gsonEntry = entryConverter.convert(entry);
            gsonEntriesList.add(gsonEntry);
        }

        for (com.ataulm.chunks.Entry entry : chunks.tomorrow()) {
            com.ataulm.chunks.repository.GsonEntry gsonEntry = entryConverter.convert(entry);
            gsonEntriesList.add(gsonEntry);
        }

        com.ataulm.chunks.repository.GsonEntries gsonEntries = new com.ataulm.chunks.repository.GsonEntries();
        gsonEntries.modifiedTimestamp = chunks.modifiedTimestamp();
        gsonEntries.entries = gsonEntriesList;
        return gsonEntries;
    }

    public com.ataulm.chunks.Chunks convert(com.ataulm.chunks.repository.GsonEntries gsonEntries) {
        List<com.ataulm.chunks.Entry> yesterday = new ArrayList<>();
        List<com.ataulm.chunks.Entry> today = new ArrayList<>();
        List<com.ataulm.chunks.Entry> tomorrow = new ArrayList<>();

        for (com.ataulm.chunks.repository.GsonEntry gsonEntry : gsonEntries.entries) {
            com.ataulm.chunks.Entry entry = entryConverter.convert(gsonEntry);
            switch (entry.day()) {
                case YESTERDAY:
                    yesterday.add(entry);
                    break;
                case TODAY:
                    today.add(entry);
                    break;
                case TOMORROW:
                    tomorrow.add(entry);
                    break;
                default:
                    throw new IllegalArgumentException("no support for " + entry.day());
            }
        }

        com.ataulm.chunks.Chunk yesterdayChunk = com.ataulm.chunks.Chunk.create(yesterday);
        com.ataulm.chunks.Chunk todayChunk = com.ataulm.chunks.Chunk.create(today);
        com.ataulm.chunks.Chunk tomorrowChunk = com.ataulm.chunks.Chunk.create(tomorrow);
        return com.ataulm.chunks.Chunks.create(gsonEntries.modifiedTimestamp, yesterdayChunk, todayChunk, tomorrowChunk);
    }

}
