package com.ataulm.chunks;

import org.junit.Test;

import static com.ataulm.chunks.EntryFixtures.anEntry;
import static org.fest.assertions.api.Assertions.assertThat;

public class ChunksShould {

    @Test
    public void returnChunksWithEntryAdded() {
        Chunks chunks = Chunks.empty();
        Entry entry = anEntry().with(Day.TODAY).build();

        Chunks updatedChunks = chunks.add(entry);

        assertThat(updatedChunks.today()).contains(entry);
    }

    @Test
    public void returnChunksWithEntryAddedToTomorrow() {
        Chunks chunks = Chunks.empty();
        Entry entry = anEntry().with(Day.TOMORROW).build();

        Chunks updatedChunks = chunks.add(entry);

        assertThat(updatedChunks.tomorrow()).contains(entry);
    }

    @Test
    public void foo() {
        Chunks chunks = Chunks.empty();
        Entry entry = anEntry().with(Day.TODAY).build();
        chunks.add(entry);

        Entry updatedEntry = Entry.transitioned(entry, Day.TOMORROW);
        chunks.update(updatedEntry);

        Chunks updatedChunks = chunks.add(entry);

        assertThat(updatedChunks.tomorrow()).contains(entry);
    }






}
