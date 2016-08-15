package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@AutoValue
public abstract class Chunk implements Iterable<Entry> {

    public static Chunk create(List<Entry> entries) {
        return new AutoValue_Chunk(Collections.unmodifiableList(entries));
    }

    public static Chunk empty() {
        return create(Collections.<Entry>emptyList());
    }

    abstract List<Entry> values();

    protected Chunk() {
        // use static factory
    }

    public boolean isEmpty() {
        return values().isEmpty();
    }

    public Entry get(int position) {
        return values().get(position);
    }

    public boolean contains(Entry entry) {
        for (Entry value : values()) {
            if (value.id().equals(entry.id())) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return values().size();
    }

    public Chunk add(Entry entry) {
        List<Entry> values = new ArrayList<>(size() + 1);
        values.addAll(values());
        values.add(entry);
        return create(values);
    }

    public Chunk remove(Entry entry) {
        if (!contains(entry)) {
            throw new IllegalArgumentException("Chunk doesn't contain Entry: " + entry);
        }

        List<Entry> values = new ArrayList<>(size() - 1);
        for (Entry value : values()) {
            if (!value.equals(entry)) {
                values.add(value);
            }
        }
        return create(values);
    }

    public Chunk update(Entry modifiedEntry) {
        if (!contains(modifiedEntry)) {
            throw new IllegalArgumentException("Chunk doesn't contain Entry: " + modifiedEntry);
        }

        List<Entry> values = new ArrayList<>(size() - 1);
        for (Entry existingValue : values()) {
            if (existingValue.id().equals(modifiedEntry.id())) {
                values.add(modifiedEntry);
            } else {
                values.add(existingValue);
            }
        }
        return create(values);
    }

    @Override
    public Iterator<Entry> iterator() {
        return values().iterator();
    }
}

