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

    public boolean containsEntryWith(Id id) {
        for (Entry entry : values()) {
            if (entry.id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return values().size();
    }

    public Chunk add(Entry entry) {
        if (containsEntryWith(entry.id())) {
            throw new IllegalArgumentException("chunk already contains entry with id: " + entry.id());
        }

        List<Entry> values = new ArrayList<>(size() + 1);
        values.addAll(values());
        values.add(entry);
        return create(values);
    }

    public Chunk add(List<Entry> entries) {
        for (Entry entry : entries) {
            if (containsEntryWith(entry.id())) {
                throw new IllegalArgumentException("chunk already contains entry with id: " + entry.id());
            }
        }

        List<Entry> updatedValues = new ArrayList<>(size() + entries.size());
        updatedValues.addAll(values());
        updatedValues.addAll(entries);
        return create(updatedValues);
    }

    public Chunk remove(Id id) {
        List<Entry> updatedValues = new ArrayList<>(size());
        for (Entry value : values()) {
            if (!value.id().equals(id)) {
                updatedValues.add(value);
            }
        }
        return create(updatedValues);
    }

    public Chunk remove(List<Entry> entries) {
        List<Entry> updatedValues = new ArrayList<>(values());
        updatedValues.removeAll(entries);
        return create(updatedValues);
    }

    public Chunk update(Entry entry) {
        List<Entry> updatedValues = new ArrayList<>(size());
        for (Entry existingValue : values()) {
            if (existingValue.id().equals(entry.id())) {
                updatedValues.add(entry);
            } else {
                updatedValues.add(existingValue);
            }
        }
        return create(updatedValues);
    }

    @Override
    public Iterator<Entry> iterator() {
        return values().iterator();
    }

}

