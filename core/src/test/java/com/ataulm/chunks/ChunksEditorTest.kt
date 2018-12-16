package com.ataulm.chunks

import com.ataulm.chunks.ChunksFixtures.Companion.aChunks
import com.ataulm.chunks.ItemFixtures.anItem
import com.ataulm.chunks.ItemsFixtures.items
import org.fest.assertions.api.Assertions.assertThat
import org.fest.assertions.core.Condition
import org.junit.Test
import java.util.*

class ChunksEditorTest {

    private val chunksEditor = ChunksEditor()

    @Test
    fun addingAnEntry() {
        val items = items().get()
        val item = anItem().get()

        val updatedItems = chunksEditor.add(items, item)

        assertThat(updatedItems.entries).containsExactly(item)
    }

    @Test
    fun addingMultipleEntries() {
        val items = items().get()
        val one = anItem().get()
        val two = anItem().get()

        val updatedItems = chunksEditor.add(items, Arrays.asList(one, two))

        assertThat(updatedItems.entries).containsExactly(one, two)
    }

    @Test(expected = IllegalArgumentException::class)
    fun addingEntriesWithSameIdThrowsError() {
        val item = anItem().get()
        val itemWithSameId = anItem().withId(item.id).get()
        val items = items().with(item).get()

        try {
            chunksEditor.add(items, itemWithSameId)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageContaining(item.id.toString())
            throw e
        }

    }

    @Test
    fun removingAnEntry() {
        val item = anItem().get()
        val items = items().with(item).get()

        val updatedItems = chunksEditor.remove(items, item.id)

        assertThat(updatedItems.containsEntryWith(item.id)).isFalse
    }

    @Test
    fun removingNonExistentEntryDoesNothing() {
        val item = anItem().get()
        val items = items().get()

        val updatedItems = chunksEditor.remove(items, item.id)

        assertThat(updatedItems.containsEntryWith(item.id)).isFalse
    }

    @Test
    fun removingMultipleEntries() {
        val one = anItem().get()
        val two = anItem().get()
        val three = anItem().get()
        val items = items().with(one, two, three).get()

        val updatedItems = chunksEditor.remove(items, Arrays.asList(two, three))

        assertThat(updatedItems.entries).containsExactly(one)
    }

    @Test
    fun removingMultipleEntriesWithNonExistingEntriesDoesNothing() {
        val one = anItem().get()
        val two = anItem().get()
        val three = anItem().get()
        val four = anItem().get()
        val five = anItem().get()
        val items = items().with(one, two, three).get()

        val updatedItems = chunksEditor.remove(items, Arrays.asList(four, five))

        assertThat(updatedItems.entries).containsExactly(one, two, three)
    }

    @Test
    fun removingMultipleEntriesWithSomeNonExistingEntriesRemovesTheExistingOnes() {
        val one = anItem().get()
        val two = anItem().get()
        val three = anItem().get()
        val four = anItem().get()
        val items = items().with(one, two, three).get()

        val updatedItems = chunksEditor.remove(items, Arrays.asList(two, three, four))

        assertThat(updatedItems.entries).containsExactly(one)
    }

    @Test
    fun updatingAnEntry() {
        val initial = anItem().withValue("initial").get()
        val items = items().with(initial).get()
        val updated = anItem().withId(initial.id).withValue("updated").get()

        val updatedItems = chunksEditor.update(items, updated)

        assertThat(updatedItems.findEntryWith(initial.id)!!.value).isEqualTo("updated")
    }

    @Test
    fun updatingNonExistentEntryDoesNothing() {
        val items = items().get()
        val updated = anItem().get()

        chunksEditor.update(items, updated)

        assertThat(items.isEmpty)
    }

    @Test
    fun addingAnEntryToDay() {
        val item = anItem().get()
        val chunks = aChunks().get()

        val (_, _, tomorrow) = chunksEditor.add(chunks, Day.TOMORROW, item)

        assertThat(tomorrow.entries).containsExactly(item)
    }

    @Test
    fun addingMultipleEntriesToDay() {
        val one = anItem().get()
        val two = anItem().get()
        val chunks = aChunks().get()

        val (_, _, tomorrow) = chunksEditor.add(chunks, Day.TOMORROW, Arrays.asList(one, two))

        assertThat(tomorrow.entries).containsExactly(one, two)
    }

    @Test(expected = IllegalArgumentException::class)
    fun addingEntriesWithSameIdToDayThrowsError() {
        val item = anItem().get()
        val itemWithSameId = anItem().withId(item.id).get()
        val today = items().with(item).get()
        val chunks = aChunks().withToday(today).get()

        try {
            chunksEditor.add(chunks, Day.TODAY, itemWithSameId)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageContaining(item.id.toString())
            throw e
        }

    }

    @Test(expected = IllegalArgumentException::class)
    fun addingEntriesWithSameIdAcrossDaysThrowsError() {
        val item = anItem().get()
        val itemWithSameId = anItem().withId(item.id).get()
        val today = items().with(item).get()
        val chunks = aChunks().withToday(today).get()

        try {
            chunksEditor.add(chunks, Day.TOMORROW, itemWithSameId)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageContaining(item.id.toString())
            throw e
        }

    }

    @Test
    fun editingEntryRemovesItFromDayAndPassesTheValue() {
        val item = anItem().get()
        val today = items().with(item).get()
        val chunks = aChunks().withToday(today).get()

        val (_, today1, _, _, input) = chunksEditor.edit(chunks, item.id)

        assertThat(today1.entries).isEmpty()
        assertThat(input!!).isEqualTo(item.value)
    }

    @Test
    fun editingNonExistentEntryDoesNothing() {
        val item = anItem().get()
        val chunks = aChunks().get()

        val updatedChunks = chunksEditor.edit(chunks, item.id)

        assertThat(updatedChunks).isEqualTo(chunks)
        assertThat(updatedChunks.input != null).isFalse()
    }

    @Test
    fun removingAnEntryFromChunks() {
        val item = anItem().get()
        val today = items().with(item).get()
        val chunks = aChunks().withToday(today).get()

        val (_, today1) = chunksEditor.remove(chunks, item.id)

        assertThat(today1).isEmpty()
    }

    @Test
    fun removingNonExistentEntryFromChunksDoesNothing() {
        val item = anItem().get()
        val chunks = aChunks().get()

        val updatedChunks = chunksEditor.remove(chunks, item.id)

        assertThat(updatedChunks).isEqualTo(chunks)
    }

    @Test
    fun updatingAnEntryToDay() {
        val initial = anItem().withValue("initial").get()
        val today = items().with(initial).get()
        val chunks = aChunks().withToday(today).get()
        val updated = anItem().withId(initial.id).withValue("updated").get()

        val (_, today1) = chunksEditor.update(chunks, updated)

        assertThat(today1).containsExactly(updated)
    }

    @Test(expected = IllegalArgumentException::class)
    fun updatingNonExistentEntryThrowsError() {
        val chunks = aChunks().get()
        val item = anItem().get()

        try {
            chunksEditor.update(chunks, item)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageContaining(item.id.toString())
            throw e
        }

    }

    @Test
    fun movingAnEntry() {
        val zero = anItem().get()
        val one = anItem().get()
        val two = anItem().get()
        val tomorrow = items().with(zero, one, two).get()
        val chunks = aChunks().withTomorrow(tomorrow).get()

        val (_, _, tomorrow1) = chunksEditor.move(chunks, two, 0)

        assertThat(tomorrow1).containsExactly(two, zero, one)
    }

    @Test(expected = IllegalArgumentException::class)
    fun movingNonExistentEntryThrowsError() {
        val chunks = aChunks().get()
        val item = anItem().get()

        try {
            chunksEditor.move(chunks, item, 0)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageContaining(item.id.toString())
            throw e
        }

    }

    @Test
    fun moveEntryToEarlierPosition() {
        val zero = anItem().get()
        val one = anItem().get()
        val two = anItem().get()
        val three = anItem().get()
        val items = items().with(zero, one, two, three).get()

        val updatedItems = chunksEditor.move(items, three, 0)

        assertThat(updatedItems.entries).containsExactly(three, zero, one, two)
    }

    @Test
    fun moveEntryToLaterPosition() {
        val zero = anItem().get()
        val one = anItem().get()
        val two = anItem().get()
        val three = anItem().get()
        val items = items().with(zero, one, two, three).get()

        val updatedItems = chunksEditor.move(items, zero, 3)

        assertThat(updatedItems.entries).containsExactly(one, two, three, zero)
    }

    @Test
    fun moveEntryWithNonExistentEntryDoesNothing() {
        val item = anItem().get()
        val items = items().with(item).get()
        val nonExistentItem = anItem().get()

        val updatedEntries = chunksEditor.move(items, nonExistentItem, 0)

        assertThat(updatedEntries).isEqualTo(items)
    }

    @Test(expected = IllegalArgumentException::class)
    fun moveEntryWithNewEntryPositionLowerThanZeroThrowsError() {
        val item = anItem().get()
        val items = items().with(item).get()
        val newEntryPosition = -1

        try {
            chunksEditor.move(items, item, newEntryPosition)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageContaining(newEntryPosition.toString())
            throw e
        }

    }

    @Test(expected = IllegalArgumentException::class)
    fun moveEntryWithNewEntryPositionGreaterThanMaxIndexThrowsError() {
        val item = anItem().get()
        val items = items().with(item).get()
        val newEntryPosition = items.size()

        try {
            chunksEditor.move(items, item, newEntryPosition)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageContaining(newEntryPosition.toString())
            throw e
        }

    }

    @Test
    fun moveEntryWithSameOriginalAndNewEntryPositionsDoesNothing() {
        val zero = anItem().get()
        val one = anItem().get()
        val items = items().with(zero, one).get()

        val updatedChunks = chunksEditor.move(items, zero, 0)

        assertThat(updatedChunks).isEqualTo(items)
    }

    @Test
    fun shuffle_along_with_different_day_moves_all_completed_tasks() {
        val completeTasks = createNewListOfCompleteNewEntries()
        val incompleteTasks = createNewListOfIncompleteNewEntries()
        val todayTasks = ArrayList<Item>()
        todayTasks.addAll(completeTasks)
        todayTasks.addAll(incompleteTasks)
        val today = items().with(todayTasks).get()
        val chunks = Chunks.create(AUGUST_02_2016, today, Items.empty(), Items.empty())

        val (_, today1) = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016)

        assertThat(today1.entries).isEqualTo(incompleteTasks)
    }

    init {
        val todayTasks = createNewListOfIncompleteNewEntries()
        val tomorrowTasks = createNewListOfIncompleteNewEntries()
        val today = items().with(todayTasks).get()
        val tomorrow = items().with(tomorrowTasks).get()
        val chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, Items.empty())

        val updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016)

        assertThat(updatedChunks).`is`(withNoTasksInTomorrowAndTodayContains(tomorrowTasks))
    }

    @Test
    fun shuffle_along_with_different_day_removes_all_completed_tasks_from_tomorrow() {
        val todayTasks = createNewListOfIncompleteNewEntries()
        val completed = anItem().withCompletedTimestamp("0").get()
        val tomorrowTasks = createNewListOfIncompleteNewEntriesAnd(completed)
        val today = items().with(todayTasks).get()
        val tomorrow = items().with(tomorrowTasks).get()
        val chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, Items.empty())

        val updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016)

        assertThat(updatedChunks).`is`(without(completed))
    }

    @Test
    fun shuffle_along_with_different_day_removes_all_completed_tasks_from_sometime() {
        val todayTasks = createNewListOfIncompleteNewEntries()
        val tomorrowTasks = createNewListOfIncompleteNewEntries()
        val completed = anItem().withCompletedTimestamp("0").get()
        val sometimeTasks = createNewListOfIncompleteNewEntriesAnd(completed)
        val today = items().with(todayTasks).get()
        val tomorrow = items().with(tomorrowTasks).get()
        val sometime = items().with(sometimeTasks).get()
        val chunks = Chunks.create(AUGUST_02_2016, today, tomorrow, sometime)

        val updatedChunks = chunksEditor.shuffleAlong(chunks, AUGUST_03_2016)

        assertThat(updatedChunks).`is`(without(completed))
    }

    companion object {

        private val AUGUST_02_2016 = ChunkDate.create(1470096000000L)
        private val AUGUST_03_2016 = ChunkDate.create(1470182400000L)

        private fun without(item: Item): Condition<Chunks> {
            return object : Condition<Chunks>() {
                override fun matches(chunks: Chunks): Boolean {
                    val chunksContainsEntry = (chunks.today.containsEntryWith(item.id)
                            || chunks.tomorrow.containsEntryWith(item.id)
                            || chunks.sometime.containsEntryWith(item.id))
                    return !chunksContainsEntry
                }
            }
        }

        private fun withNoTasksInTomorrowAndTodayContains(tomorrowTasks: List<Item>): Condition<Chunks> {
            return object : Condition<Chunks>() {
                override fun matches(value: Chunks): Boolean {
                    return value.today.entries.containsAll(tomorrowTasks) && value.tomorrow.isEmpty
                }
            }
        }

        private fun createNewListOfCompleteNewEntries(): List<Item> {
            val timestamp_1970 = "0"
            val oneComplete = anItem().withCompletedTimestamp(timestamp_1970).get()
            val twoComplete = anItem().withCompletedTimestamp(timestamp_1970).get()
            val threeComplete = anItem().withCompletedTimestamp(timestamp_1970).get()
            val fourComplete = anItem().withCompletedTimestamp(timestamp_1970).get()
            return Arrays.asList(oneComplete, twoComplete, threeComplete, fourComplete)
        }

        private fun createNewListOfIncompleteNewEntries(): List<Item> {
            return createNewListOfIncompleteNewEntriesAnd()
        }

        private fun createNewListOfIncompleteNewEntriesAnd(vararg entries: Item): List<Item> {
            val all = ArrayList(Arrays.asList(*entries))

            val oneIncomplete = anItem().get()
            val twoIncomplete = anItem().get()
            val threeIncomplete = anItem().get()
            val fourIncomplete = anItem().get()

            Collections.addAll(all, oneIncomplete, twoIncomplete, threeIncomplete, fourIncomplete)
            return all
        }
    }

}
