package com.ataulm.settings;

import android.support.annotation.IdRes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Settings {

    private final List<Item> settings;

    public Settings(List<Item> settings) {
        this.settings = settings;
    }

    public int size() {
        return settings.size();
    }

    public Item get(int position) {
        return settings.get(position);
    }

    public interface Item {

        int id();

        boolean enabled();

        @IdRes
        int viewType();

        CharSequence title();

    }

    public static class Builder {

        private final List<Item> items = new ArrayList<>();

        public Builder add(Item item) {
            items.add(item);
            return this;
        }

        public Builder add(HeadingItem headingItem, Item... entries) {
            items.add(headingItem);
            Collections.addAll(items, entries);
            // TODO: add artificial gap at the end?
            return this;
        }

        public Settings build() {
            return new Settings(items);
        }

    }

}
