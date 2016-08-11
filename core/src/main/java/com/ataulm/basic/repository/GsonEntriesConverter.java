package com.ataulm.basic.repository;

import com.ataulm.basic.*;

import java.util.ArrayList;
import java.util.List;

public class GsonEntriesConverter {

    private final GsonEntryConverter entryConverter;

    public GsonEntriesConverter(GsonEntryConverter entryConverter) {
        this.entryConverter = entryConverter;
    }

    public GsonEntries convert(Chunks chunks) {
        List<GsonEntry> gsonEntriesList = new ArrayList<>(chunks.yesterday().size() + chunks.today().size() + chunks.tomorrow().size());

        for (Entry entry : chunks.yesterday()) {
            GsonEntry gsonEntry = entryConverter.convert(entry);
            gsonEntriesList.add(gsonEntry);
        }

        for (Entry entry : chunks.today()) {
            GsonEntry gsonEntry = entryConverter.convert(entry);
            gsonEntriesList.add(gsonEntry);
        }

        for (Entry entry : chunks.tomorrow()) {
            GsonEntry gsonEntry = entryConverter.convert(entry);
            gsonEntriesList.add(gsonEntry);
        }

        GsonEntries gsonEntries = new GsonEntries();
        gsonEntries.modifiedTimestamp = chunks.modifiedTimestamp();
        gsonEntries.entries = gsonEntriesList;
        return gsonEntries;
    }

    public Chunks convert(GsonEntries gsonEntries) {
        List<Entry> yesterday = new ArrayList<>();
        List<Entry> today = new ArrayList<>();
        List<Entry> tomorrow = new ArrayList<>();

        for (GsonEntry gsonEntry : gsonEntries.entries) {
            Entry entry = entryConverter.convert(gsonEntry);
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

        Chunk yesterdayChunk = Chunk.create(yesterday);
        Chunk todayChunk = Chunk.create(today);
        Chunk tomorrowChunk = Chunk.create(tomorrow);
        return Chunks.create(gsonEntries.modifiedTimestamp, yesterdayChunk, todayChunk, tomorrowChunk);
    }

}
