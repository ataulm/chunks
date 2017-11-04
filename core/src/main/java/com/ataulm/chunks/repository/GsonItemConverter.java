package com.ataulm.chunks.repository;

import com.ataulm.chunks.Item;
import com.ataulm.chunks.Id;

public class GsonItemConverter {

    public GsonItem convert(Item item) {
        GsonItem gsonItem = new GsonItem();
        gsonItem.id = String.valueOf(item.id().value());
        gsonItem.value = item.value();
        gsonItem.completedTimestamp = item.completedTimestamp().isPresent() ? item.completedTimestamp().get() : null;
        return gsonItem;
    }

    public Item convert(GsonItem gsonItem) {
        return Item.createFrom(
                Id.createFrom(gsonItem.id),
                gsonItem.value,
                gsonItem.completedTimestamp
        );
    }

}
