package com.ataulm.chunks

data class Chunks(
        val todaysDate: ChunkDate,
        val today: Items,
        val tomorrow: Items,
        val sometime: Items,
        val input: String?
) {

    companion object {

        fun empty(todaysDate: ChunkDate): Chunks {
            return create(todaysDate, Items.empty(), Items.empty(), Items.empty())
        }

        @JvmOverloads
        fun create(todaysDate: ChunkDate, today: Items, tomorrow: Items, sometime: Items, input: String? = null): Chunks {
            return Chunks(todaysDate, today, tomorrow, sometime, input)
        }
    }
}

