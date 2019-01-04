package com.ataulm.chunks.repository

import com.ataulm.chunks.ChunkDate
import com.ataulm.chunks.Chunks

class GsonChunksConverter(private val chunkConverter: GsonChunkConverter) {

    fun convert(chunks: Chunks): GsonChunks {
        val gsonChunks = GsonChunks()
        gsonChunks.todays_date = chunks.todaysDate.timestamp.toString()
        gsonChunks.today = chunkConverter.convert(chunks.today)
        gsonChunks.tomorrow = chunkConverter.convert(chunks.tomorrow)
        gsonChunks.sometime = chunkConverter.convert(chunks.sometime)
        return gsonChunks
    }

    fun convert(gsonChunks: GsonChunks): Chunks {
        return Chunks.create(
                ChunkDate.create(java.lang.Long.parseLong(gsonChunks.todays_date)),
                chunkConverter.convert(gsonChunks.today),
                chunkConverter.convert(gsonChunks.tomorrow),
                chunkConverter.convert(gsonChunks.sometime)
        )
    }

}
