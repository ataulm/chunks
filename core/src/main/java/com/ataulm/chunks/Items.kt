package com.ataulm.chunks

import java.util.*

data class Items(val entries: List<Item>)
    : Iterable<Item> {

    val isEmpty: Boolean
        get() = entries.isEmpty()

    operator fun get(position: Int): Item {
        return entries[position]
    }

    fun containsEntryWith(id: Id): Boolean {
        return findEntryWith(id) != null
    }

    fun findEntryWith(id: Id): Item? {
        for (item in entries) {
            if (item.id == id) {
                return item
            }
        }
        return null
    }

    fun size(): Int {
        return entries.size
    }

    override fun iterator(): Iterator<Item> {
        return entries.iterator()
    }

    companion object {

        fun create(entries: List<Item>): Items {
            return Items(Collections.unmodifiableList(entries))
        }

        fun empty(): Items {
            return create(emptyList())
        }
    }

}

