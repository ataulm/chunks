package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
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

    public Chunk add(Entry entry) {
        if (containsEntryWith(entry.id())) {
            throw new IllegalArgumentException("chunk already contains entry with id: " + entry.id());
        }

        List<Entry> entries = new ArrayList<>(size() + 1);
        entries.addAll(entries());
        entries.add(entry);
        return create(entries);
    }

    public Chunk add(List<Entry> entries) {
        for (Entry entry : entries) {
            if (containsEntryWith(entry.id())) {
                throw new IllegalArgumentException("chunk already contains entry with id: " + entry.id());
            }
        }

        List<Entry> updatedEntries = new ArrayList<>(size() + entries.size());
        updatedEntries.addAll(entries());
        updatedEntries.addAll(entries);
        return create(updatedEntries);
    }

    public Chunk remove(Id id) {
        List<Entry> updatedEntries = new ArrayList<>(size());
        for (Entry value : entries()) {
            if (!value.id().equals(id)) {
                updatedEntries.add(value);
            }
        }
        return create(updatedEntries);
    }

    public Chunk remove(List<Entry> entries) {
        List<Entry> updatedEntries = new ArrayList<>(entries());
        updatedEntries.removeAll(entries);
        return create(updatedEntries);
    }

    public Chunk update(Entry entry) {
        List<Entry> updatedEntries = new ArrayList<>(size());
        for (Entry existingValue : entries()) {
            if (existingValue.id().equals(entry.id())) {
                updatedEntries.add(entry);
            } else {
                updatedEntries.add(existingValue);
            }
        }
        return create(updatedEntries);
    }

    @Override
    public Iterator<Entry> iterator() {
        return entries().iterator();
    }

}

