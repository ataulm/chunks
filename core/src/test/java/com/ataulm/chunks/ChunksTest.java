package com.ataulm.chunks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fest.assertions.core.Condition;
import org.junit.Test;

import static com.ataulm.chunks.ChunkFixtures.aChunk;
import static com.ataulm.chunks.EntryFixtures.anEntry;
import static org.fest.assertions.api.Assertions.assertThat;

public class ChunksTest {

    private static final ChunkDate AUGUST_02_2016 = ChunkDate.create(1470096000000L);
    private static final ChunkDate AUGUST_03_2016 = ChunkDate.create(1470182400000L);

    private static final List<Entry> ENTRIES_TODAY = Arrays.asList(
            anEntry().get(),
            anEntry().get(),
            anEntry().get()
    );

    private static final List<Entry> ENTRIES_TOMORROW = Arrays.asList(
            anEntry().get(),
            anEntry().get(),
            anEntry().get()
    );

    private static final List<Entry> ENTRIES_SOMETIME = Arrays.asList(
            anEntry().get(),
            anEntry().get(),
            anEntry().get()
    );

    @Test
    public void add_entry_to_today() {
        Chunk today = aChunk().with(ENTRIES_TODAY).get();
        Chunk tomorrow = aChunk().with(ENTRIES_TOMORROW).get();
        Chunk sometime = aChunk().with(ENTRIES_SOMETIME).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, sometime);
        Entry entry = anEntry().get();

        Chunks updatedChunks = chunks.add(entry, Day.TODAY);

        assertThat(updatedChunks.today()).contains(entry);
    }

    @Test
    public void add_entry_to_tomorrow() {
        Chunk today = aChunk().with(ENTRIES_TODAY).get();
        Chunk tomorrow = aChunk().with(ENTRIES_TOMORROW).get();
        Chunk sometime = aChunk().with(ENTRIES_SOMETIME).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, sometime);
        Entry entry = anEntry().get();

        Chunks updatedChunks = chunks.add(entry, Day.TOMORROW);

        assertThat(updatedChunks.tomorrow()).contains(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_entry_complains_if_duplicate() {
        Entry entry = anEntry().get();
        Chunk today = Chunk.create(Collections.singletonList(entry));
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, Chunk.empty(), Chunk.empty());

        chunks.add(entry, Day.TODAY);
    }

    @Test
    public void remove_entry_from_today() {
        Entry entry = anEntry().get();
        Chunk today = aChunk().with(entry).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, Chunk.empty(), Chunk.empty());

        // TODO: if this is how to remove, then duplicate entries should not be allowed even across days
        Chunks updatedChunks = chunks.remove(entry.id());

        assertThat(updatedChunks.today()).doesNotContain(entry);
    }

    @Test
    public void remove_entry_from_tomorrow() {
        Entry entry = anEntry().get();
        Chunk tomorrow = aChunk().with(entry).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, Chunk.empty(), tomorrow, Chunk.empty());

        Chunks updatedChunks = chunks.remove(entry.id());

        assertThat(updatedChunks.today()).doesNotContain(entry);
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

        Chunks updatedChunks = chunks.shuffleAlong(AUGUST_03_2016);

        assertThat(updatedChunks.today().entries()).isEqualTo(incompleteTasks);
    }

    @Test
    public void shuffle_along_with_different_day_moves_all_incomplete_tasks_to_today() {
        List<Entry> todayTasks = createNewListOfIncompleteNewEntries();
        List<Entry> tomorrowTasks = createNewListOfIncompleteNewEntries();
        Chunk today = aChunk().with(todayTasks).get();
        Chunk tomorrow = aChunk().with(tomorrowTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, Chunk.empty());

        Chunks updatedChunks = chunks.shuffleAlong(AUGUST_03_2016);

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

        Chunks updatedChunks = chunks.shuffleAlong(AUGUST_03_2016);

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

    private List<Entry> createNewListOfCompleteNewEntries() {
        String timestamp_1970 = "0";
        Entry oneComplete = anEntry().withCompletedTimestamp(timestamp_1970).get();
        Entry twoComplete = anEntry().withCompletedTimestamp(timestamp_1970).get();
        Entry threeComplete = anEntry().withCompletedTimestamp(timestamp_1970).get();
        Entry fourComplete = anEntry().withCompletedTimestamp(timestamp_1970).get();
        return Arrays.asList(oneComplete, twoComplete, threeComplete, fourComplete);
    }

    private List<Entry> createNewListOfIncompleteNewEntries() {
        return createNewListOfIncompleteNewEntriesAnd();
    }

    private List<Entry> createNewListOfIncompleteNewEntriesAnd(Entry... entries) {
        List<Entry> all = new ArrayList<>(Arrays.asList(entries));

        Entry oneIncomplete = anEntry().get();
        Entry twoIncomplete = anEntry().get();
        Entry threeIncomplete = anEntry().get();
        Entry fourIncomplete = anEntry().get();

        Collections.addAll(all, oneIncomplete, twoIncomplete, threeIncomplete, fourIncomplete);
        return all;
    }

}
