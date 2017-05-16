package com.ataulm.chunks;

import org.fest.assertions.core.Condition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ataulm.chunks.ChunkFixtures.aChunk;
import static com.ataulm.chunks.ChunksFixtures.aChunks;
import static com.ataulm.chunks.EntryFixtures.anEntry;
import static org.fest.assertions.api.Assertions.assertThat;

public class ChunksEditorTest {

    private static final ChunkDate AUGUST_02_2016 = ChunkDate.create(1470096000000L);
    private static final ChunkDate AUGUST_03_2016 = ChunkDate.create(1470182400000L);

    ChunksEditor chunksEditor = new ChunksEditor();

    @Test
    public void addingAnEntry() {
        Chunk chunk = aChunk().get();
        Entry entry = anEntry().get();

        Chunk updatedChunk = chunksEditor.add(chunk, entry);

        assertThat(updatedChunk.entries()).containsExactly(entry);
    }

    @Test
    public void addingMultipleEntries() {
        Chunk chunk = aChunk().get();
        Entry one = anEntry().get();
        Entry two = anEntry().get();

        Chunk updatedChunk = chunksEditor.add(chunk, Arrays.asList(one, two));

        assertThat(updatedChunk.entries()).containsExactly(one, two);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEntriesWithSameIdThrowsError() {
        Entry entry = anEntry().get();
        Entry entryWithSameId = anEntry().withId(entry.id()).get();
        Chunk chunk = aChunk().with(entry).get();

        try {
            chunksEditor.add(chunk, entryWithSameId);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(entry.id()));
            throw e;
        }
    }

    @Test
    public void removingAnEntry() {
        Entry entry = anEntry().get();
        Chunk chunk = aChunk().with(entry).get();

        Chunk updatedChunk = chunksEditor.remove(chunk, entry.id());

        assertThat(updatedChunk.containsEntryWith(entry.id())).isFalse();
    }

    @Test
    public void removingNonExistentEntryDoesNothing() {
        Entry entry = anEntry().get();
        Chunk chunk = aChunk().get();

        Chunk updatedChunk = chunksEditor.remove(chunk, entry.id());

        assertThat(updatedChunk.containsEntryWith(entry.id())).isFalse();
    }

    @Test
    public void removingMultipleEntries() {
        Entry one = anEntry().get();
        Entry two = anEntry().get();
        Entry three = anEntry().get();
        Chunk chunk = aChunk().with(Arrays.asList(one, two, three)).get();

        Chunk updatedChunk = chunksEditor.remove(chunk, Arrays.asList(two, three));

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

        Chunk updatedChunk = chunksEditor.remove(chunk, Arrays.asList(four, five));

        assertThat(updatedChunk.entries()).containsExactly(one, two, three);
    }

    @Test
    public void removingMultipleEntriesWithSomeNonExistingEntriesRemovesTheExistingOnes() {
        Entry one = anEntry().get();
        Entry two = anEntry().get();
        Entry three = anEntry().get();
        Entry four = anEntry().get();
        Chunk chunk = aChunk().with(Arrays.asList(one, two, three)).get();

        Chunk updatedChunk = chunksEditor.remove(chunk, Arrays.asList(two, three, four));

        assertThat(updatedChunk.entries()).containsExactly(one);
    }

    @Test
    public void updatingAnEntry() {
        Entry initial = anEntry().withValue("initial").get();
        Chunk chunk = aChunk().with(initial).get();
        Entry updated = anEntry().withId(initial.id()).withValue("updated").get();

        Chunk updatedChunk = chunksEditor.update(chunk, updated);

        assertThat(updatedChunk.findEntryWith(initial.id()).value()).isEqualTo("updated");
    }

    @Test
    public void updatingNonExistentEntryDoesNothing() {
        Chunk chunk = aChunk().get();
        Entry updated = anEntry().get();

        chunksEditor.update(chunk, updated);

        assertThat(chunk.isEmpty());
    }

    @Test
    public void addingAnEntryToDay() {
        Entry entry = anEntry().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunksEditor.add(chunks, Day.TOMORROW, entry);

        assertThat(updatedChunks.tomorrow().entries()).containsExactly(entry);
    }

    @Test
    public void addingMultipleEntriesToDay() {
        Entry one = anEntry().get();
        Entry two = anEntry().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunksEditor.add(chunks, Day.TOMORROW, Arrays.asList(one, two));

        assertThat(updatedChunks.tomorrow().entries()).containsExactly(one, two);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEntriesWithSameIdToDayThrowsError() {
        Entry entry = anEntry().get();
        Entry entryWithSameId = anEntry().withId(entry.id()).get();
        Chunk today = aChunk().with(entry).get();
        Chunks chunks = aChunks().withToday(today).get();

        try {
            chunksEditor.add(chunks, Day.TODAY, entryWithSameId);
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
            chunksEditor.add(chunks, Day.TOMORROW, entryWithSameId);
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

        Chunks updatedChunks = chunksEditor.edit(chunks, entry.id());

        assertThat(updatedChunks.today().entries()).isEmpty();
        assertThat(updatedChunks.input().get()).isEqualTo(entry.value());
    }

    @Test
    public void editingNonExistentEntryDoesNothing() {
        Entry entry = anEntry().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunksEditor.edit(chunks, entry.id());

        assertThat(updatedChunks).isEqualTo(chunks);
        assertThat(updatedChunks.input().isPresent()).isFalse();
    }

    @Test
    public void removingAnEntryFromChunks() {
        Entry entry = anEntry().get();
        Chunk today = aChunk().with(entry).get();
        Chunks chunks = aChunks().withToday(today).get();

        Chunks updatedChunks = chunksEditor.remove(chunks, entry.id());

        assertThat(updatedChunks.today()).isEmpty();
    }

    @Test
    public void removingNonExistentEntryFromChunksDoesNothing() {
        Entry entry = anEntry().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunksEditor.remove(chunks, entry.id());

        assertThat(updatedChunks).isEqualTo(chunks);
    }

    @Test
    public void updatingAnEntryToDay() {
        Entry initial = anEntry().withValue("initial").get();
        Chunk today = aChunk().with(initial).get();
        Chunks chunks = aChunks().withToday(today).get();
        Entry updated = anEntry().withId(initial.id()).withValue("updated").get();

        Chunks updatedChunks = chunksEditor.update(chunks, updated);

        assertThat(updatedChunks.today()).containsExactly(updated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatingNonExistentThrowsError() {
        Chunks chunks = aChunks().get();
        Entry entry = anEntry().get();

        try {
            chunksEditor.update(chunks, entry);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(entry.id()));
            throw e;
        }
    }

    @Test
    public void shuffle_along_with_different_day_moves_all_completed_tasks() {
        List<Entry> completeTasks = createNewListOfCompleteNewEntries();
        List<Entry> incompleteTasks = createNewListOfIncompleteNewEntries();
        List<Entry> todayTasks = new ArrayList<>();
        todayTasks.addAll(completeTasks);
        todayTasks.addAll(incompleteTasks);
        Chunk today = aChunk().with(todayTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, Chunk.empty(), Chunk.empty());

        Chunks updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016);

        assertThat(updatedChunks.today().entries()).isEqualTo(incompleteTasks);
    }

    @Test
    public void shuffle_along_with_different_day_moves_all_incomplete_tasks_to_today() {
        List<Entry> todayTasks = createNewListOfIncompleteNewEntries();
        List<Entry> tomorrowTasks = createNewListOfIncompleteNewEntries();
        Chunk today = aChunk().with(todayTasks).get();
        Chunk tomorrow = aChunk().with(tomorrowTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, Chunk.empty());

        Chunks updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016);

        assertThat(updatedChunks).is(withNoTasksInTomorrowAndTodayContains(tomorrowTasks));
    }

    @Test
    public void shuffle_along_with_different_day_removes_all_completed_tasks_from_tomorrow() {
        List<Entry> todayTasks = createNewListOfIncompleteNewEntries();
        Entry completed = anEntry().withCompletedTimestamp("0").get();
        List<Entry> tomorrowTasks = createNewListOfIncompleteNewEntriesAnd(completed);
        Chunk today = aChunk().with(todayTasks).get();
        Chunk tomorrow = aChunk().with(tomorrowTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, Chunk.empty());

        Chunks updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016);

        assertThat(updatedChunks).is(without(completed));
    }

    @Test
    public void shuffle_along_with_different_day_removes_all_completed_tasks_from_sometime() {
        List<Entry> todayTasks = createNewListOfIncompleteNewEntries();
        List<Entry> tomorrowTasks = createNewListOfIncompleteNewEntries();
        Entry completed = anEntry().withCompletedTimestamp("0").get();
        List<Entry> sometimeTasks = createNewListOfIncompleteNewEntriesAnd(completed);
        Chunk today = aChunk().with(todayTasks).get();
        Chunk tomorrow = aChunk().with(tomorrowTasks).get();
        Chunk sometime = aChunk().with(sometimeTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, sometime);

        Chunks updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016);

        assertThat(updatedChunks).is(without(completed));
    }

    private static Condition<Chunks> without(final Entry entry) {
        return new Condition<Chunks>() {
            @Override
            public boolean matches(Chunks chunks) {
                boolean chunksContainsEntry = chunks.today().containsEntryWith(entry.id())
                        || chunks.tomorrow().containsEntryWith(entry.id())
                        || chunks.sometime().containsEntryWith(entry.id());
                return !chunksContainsEntry;
            }
        };
    }

    private static Condition<Chunks> withNoTasksInTomorrowAndTodayContains(final List<Entry> tomorrowTasks) {
        return new Condition<Chunks>() {
            @Override
            public boolean matches(Chunks value) {
                return value.today().entries().containsAll(tomorrowTasks)
                        && value.tomorrow().isEmpty();
            }
        };
    }

    private static List<Entry> createNewListOfCompleteNewEntries() {
        String timestamp_1970 = "0";
        Entry oneComplete = anEntry().withCompletedTimestamp(timestamp_1970).get();
        Entry twoComplete = anEntry().withCompletedTimestamp(timestamp_1970).get();
        Entry threeComplete = anEntry().withCompletedTimestamp(timestamp_1970).get();
        Entry fourComplete = anEntry().withCompletedTimestamp(timestamp_1970).get();
        return Arrays.asList(oneComplete, twoComplete, threeComplete, fourComplete);
    }

    private static List<Entry> createNewListOfIncompleteNewEntries() {
        return createNewListOfIncompleteNewEntriesAnd();
    }

    private static List<Entry> createNewListOfIncompleteNewEntriesAnd(Entry... entries) {
        List<Entry> all = new ArrayList<>(Arrays.asList(entries));

        Entry oneIncomplete = anEntry().get();
        Entry twoIncomplete = anEntry().get();
        Entry threeIncomplete = anEntry().get();
        Entry fourIncomplete = anEntry().get();

        Collections.addAll(all, oneIncomplete, twoIncomplete, threeIncomplete, fourIncomplete);
        return all;
    }

}
