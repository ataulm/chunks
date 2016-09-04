package com.ataulm.chunks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.fest.assertions.core.Condition;
import org.junit.Test;

import static com.ataulm.chunks.ChunkFixtures.aChunk;
import static com.ataulm.chunks.ChunksFixtures.aChunks;
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

    @Test
    public void add_entry_to_today() {
        Chunk today = aChunk().with(ENTRIES_TODAY).get();
        Chunk tomorrow = aChunk().with(ENTRIES_TOMORROW).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow);
        Entry entry = anEntry().get();

        Chunks updatedChunks = chunks.add(entry, Day.TODAY);

        assertThat(updatedChunks.today()).contains(entry);
    }

    @Test
    public void add_entry_to_tomorrow() {
        Chunk today = aChunk().with(ENTRIES_TODAY).get();
        Chunk tomorrow = aChunk().with(ENTRIES_TOMORROW).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow);
        Entry entry = anEntry().get();

        Chunks updatedChunks = chunks.add(entry, Day.TOMORROW);

        assertThat(updatedChunks.tomorrow()).contains(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_entry_complains_if_duplicate() {
        Entry entry = anEntry().get();
        Chunk today = Chunk.create(Collections.singletonList(entry));
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, Chunk.empty());

        chunks.add(entry, Day.TODAY);
    }

    @Test
    public void remove_entry_from_today() {
        Entry entry = anEntry().get();
        Chunk today = aChunk().with(entry).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, Chunk.empty());

        // TODO: if this is how to remove, then duplicate entries should not be allowed even across days
        Chunks updatedChunks = chunks.remove(entry.id());


        assertThat(updatedChunks.today()).doesNotContain(entry);
    }

    @Test
    public void remove_entry_from_tomorrow() {
        Entry entry = anEntry().get();
        Chunk tomorrow = aChunk().with(entry).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, Chunk.empty(), tomorrow);

        Chunks updatedChunks = chunks.remove(entry.id());


        assertThat(updatedChunks.today()).doesNotContain(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void remove_entry_complains_if_entry_not_found() {
        Chunks chunks = Chunks.create(AUGUST_02_2016, Chunk.empty(), Chunk.empty());

        Entry entry = anEntry().get();
        chunks.remove(entry.id());
    }

    @Test
    public void shuffle_along_with_different_day_moves_all_completed_tasks() {
        List<Entry> completeTasks = createNewListOfCompleteNewEntries();
        List<Entry> incompleteTasks = createNewListOfIncompleteNewEntries();
        List<Entry> todayTasks = new ArrayList<>();
        todayTasks.addAll(completeTasks);
        todayTasks.addAll(incompleteTasks);
        Chunk today = aChunk().with(todayTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, Chunk.empty());

        Chunks updatedChunks = chunks.shuffleAlong(AUGUST_03_2016);

        assertThat(updatedChunks.today().entries()).isEqualTo(incompleteTasks);
    }

    @Test
    public void shuffle_along_with_different_day_moves_all_incomplete_tasks_to_today() {
        List<Entry> todayTasks = createNewListOfIncompleteNewEntries();
        List<Entry> tomorrowTasks = createNewListOfIncompleteNewEntries();
        Chunk today = aChunk().with(todayTasks).get();
        Chunk tomorrow = aChunk().with(tomorrowTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow);

        Chunks updatedChunks = chunks.shuffleAlong(AUGUST_03_2016);

        assertThat(updatedChunks).is(withNoTasksInTomorrowIsEmptyAndTodayContains(tomorrowTasks));
    }

    private static Condition<Chunks> withNoTasksInTomorrowIsEmptyAndTodayContains(final List<Entry> tomorrowTasks) {
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
        Entry oneIncomplete = anEntry().get();
        Entry twoIncomplete = anEntry().get();
        Entry threeIncomplete = anEntry().get();
        Entry fourIncomplete = anEntry().get();
        return Arrays.asList(oneIncomplete, twoIncomplete, threeIncomplete, fourIncomplete);
    }

}