package com.ataulm.chunks;

import org.fest.assertions.core.Condition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ataulm.chunks.ItemsFixtures.items;
import static com.ataulm.chunks.ChunksFixtures.aChunks;
import static com.ataulm.chunks.ItemFixtures.anItem;
import static org.fest.assertions.api.Assertions.assertThat;

public class ChunksEditorTest {

    private static final ChunkDate AUGUST_02_2016 = ChunkDate.create(1470096000000L);
    private static final ChunkDate AUGUST_03_2016 = ChunkDate.create(1470182400000L);

    ChunksEditor chunksEditor = new ChunksEditor();

    @Test
    public void addingAnEntry() {
        Items items = items().get();
        Item item = anItem().get();

        Items updatedItems = chunksEditor.add(items, item);

        assertThat(updatedItems.entries()).containsExactly(item);
    }

    @Test
    public void addingMultipleEntries() {
        Items items = items().get();
        Item one = anItem().get();
        Item two = anItem().get();

        Items updatedItems = chunksEditor.add(items, Arrays.asList(one, two));

        assertThat(updatedItems.entries()).containsExactly(one, two);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEntriesWithSameIdThrowsError() {
        Item item = anItem().get();
        Item itemWithSameId = anItem().withId(item.id()).get();
        Items items = items().with(item).get();

        try {
            chunksEditor.add(items, itemWithSameId);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(item.id()));
            throw e;
        }
    }

    @Test
    public void removingAnEntry() {
        Item item = anItem().get();
        Items items = items().with(item).get();

        Items updatedItems = chunksEditor.remove(items, item.id());

        assertThat(updatedItems.containsEntryWith(item.id())).isFalse();
    }

    @Test
    public void removingNonExistentEntryDoesNothing() {
        Item item = anItem().get();
        Items items = items().get();

        Items updatedItems = chunksEditor.remove(items, item.id());

        assertThat(updatedItems.containsEntryWith(item.id())).isFalse();
    }

    @Test
    public void removingMultipleEntries() {
        Item one = anItem().get();
        Item two = anItem().get();
        Item three = anItem().get();
        Items items = items().with(one, two, three).get();

        Items updatedItems = chunksEditor.remove(items, Arrays.asList(two, three));

        assertThat(updatedItems.entries()).containsExactly(one);
    }

    @Test
    public void removingMultipleEntriesWithNonExistingEntriesDoesNothing() {
        Item one = anItem().get();
        Item two = anItem().get();
        Item three = anItem().get();
        Item four = anItem().get();
        Item five = anItem().get();
        Items items = items().with(one, two, three).get();

        Items updatedItems = chunksEditor.remove(items, Arrays.asList(four, five));

        assertThat(updatedItems.entries()).containsExactly(one, two, three);
    }

    @Test
    public void removingMultipleEntriesWithSomeNonExistingEntriesRemovesTheExistingOnes() {
        Item one = anItem().get();
        Item two = anItem().get();
        Item three = anItem().get();
        Item four = anItem().get();
        Items items = items().with(one, two, three).get();

        Items updatedItems = chunksEditor.remove(items, Arrays.asList(two, three, four));

        assertThat(updatedItems.entries()).containsExactly(one);
    }

    @Test
    public void updatingAnEntry() {
        Item initial = anItem().withValue("initial").get();
        Items items = items().with(initial).get();
        Item updated = anItem().withId(initial.id()).withValue("updated").get();

        Items updatedItems = chunksEditor.update(items, updated);

        assertThat(updatedItems.findEntryWith(initial.id()).value()).isEqualTo("updated");
    }

    @Test
    public void updatingNonExistentEntryDoesNothing() {
        Items items = items().get();
        Item updated = anItem().get();

        chunksEditor.update(items, updated);

        assertThat(items.isEmpty());
    }

    @Test
    public void addingAnEntryToDay() {
        Item item = anItem().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunksEditor.add(chunks, Day.TOMORROW, item);

        assertThat(updatedChunks.tomorrow().entries()).containsExactly(item);
    }

    @Test
    public void addingMultipleEntriesToDay() {
        Item one = anItem().get();
        Item two = anItem().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunksEditor.add(chunks, Day.TOMORROW, Arrays.asList(one, two));

        assertThat(updatedChunks.tomorrow().entries()).containsExactly(one, two);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEntriesWithSameIdToDayThrowsError() {
        Item item = anItem().get();
        Item itemWithSameId = anItem().withId(item.id()).get();
        Items today = items().with(item).get();
        Chunks chunks = aChunks().withToday(today).get();

        try {
            chunksEditor.add(chunks, Day.TODAY, itemWithSameId);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(item.id()));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingEntriesWithSameIdAcrossDaysThrowsError() {
        Item item = anItem().get();
        Item itemWithSameId = anItem().withId(item.id()).get();
        Items today = items().with(item).get();
        Chunks chunks = aChunks().withToday(today).get();

        try {
            chunksEditor.add(chunks, Day.TOMORROW, itemWithSameId);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(item.id()));
            throw e;
        }
    }

    @Test
    public void editingEntryRemovesItFromDayAndPassesTheValue() {
        Item item = anItem().get();
        Items today = items().with(item).get();
        Chunks chunks = aChunks().withToday(today).get();

        Chunks updatedChunks = chunksEditor.edit(chunks, item.id());

        assertThat(updatedChunks.today().entries()).isEmpty();
        assertThat(updatedChunks.input().get()).isEqualTo(item.value());
    }

    @Test
    public void editingNonExistentEntryDoesNothing() {
        Item item = anItem().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunksEditor.edit(chunks, item.id());

        assertThat(updatedChunks).isEqualTo(chunks);
        assertThat(updatedChunks.input().isPresent()).isFalse();
    }

    @Test
    public void removingAnEntryFromChunks() {
        Item item = anItem().get();
        Items today = items().with(item).get();
        Chunks chunks = aChunks().withToday(today).get();

        Chunks updatedChunks = chunksEditor.remove(chunks, item.id());

        assertThat(updatedChunks.today()).isEmpty();
    }

    @Test
    public void removingNonExistentEntryFromChunksDoesNothing() {
        Item item = anItem().get();
        Chunks chunks = aChunks().get();

        Chunks updatedChunks = chunksEditor.remove(chunks, item.id());

        assertThat(updatedChunks).isEqualTo(chunks);
    }

    @Test
    public void updatingAnEntryToDay() {
        Item initial = anItem().withValue("initial").get();
        Items today = items().with(initial).get();
        Chunks chunks = aChunks().withToday(today).get();
        Item updated = anItem().withId(initial.id()).withValue("updated").get();

        Chunks updatedChunks = chunksEditor.update(chunks, updated);

        assertThat(updatedChunks.today()).containsExactly(updated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatingNonExistentEntryThrowsError() {
        Chunks chunks = aChunks().get();
        Item item = anItem().get();

        try {
            chunksEditor.update(chunks, item);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(item.id()));
            throw e;
        }
    }

    @Test
    public void movingAnEntry() {
        Item zero = anItem().get();
        Item one = anItem().get();
        Item two = anItem().get();
        Items tomorrow = items().with(zero, one, two).get();
        Chunks chunks = aChunks().withTomorrow(tomorrow).get();

        Chunks updatedChunks = chunksEditor.move(chunks, two, 0);

        assertThat(updatedChunks.tomorrow()).containsExactly(two, zero, one);
    }

    @Test(expected = IllegalArgumentException.class)
    public void movingNonExistentEntryThrowsError() {
        Chunks chunks = aChunks().get();
        Item item = anItem().get();

        try {
            chunksEditor.move(chunks, item, 0);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(item.id()));
            throw e;
        }
    }

    @Test
    public void moveEntryToEarlierPosition() {
        Item zero = anItem().get();
        Item one = anItem().get();
        Item two = anItem().get();
        Item three = anItem().get();
        Items items = items().with(zero, one, two, three).get();

        Items updatedItems = chunksEditor.move(items, three, 0);

        assertThat(updatedItems.entries()).containsExactly(three, zero, one, two);
    }

    @Test
    public void moveEntryToLaterPosition() {
        Item zero = anItem().get();
        Item one = anItem().get();
        Item two = anItem().get();
        Item three = anItem().get();
        Items items = items().with(zero, one, two, three).get();

        Items updatedItems = chunksEditor.move(items, zero, 3);

        assertThat(updatedItems.entries()).containsExactly(one, two, three, zero);
    }

    @Test
    public void moveEntryWithNonExistentEntryDoesNothing() {
        Item item = anItem().get();
        Items items = items().with(item).get();
        Item nonExistentItem = anItem().get();

        Items updatedEntries = chunksEditor.move(items, nonExistentItem, 0);

        assertThat(updatedEntries).isEqualTo(items);
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveEntryWithNewEntryPositionLowerThanZeroThrowsError() {
        Item item = anItem().get();
        Items items = items().with(item).get();
        int newEntryPosition = -1;

        try {
            chunksEditor.move(items, item, newEntryPosition);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(newEntryPosition));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void moveEntryWithNewEntryPositionGreaterThanMaxIndexThrowsError() {
        Item item = anItem().get();
        Items items = items().with(item).get();
        int newEntryPosition = items.size();

        try {
            chunksEditor.move(items, item, newEntryPosition);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining(String.valueOf(newEntryPosition));
            throw e;
        }
    }

    @Test
    public void moveEntryWithSameOriginalAndNewEntryPositionsDoesNothing() {
        Item zero = anItem().get();
        Item one = anItem().get();
        Items items = items().with(zero, one).get();

        Items updatedChunks = chunksEditor.move(items, zero, 0);

        assertThat(updatedChunks).isEqualTo(items);
    }

    @Test
    public void shuffle_along_with_different_day_moves_all_completed_tasks() {
        List<Item> completeTasks = createNewListOfCompleteNewEntries();
        List<Item> incompleteTasks = createNewListOfIncompleteNewEntries();
        List<Item> todayTasks = new ArrayList<>();
        todayTasks.addAll(completeTasks);
        todayTasks.addAll(incompleteTasks);
        Items today = items().with(todayTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, Items.empty(), Items.empty());

        Chunks updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016);

        assertThat(updatedChunks.today().entries()).isEqualTo(incompleteTasks);
    }

    @Test
    public void shuffle_along_with_different_day_moves_all_incomplete_tasks_to_today() {
        List<Item> todayTasks = createNewListOfIncompleteNewEntries();
        List<Item> tomorrowTasks = createNewListOfIncompleteNewEntries();
        Items today = items().with(todayTasks).get();
        Items tomorrow = items().with(tomorrowTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, Items.empty());

        Chunks updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016);

        assertThat(updatedChunks).is(withNoTasksInTomorrowAndTodayContains(tomorrowTasks));
    }

    @Test
    public void shuffle_along_with_different_day_removes_all_completed_tasks_from_tomorrow() {
        List<Item> todayTasks = createNewListOfIncompleteNewEntries();
        Item completed = anItem().withCompletedTimestamp("0").get();
        List<Item> tomorrowTasks = createNewListOfIncompleteNewEntriesAnd(completed);
        Items today = items().with(todayTasks).get();
        Items tomorrow = items().with(tomorrowTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, Items.empty());

        Chunks updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016);

        assertThat(updatedChunks).is(without(completed));
    }

    @Test
    public void shuffle_along_with_different_day_removes_all_completed_tasks_from_sometime() {
        List<Item> todayTasks = createNewListOfIncompleteNewEntries();
        List<Item> tomorrowTasks = createNewListOfIncompleteNewEntries();
        Item completed = anItem().withCompletedTimestamp("0").get();
        List<Item> sometimeTasks = createNewListOfIncompleteNewEntriesAnd(completed);
        Items today = items().with(todayTasks).get();
        Items tomorrow = items().with(tomorrowTasks).get();
        Items sometime = items().with(sometimeTasks).get();
        Chunks chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, sometime);

        Chunks updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016);

        assertThat(updatedChunks).is(without(completed));
    }

    private static Condition<Chunks> without(final Item item) {
        return new Condition<Chunks>() {
            @Override
            public boolean matches(Chunks chunks) {
                boolean chunksContainsEntry = chunks.today().containsEntryWith(item.id())
                        || chunks.tomorrow().containsEntryWith(item.id())
                        || chunks.sometime().containsEntryWith(item.id());
                return !chunksContainsEntry;
            }
        };
    }

    private static Condition<Chunks> withNoTasksInTomorrowAndTodayContains(final List<Item> tomorrowTasks) {
        return new Condition<Chunks>() {
            @Override
            public boolean matches(Chunks value) {
                return value.today().entries().containsAll(tomorrowTasks)
                        && value.tomorrow().isEmpty();
            }
        };
    }

    private static List<Item> createNewListOfCompleteNewEntries() {
        String timestamp_1970 = "0";
        Item oneComplete = anItem().withCompletedTimestamp(timestamp_1970).get();
        Item twoComplete = anItem().withCompletedTimestamp(timestamp_1970).get();
        Item threeComplete = anItem().withCompletedTimestamp(timestamp_1970).get();
        Item fourComplete = anItem().withCompletedTimestamp(timestamp_1970).get();
        return Arrays.asList(oneComplete, twoComplete, threeComplete, fourComplete);
    }

    private static List<Item> createNewListOfIncompleteNewEntries() {
        return createNewListOfIncompleteNewEntriesAnd();
    }

    private static List<Item> createNewListOfIncompleteNewEntriesAnd(Item... entries) {
        List<Item> all = new ArrayList<>(Arrays.asList(entries));

        Item oneIncomplete = anItem().get();
        Item twoIncomplete = anItem().get();
        Item threeIncomplete = anItem().get();
        Item fourIncomplete = anItem().get();

        Collections.addAll(all, oneIncomplete, twoIncomplete, threeIncomplete, fourIncomplete);
        return all;
    }

}
