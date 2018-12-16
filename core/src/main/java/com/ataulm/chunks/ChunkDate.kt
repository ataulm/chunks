package com.ataulm.chunks

import java.util.*

data class ChunkDate(val timestamp: Long) {

    private val year: Int
        get() {
            val dateCalendar = Calendar.getInstance()
            dateCalendar.timeInMillis = timestamp
            return dateCalendar.get(Calendar.YEAR)
        }

    private val dayOfYear: Int
        get() {
            val dateCalendar = Calendar.getInstance()
            dateCalendar.timeInMillis = timestamp
            return dateCalendar.get(Calendar.DAY_OF_YEAR)
        }

    fun isSameDayAs(date: ChunkDate): Boolean {
        return date.dayOfYear == dayOfYear && date.year == year
    }

    companion object {

        fun create(clock: Clock): ChunkDate {
            return create(clock.currentTime)
        }

        fun create(timestamp: Long): ChunkDate {
            return ChunkDate(timestamp)
        }
    }

}
