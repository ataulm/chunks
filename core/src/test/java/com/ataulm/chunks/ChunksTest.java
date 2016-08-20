package com.ataulm.chunks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fest.assertions.core.Condition;
import org.junit.Test;

import static com.ataulm.chunks.ChunkFixtures.aChunk;
import static com.ataulm.chunks.ChunksFixtures.aChunks;
import static com.ataulm.chunks.EntryFixtures.anEntry;
import static org.fest.assertions.api.Assertions.assertThat;

public class ChunksTest {

    @Test
    public void addEntryToToday() {
        Chunks chunks = Chunks.empty(ChunkDate.create(new SystemClock()));
        Entry entry = anEntry().get();

        Chunks updatedChunks = chunks.add(entry, Day.TODAY);

        assertThat(updatedChunks.today()).contains(entry);
    }

    @Test
    public void addEntryToTomorrow() {
        Chunks chunks = Chunks.empty(ChunkDate.create(new SystemClock()));
        Entry entry = anEntry().get();

        Chunks updatedChunks = chunks.add(entry, Day.TOMORROW);

        assertThat(updatedChunks.tomorrow()).contains(entry);
    }

    @Test
    public void removeEntryFromToday() {
        Entry entry = anEntry().get();
        Chunk today = aChunk().with(entry).get();
        Chunks chunks = aChunks()
                .withToday(today)
                .get();

        Chunks updatedChunks = chunks.remove(entry.id());

        assertThat(updatedChunks.today()).doesNotContain(entry);
    }

    @Test
    public void removeEntryFromTomorrow() {
        Entry entry = anEntry().get();
        Chunk tomorrow = aChunk().with(entry).get();
        Chunks chunks = aChunks()
                .withTomorrow(tomorrow)
                .get();

        Chunks updatedChunks = chunks.remove(entry.id());

        assertThat(updatedChunks.tomorrow()).doesNotContain(entry);
    }

    @Test
    public void shuffleAlongRemovesCompletedTasks() {
        List<Entry> completeTasks = createNewListOfCompleteNewEntries();
        List<Entry> incompleteTasks = createNewListOfIncompleteNewEntries();

        List<Entry> todayTasks = new ArrayList<>();
        todayTasks.addAll(completeTasks);
        todayTasks.addAll(incompleteTasks);

        Chunk today = aChunk().with(todayTasks).get();
        Chunks chunks = aChunks().withToday(today).get();

        Chunks updatedChunks = chunks.shuffleAlong();

        assertThat(updatedChunks.today().entries()).isEqualTo(incompleteTasks);
    }

    @Test
    public void shuffleAlongMovesAllOfTomorrowsTasksToToday() {
        List<Entry> todayTasks = createNewListOfIncompleteNewEntries();
        List<Entry> tomorrowTasks = createNewListOfIncompleteNewEntries();

        Chunk today = aChunk().with(todayTasks).get();
        Chunk tomorrow = aChunk().with(tomorrowTasks).get();

        ChunkDate todaysDate = ChunkDate.create(0);
        Chunks chunks = aChunks().withTodaysDate(todaysDate).withToday(today).withTomorrow(tomorrow).get();

        Chunks updatedChunks = chunks.shuffleAlong(todaysDate);

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
