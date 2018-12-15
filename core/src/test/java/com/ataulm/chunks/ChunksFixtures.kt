package com.ataulm.chunks

import com.ataulm.chunks.ItemsFixtures.items

class ChunksFixtures private constructor(
        private var todaysDate: ChunkDate,
        private var today: Items,
        private var tomorrow: Items,
        private var sometime: Items
) {

    fun withTodaysDate(todaysDate: ChunkDate): ChunksFixtures {
        this.todaysDate = todaysDate
        return this
    }

    fun withToday(today: Items): ChunksFixtures {
        this.today = today
        return this
    }

    fun withTomorrow(tomorrow: Items): ChunksFixtures {
        this.tomorrow = tomorrow
        return this
    }

    fun withSometime(sometime: Items): ChunksFixtures {
        this.sometime = sometime
        return this
    }

    fun get(): Chunks {
        return Chunks.create(todaysDate, today, tomorrow, sometime)
    }

    companion object {

        fun aChunks(): ChunksFixtures {
            return ChunksFixtures(
                    ChunkDate.create(0),
                    items().get(),
                    items().get(),
                    items().get()
            )
        }
    }

}
