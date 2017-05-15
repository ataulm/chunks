package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

@AutoValue
public abstract class Chunk implements Iterable<Entry> {

    public static Chunk create(List<Entry> entries) {
        return new AutoValue_Chunk(Collections.unmodifiableList(entries));
    }

    public static Chunk empty() {
        return create(Collections.<Entry>emptyList());
    }

    public abstract List<Entry> entries();

    protected Chunk() {
        // use static factory
    }

    public boolean isEmpty() {
        return entries().isEmpty();
    }

    public Entry get(int position) {
        return entries().get(position);
    }

    public boolean containsEntryWith(Id id) {
        return findEntryWith(id) != null;
    }

    @Nullable
    public Entry findEntryWith(Id id) {
        for (Entry entry : entries()) {
            if (entry.id().equals(id)) {
                return entry;
            }
        }
        return null;
    }

    public int size() {
        return entries().size();
    }

    @Override
    public Iterator<Entry> iterator() {
        return entries().iterator();
    }

}

