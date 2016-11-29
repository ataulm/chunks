package com.ataulm.chunks.repository;

import com.ataulm.chunks.ChunkDate;
import com.ataulm.chunks.Chunks;

public class GsonChunksConverter {

    private final GsonChunkConverter chunkConverter;

    public GsonChunksConverter(GsonChunkConverter chunkConverter) {
        this.chunkConverter = chunkConverter;
    }

    public GsonChunks convert(Chunks chunks) {
        GsonChunks gsonChunks = new GsonChunks();
        gsonChunks.todays_date = String.valueOf(chunks.todaysDate().timestamp());
        gsonChunks.today = chunkConverter.convert(chunks.today());
        gsonChunks.tomorrow = chunkConverter.convert(chunks.tomorrow());
        gsonChunks.sometime = chunkConverter.convert(chunks.sometime());
        return gsonChunks;
    }

    public Chunks convert(GsonChunks gsonChunks) {
        return Chunks.create(
                ChunkDate.create(Long.parseLong(gsonChunks.todays_date)),
                chunkConverter.convert(gsonChunks.today),
                chunkConverter.convert(gsonChunks.tomorrow),
                chunkConverter.convert(gsonChunks.sometime)
        );
    }

}
