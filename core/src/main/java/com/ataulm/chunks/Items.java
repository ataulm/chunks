package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

@AutoValue
public abstract class Items implements Iterable<Item> {

    public static Items create(List<Item> entries) {
        return new AutoValue_Items(Collections.unmodifiableList(entries));
    }

    public static Items empty() {
        return create(Collections.<Item>emptyList());
    }

    public abstract List<Item> entries();

    protected Items() {
        // use static factory
    }

    public boolean isEmpty() {
        return entries().isEmpty();
    }

    public Item get(int position) {
        return entries().get(position);
    }

    public boolean containsEntryWith(Id id) {
        return findEntryWith(id) != null;
    }

    @Nullable
    public Item findEntryWith(Id id) {
        for (Item item : entries()) {
            if (item.id().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public int size() {
        return entries().size();
    }

    @Override
    public Iterator<Item> iterator() {
        return entries().iterator();
    }

}
