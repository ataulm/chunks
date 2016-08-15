package com.ataulm.chunks;

import org.junit.Test;

import static com.ataulm.chunks.ChunkFixtures.aChunk;
import static com.ataulm.chunks.ChunksFixtures.aChunks;
import static com.ataulm.chunks.EntryFixtures.anEntry;
import static org.fest.assertions.api.Assertions.assertThat;

public class ChunksTest {

    @Test
    public void addEntryToChunks() {
        Chunks chunks = Chunks.empty();
        Entry entry = anEntry().get();

        Chunks updatedChunks = chunks.add(entry, Day.YESTERDAY);

        assertThat(updatedChunks.yesterday()).contains(entry);
    }

    @Test
    public void removeEntryFromChunks() {
        Entry entry = anEntry().get();
        Chunk tomorrow = aChunk().with(entry).get();
        Chunks chunks = aChunks()
                .withTomorrow(tomorrow)
                .get();

        Chunks updatedChunks = chunks.remove(entry);

        assertThat(updatedChunks.tomorrow()).doesNotContain(entry);
    }

}
