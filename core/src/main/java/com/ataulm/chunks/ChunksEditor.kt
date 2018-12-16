package com.ataulm.chunks

import java.util.*

class ChunksEditor {

    fun add(items: Items, item: Item): Items {
        assertNoEntryWithSameId(items, item)

        val entries = ArrayList<Item>(items.size() + 1)
        entries.addAll(items.entries)
        entries.add(item)
        return Items.create(entries)
    }

    fun add(items: Items, entries: List<Item>): Items {
        for (item in entries) {
            assertNoEntryWithSameId(items, item)
        }

        val updatedEntries = ArrayList<Item>(items.size() + entries.size)
        updatedEntries.addAll(items.entries)
        updatedEntries.addAll(entries)
        return Items.create(updatedEntries)
    }

    fun remove(items: Items, id: Id): Items {
        val updatedEntries = ArrayList<Item>(items.size())
        for (value in items.entries) {
            if (value.id != id) {
                updatedEntries.add(value)
            }
        }
        return Items.create(updatedEntries)
    }

    fun remove(items: Items, entries: List<Item>): Items {
        val updatedEntries = ArrayList(items.entries)
        updatedEntries.removeAll(entries)
        return Items.create(updatedEntries)
    }

    fun update(items: Items, item: Item): Items {
        val updatedEntries = ArrayList<Item>(items.size())
        for (existingValue in items.entries) {
            if (existingValue.id == item.id) {
                updatedEntries.add(item)
            } else {
                updatedEntries.add(existingValue)
            }
        }
        return Items.create(updatedEntries)
    }

    fun add(chunks: Chunks, day: Day, item: Item): Chunks {
        assertNoEntriesWithSameId(chunks, item)

        when (day) {
            Day.TODAY -> {
                val updatedToday = add(chunks.today, item)
                return Chunks.create(chunks.todaysDate, updatedToday, chunks.tomorrow, chunks.sometime)
            }
            Day.TOMORROW -> {
                val updatedTomorrow = add(chunks.tomorrow, item)
                return Chunks.create(chunks.todaysDate, chunks.today, updatedTomorrow, chunks.sometime)
            }
            Day.SOMETIME -> {
                val updatedSometime = add(chunks.sometime, item)
                return Chunks.create(chunks.todaysDate, chunks.today, chunks.tomorrow, updatedSometime)
            }
            else -> throw IllegalArgumentException("unsupported day: $day")
        }
    }

    fun add(chunks: Chunks, day: Day, entries: List<Item>): Chunks {
        for (item in entries) {
            assertNoEntriesWithSameId(chunks, item)
        }

        when (day) {
            Day.TODAY -> {
                val updatedToday = add(chunks.today, entries)
                return Chunks.create(chunks.todaysDate, updatedToday, chunks.tomorrow, chunks.sometime)
            }
            Day.TOMORROW -> {
                val updatedTomorrow = add(chunks.tomorrow, entries)
                return Chunks.create(chunks.todaysDate, chunks.today, updatedTomorrow, chunks.sometime)
            }
            Day.SOMETIME -> {
                val updatedSometime = add(chunks.sometime, entries)
                return Chunks.create(chunks.todaysDate, chunks.today, chunks.tomorrow, updatedSometime)
            }
            else -> throw IllegalArgumentException("unsupported day: $day")
        }
    }

    fun edit(chunks: Chunks, id: Id): Chunks {
        if (chunks.today.containsEntryWith(id)) {
            val item = chunks.today.findEntryWith(id)
            val updatedToday = remove(chunks.today, id)
            return Chunks.create(chunks.todaysDate, updatedToday, chunks.tomorrow, chunks.sometime, item?.value)
        }

        if (chunks.tomorrow.containsEntryWith(id)) {
            val item = chunks.tomorrow.findEntryWith(id)
            val updatedTomorrow = remove(chunks.tomorrow, id)
            return Chunks.create(chunks.todaysDate, chunks.today, updatedTomorrow, chunks.sometime, item?.value)
        }

        if (chunks.sometime.containsEntryWith(id)) {
            val item = chunks.sometime.findEntryWith(id)
            val updatedSometime = remove(chunks.sometime, id)
            return Chunks.create(chunks.todaysDate, chunks.today, chunks.tomorrow, updatedSometime, item?.value)
        }

        return chunks
    }

    fun remove(chunks: Chunks, id: Id): Chunks {
        if (chunks.today.containsEntryWith(id)) {
            val updatedToday = remove(chunks.today, id)
            return Chunks.create(chunks.todaysDate, updatedToday, chunks.tomorrow, chunks.sometime)
        }

        if (chunks.tomorrow.containsEntryWith(id)) {
            val updatedTomorrow = remove(chunks.tomorrow, id)
            return Chunks.create(chunks.todaysDate, chunks.today, updatedTomorrow, chunks.sometime)
        }

        if (chunks.sometime.containsEntryWith(id)) {
            val updatedSometime = remove(chunks.sometime, id)
            return Chunks.create(chunks.todaysDate, chunks.today, chunks.tomorrow, updatedSometime)
        }

        return chunks
    }

    fun update(chunks: Chunks, item: Item): Chunks {
        if (chunks.today.containsEntryWith(item.id)) {
            val updatedToday = update(chunks.today, item)
            return Chunks.create(chunks.todaysDate, updatedToday, chunks.tomorrow, chunks.sometime)
        }

        if (chunks.tomorrow.containsEntryWith(item.id)) {
            val updatedTomorrow = update(chunks.tomorrow, item)
            return Chunks.create(chunks.todaysDate, chunks.today, updatedTomorrow, chunks.sometime)
        }

        if (chunks.sometime.containsEntryWith(item.id)) {
            val updatedSometime = update(chunks.sometime, item)
            return Chunks.create(chunks.todaysDate, chunks.today, chunks.tomorrow, updatedSometime)
        }

        throw IllegalArgumentException("no entries with id found: " + item.id)
    }

    fun move(chunks: Chunks, item: Item, newEntryPosition: Int): Chunks {
        if (chunks.today.containsEntryWith(item.id)) {
            val updatedToday = move(chunks.today, item, newEntryPosition)
            return Chunks.create(chunks.todaysDate, updatedToday, chunks.tomorrow, chunks.sometime)
        }

        if (chunks.tomorrow.containsEntryWith(item.id)) {
            val updatedTomorrow = move(chunks.tomorrow, item, newEntryPosition)
            return Chunks.create(chunks.todaysDate, chunks.today, updatedTomorrow, chunks.sometime)
        }

        if (chunks.sometime.containsEntryWith(item.id)) {
            val updatedSometime = move(chunks.sometime, item, newEntryPosition)
            return Chunks.create(chunks.todaysDate, chunks.today, chunks.tomorrow, updatedSometime)
        }

        throw IllegalArgumentException("no entries with id found: " + item.id)
    }

    fun move(items: Items, item: Item, newEntryPosition: Int): Items {
        if (newEntryPosition < 0 || newEntryPosition >= items.size()) {
            throw IllegalArgumentException("newEntryPosition is out of bounds: $newEntryPosition")
        }

        val originalEntryPosition = items.entries.indexOf(item)
        if (originalEntryPosition == -1 || originalEntryPosition == newEntryPosition) {
            return items
        }

        val updatedEntries = ArrayList(items.entries)
        if (originalEntryPosition > newEntryPosition) {
            updatedEntries.add(newEntryPosition, item)
            updatedEntries.removeAt(originalEntryPosition + 1)
        } else {
            updatedEntries.add(newEntryPosition + 1, item)
            updatedEntries.removeAt(originalEntryPosition)
        }

        return Items.create(updatedEntries)
    }

    fun shuffleAlong(chunks: Chunks, todaysDate: ChunkDate): Chunks {
        if (chunks.todaysDate.isSameDayAs(todaysDate)) {
            return chunks
        }

        val updatedTodayEntries = ArrayList<Item>()
        for (item in chunks.today) {
            if (!item.isCompleted) {
                updatedTodayEntries.add(item)
            }
        }

        for (item in chunks.tomorrow) {
            if (!item.isCompleted) {
                updatedTodayEntries.add(item)
            }
        }

        val updatedSometimeEntries = ArrayList<Item>()
        for (item in chunks.sometime) {
            if (!item.isCompleted) {
                updatedSometimeEntries.add(item)
            }
        }

        val updatedToday = Items.create(updatedTodayEntries)
        val updatedTomorrow = Items.empty()
        val updatedSometime = Items.create(updatedSometimeEntries)

        return Chunks.create(todaysDate, updatedToday, updatedTomorrow, updatedSometime)
    }

    private fun assertNoEntriesWithSameId(chunks: Chunks, item: Item) {
        assertNoEntryWithSameId(chunks.today, item)
        assertNoEntryWithSameId(chunks.tomorrow, item)
        assertNoEntryWithSameId(chunks.sometime, item)
    }

    private fun assertNoEntryWithSameId(items: Items, item: Item) {
        if (items.containsEntryWith(item.id)) {
            throw IllegalArgumentException("items already contains item with id: " + item.id)
        }
    }

}
