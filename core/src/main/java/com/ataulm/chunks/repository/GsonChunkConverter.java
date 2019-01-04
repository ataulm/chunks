package com.ataulm.chunks.repository;

import com.ataulm.chunks.Items;
import com.ataulm.chunks.Item;

import java.util.ArrayList;
import java.util.List;

public class GsonChunkConverter {

    private final GsonItemConverter entryConverter;

    public GsonChunkConverter(GsonItemConverter entryConverter) {
        this.entryConverter = entryConverter;
    }

    public GsonChunk convert(Items items) {
        GsonChunk gsonChunk = new GsonChunk();
        gsonChunk.entries = new ArrayList<>(items.size());
        for (Item item : items) {
            gsonChunk.entries.add(entryConverter.convert(item));
        }
        return gsonChunk;
    }

    public Items convert(GsonChunk gsonChunk) {
        if (gsonChunk == null) {
            return Items.Companion.empty();
        }

        List<Item> entries = new ArrayList<>(gsonChunk.entries.size());
        for (GsonItem gsonItem : gsonChunk.entries) {
            Item item = entryConverter.convert(gsonItem);
            entries.add(item);
        }
        return Items.Companion.create(entries);
    }

}
