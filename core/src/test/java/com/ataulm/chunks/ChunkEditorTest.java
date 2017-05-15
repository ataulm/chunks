package com.ataulm.chunks;

import org.junit.Test;

import java.util.Arrays;

import static com.ataulm.chunks.ChunkFixtures.aChunk;
import static com.ataulm.chunks.EntryFixtures.anEntry;
import static org.fest.assertions.api.Assertions.assertThat;

public class ChunkEditorTest {

    ChunkEditor chunkEditor = new ChunkEditor();

    @Test
    public void addingAnEntry() {
        Chunk chunk = aChunk().get();
        Entry entry = anEntry().get();

        Chunk updatedChunk = chunkEditor.add(chunk, entry);

        assertThat(updatedChunk.containsEntryWith(entry.id()));
    }

    @Test
    public void addingDuplicatesThrowsError() {
        Entry entry = anEntry().get();
        Chunk chunk = aChunk().with(entry).get();

        try {
            chunkEditor.add(chunk, entry);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(entry.id()));
        }
    }

    @Test
    public void removingAnEntry() {
        Entry entry = anEntry().get();
        Chunk chunk = aChunk().with(entry).get();

        Chunk updatedChunk = chunkEditor.remove(chunk, entry.id());

        assertThat(updatedChunk.containsEntryWith(entry.id())).isFalse();
    }

    @Test
    public void removingNonExistentEntryDoesNothing() {
        Entry entry = anEntry().get();
        Chunk chunk = aChunk().get();

        Chunk updatedChunk = chunkEditor.remove(chunk, entry.id());

        assertThat(updatedChunk.containsEntryWith(entry.id())).isFalse();
    }

    @Test
    public void removingMultipleEntries() {
        Entry one = anEntry().get();
        Entry two = anEntry().get();
        Entry three = anEntry().get();
        Chunk chunk = aChunk().with(Arrays.asList(one, two, three)).get();

        Chunk updatedChunk = chunkEditor.remove(chunk, Arrays.asList(two, three));

        assertThat(updatedChunk.entries()).containsExactly(one);
    }

    @Test
    public void removingMultipleEntriesWithNonExistingEntriesDoesNothing() {
        Entry one = anEntry().get();
        Entry two = anEntry().get();
        Entry three = anEntry().get();
        Entry four = anEntry().get();
        Entry five = anEntry().get();
        Chunk chunk = aChunk().with(Arrays.asList(one, two, three)).get();

        Chunk updatedChunk = chunkEditor.remove(chunk, Arrays.asList(four, five));

        assertThat(updatedChunk.entries()).containsExactly(one, two, three);
    }

    @Test
    public void removingMultipleEntriesWithSomeNonExistingEntriesRemovesTheExistingOnes() {
        Entry one = anEntry().get();
        Entry two = anEntry().get();
        Entry three = anEntry().get();
        Entry four = anEntry().get();
        Chunk chunk = aChunk().with(Arrays.asList(one, two, three)).get();

        Chunk updatedChunk = chunkEditor.remove(chunk, Arrays.asList(two, three, four));

        assertThat(updatedChunk.entries()).containsExactly(one);
    }

    @Test
    public void updatingAnEntry() {
        Entry initial = anEntry().withValue("initial").get();
        Chunk chunk = aChunk().with(initial).get();
        Entry updated = anEntry().withId(initial.id()).withValue("updated").get();

        Chunk updatedChunk = chunkEditor.update(chunk, updated);

        assertThat(updatedChunk.findEntryWith(initial.id()).value()).isEqualTo("updated");
    }

    @Test
    public void updatingNonExistentEntryDoesNothing() {
        Chunk chunk = aChunk().get();
        Entry updated = anEntry().get();

        chunkEditor.update(chunk, updated);

        assertThat(chunk.isEmpty());
    }

}
