package com.ataulm.chunks

data class Item(
        val id: Id,
        val value: String,
        val completedTimestamp: String?
) {

    val isCompleted: Boolean
        get() = completedTimestamp != null

    fun markCompleted(): Item {
        val completedTimestamp = System.currentTimeMillis().toString()
        return createFrom(id, value, completedTimestamp)
    }

    fun markNotComplete(): Item {
        return createFrom(id, value)
    }

    companion object {

        fun createNew(value: String): Item {
            return createFrom(Id.create(), value)
        }

        @JvmOverloads
        fun createFrom(id: Id, value: String, completedTimestamp: String? = null): Item {
            return Item(id, value, completedTimestamp)
        }
    }
}

