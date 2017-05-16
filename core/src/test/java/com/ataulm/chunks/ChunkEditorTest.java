package com.ataulm.chunks;

import org.junit.Test;

import java.util.Arrays;

import static com.ataulm.chunks.ChunkFixtures.aChunk;
import static com.ataulm.chunks.ChunksFixtures.aChunks;
import static com.ataulm.chunks.EntryFixtures.anEntry;
import static org.fest.assertions.api.Assertions.assertThat;

public class ChunkEditorTest {

    ChunkEditor chunkEditor = new ChunkEditor();

    @Test
    public void addingAnEntry() {
        Chunk chunk = aChunk().get();
        Entry entry = anEntry().get();

        Chunk updatedChunk = chunkEditor.add(chunk, entry);

        assertThat(updatedChunk.entries()).containsExactly(entry);
    }

    @Test
    public void addingMultipleEntries() {
        Chunk chunk = aChunk().get();
        Entry one = anEntry().get();
        Entry two = anEntry().get();

        Chunk updatedChunk = chunkEditor.add(chunk, Arrays.asList(one, two));

        assertThat(updatedChunk.entries()).containsExactly(one, two);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEntriesWithSameIdThrowsError() {
        Entry entry = anEntry().get();
        Entry entryWithSameId = anEntry().withId(entry.id()).get();
        Chunk chunk = aChunk().with(entry).get();

        try {
            chunkEditor.add(chunk, entryWithSameId);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(entry.id()));
            throw e;
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

    @Test
    public void addingAnEntryToDay() {
        Entry entry = anEntry().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunkEditor.add(chunks, Day.TOMORROW, entry);

        assertThat(updatedChunks.tomorrow().entries()).containsExactly(entry);
    }

    @Test
    public void addingMultipleEntriesToDay() {
        Entry one = anEntry().get();
        Entry two = anEntry().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunkEditor.add(chunks, Day.TOMORROW, Arrays.asList(one, two));

        assertThat(updatedChunks.tomorrow().entries()).containsExactly(one, two);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEntriesWithSameIdToDayThrowsError() {
        Entry entry = anEntry().get();
        Entry entryWithSameId = anEntry().withId(entry.id()).get();
        Chunk today = aChunk().with(entry).get();
        Chunks chunks = aChunks().withToday(today).get();

        try {
            chunkEditor.add(chunks, Day.TODAY, entryWithSameId);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(entry.id()));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEntriesWithSameIdAcrossDaysThrowsError() {
        Entry entry = anEntry().get();
        Entry entryWithSameId = anEntry().withId(entry.id()).get();
        Chunk today = aChunk().with(entry).get();
        Chunks chunks = aChunks().withToday(today).get();

        try {
            chunkEditor.add(chunks, Day.TOMORROW, entryWithSameId);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(entry.id()));
            throw e;
        }
    }

    @Test
    public void editingEntryRemovesItFromDayAndPassesTheValue() {
        Entry entry = anEntry().get();
        Chunk today = aChunk().with(entry).get();
        Chunks chunks = aChunks().withToday(today).get();

        Chunks updatedChunks = chunkEditor.edit(chunks, entry.id());

        assertThat(updatedChunks.today().entries()).isEmpty();
        assertThat(updatedChunks.input().get()).isEqualTo(entry.value());
    }

    @Test
    public void editingNonExistentEntryDoesNothing() {
        Entry entry = anEntry().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunkEditor.edit(chunks, entry.id());

        assertThat(updatedChunks).isEqualTo(chunks);
        assertThat(updatedChunks.input().isPresent()).isFalse();
    }

    @Test
    public void removingAnEntryFromChunks() {
        Entry entry = anEntry().get();
        Chunk today = aChunk().with(entry).get();
        Chunks chunks = aChunks().withToday(today).get();

        Chunks updatedChunks = chunkEditor.remove(chunks, entry.id());

        assertThat(updatedChunks.today()).isEmpty();
    }

    @Test
    public void removingNonExistentEntryFromChunksDoesNothing() {
        Entry entry = anEntry().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunkEditor.remove(chunks, entry.id());

        assertThat(updatedChunks).isEqualTo(chunks);
    }

    @Test
    public void updatingAnEntryToDay() {
        Entry initial = anEntry().withValue("initial").get();
        Chunk today = aChunk().with(initial).get();
        Chunks chunks = aChunks().withToday(today).get();
        Entry updated = anEntry().withId(initial.id()).withValue("updated").get();

        Chunks updatedChunks = chunkEditor.update(chunks, updated);

        assertThat(updatedChunks.today()).containsExactly(updated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatingNonExistentThrowsError() {
        Chunks chunks = aChunks().get();
        Entry entry = anEntry().get();

        try {
            chunkEditor.update(chunks, entry);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(entry.id()));
            throw e;
        }
    }

}
