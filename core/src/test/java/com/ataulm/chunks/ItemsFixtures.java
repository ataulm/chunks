package com.ataulm.chunks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ItemsFixtures {

    private List<Item> entries;

    public static ItemsFixtures items() {
        return new ItemsFixtures(
                Collections.<Item>emptyList()
        );
    }

    private ItemsFixtures(List<Item> entries) {
        this.entries = entries;
    }

    public ItemsFixtures with(Item... items) {
        entries = Arrays.asList(items);
        return this;
    }

    public ItemsFixtures with(List<Item> entries) {
        this.entries = entries;
        return this;
    }

    public Items get() {
        return Items.Companion.create(entries);
    }

}
